package fr.checkconsulting.scpi_doc_validation_api.service;

import fr.checkconsulting.scpi_doc_validation_api.dto.AssignPermissionRequest;
import fr.checkconsulting.scpi_doc_validation_api.dto.CreatePermissionRequest;
import fr.checkconsulting.scpi_doc_validation_api.dto.PermissionResponse;
import fr.checkconsulting.scpi_doc_validation_api.dto.UserPermissionsResponse;
import fr.checkconsulting.scpi_doc_validation_api.model.entity.RolePermission;
import fr.checkconsulting.scpi_doc_validation_api.repository.RolePermissionRepository;
import fr.checkconsulting.scpi_doc_validation_api.mapper.RolePermissionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RolePermissionService {

    private final RolePermissionRepository repository;
    private final RolePermissionMapper mapper;

    public UserPermissionsResponse getCurrentUserPermissions(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Utilisateur non authentifié");
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getSubject();
        String username = jwt.getClaim("preferred_username");
        String email = jwt.getClaim("email");

        String userRole = extractUserRole(authentication);

        List<String> permissions = repository.findByRoleName(userRole).stream()
                .map(RolePermission::getPermissionName)
                .collect(Collectors.toList());

        log.info("User {} (role: {}) has {} permissions", username, userRole, permissions.size());

        return UserPermissionsResponse.builder()
                .userId(userId)
                .username(username)
                .email(email)
                .role(userRole)
                .permissions(permissions)
                .build();
    }

    private String extractUserRole(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.replace("ROLE_", "").toLowerCase())
                .collect(Collectors.toList());

        if (roles.contains("premium"))
            return "premium";
        if (roles.contains("standard"))
            return "standard";
        if (roles.contains("admin"))
            return "admin";

        return "standard";
    }

    public List<PermissionResponse> getAllPermissions() {
        List<RolePermission> allRolePermissions = repository.findAll();

        Map<String, List<RolePermission>> groupedByPermission = allRolePermissions.stream()
                .collect(Collectors.groupingBy(RolePermission::getPermissionName));

        return groupedByPermission.entrySet().stream()
                .map(entry -> {
                    String permissionName = entry.getKey();
                    List<RolePermission> rolePermissions = entry.getValue();
                    String description = rolePermissions.isEmpty() ? null : rolePermissions.get(0).getDescription();
                    return mapper.toPermissionResponse(permissionName, description, rolePermissions);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void createPermission(CreatePermissionRequest request) {
        List<RolePermission> existing = repository.findByPermissionName(request.getPermissionName());
        if (!existing.isEmpty()) {
            throw new IllegalArgumentException("La permission existe déjà");
        }

        List<RolePermission> permissions = List.of(
                mapper.toEntity(request, "standard"),
                mapper.toEntity(request, "premium"));

        repository.saveAll(permissions);

    }

    @Transactional
    public void assignPermissionToRole(AssignPermissionRequest request) {
        boolean exists = repository.existsByRoleNameAndPermissionName(
                request.getRoleName(),
                request.getPermissionName());

        if (exists) {
            throw new IllegalArgumentException("Cette permission est déjà assignée à ce rôle");
        }

        List<RolePermission> existingPermissions = repository.findByPermissionName(request.getPermissionName());
        String description = existingPermissions.isEmpty() ? null : existingPermissions.get(0).getDescription();

        RolePermission rolePermission = mapper.toEntity(
                CreatePermissionRequest.builder()
                        .permissionName(request.getPermissionName())
                        .description(description)
                        .build(),
                request.getRoleName());

        repository.save(rolePermission);
    }

    @Transactional
    public void removePermissionFromRole(AssignPermissionRequest request) {
        repository.deleteByRoleNameAndPermissionName(request.getRoleName(), request.getPermissionName());
    }

    public List<String> getPermissionsByRole(String roleName) {
        return repository.findByRoleName(roleName).stream()
                .map(RolePermission::getPermissionName)
                .collect(Collectors.toList());
    }
}
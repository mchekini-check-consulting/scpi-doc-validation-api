package fr.checkconsulting.scpi_doc_validation_api.ressource;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import fr.checkconsulting.scpi_doc_validation_api.dto.UserPermissionsDto;
import fr.checkconsulting.scpi_doc_validation_api.service.PermissionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/permissions")
@Slf4j
public class PermissionResource {
    
    private final PermissionService permissionService;
    
    public PermissionResource(PermissionService permissionService) {
        this.permissionService = permissionService;
    }
    
    @GetMapping("/me")
    public ResponseEntity<UserPermissionsDto> getMyPermissions(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        
        String userId = jwt.getSubject();
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        
    
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = (List<String>) realmAccess.get("roles");
        
    
        String mainRole = determineMainRole(roles);
        
        List<String> permissions = permissionService.getPermissionsByRole(mainRole);
        
        
        
        UserPermissionsDto userPermissions = new UserPermissionsDto(
            userId,
            username,
            email,
            mainRole,
            permissions
        );
        
        return ResponseEntity.ok(userPermissions);
    }
    
    private String determineMainRole(List<String> roles) {
        if (roles.contains("admin")) return "admin";
        if (roles.contains("premium")) return "premium";
        if (roles.contains("standard")) return "standard";
        return "standard";
    }
}

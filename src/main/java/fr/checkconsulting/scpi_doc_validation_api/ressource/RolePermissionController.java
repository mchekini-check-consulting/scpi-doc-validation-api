package fr.checkconsulting.scpi_doc_validation_api.ressource;

import fr.checkconsulting.scpi_doc_validation_api.dto.AssignPermissionRequest;
import fr.checkconsulting.scpi_doc_validation_api.dto.CreatePermissionRequest;
import fr.checkconsulting.scpi_doc_validation_api.dto.PermissionResponse;
import fr.checkconsulting.scpi_doc_validation_api.dto.UserPermissionsResponse;
import fr.checkconsulting.scpi_doc_validation_api.service.RolePermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class RolePermissionController {

    private final RolePermissionService service;


    @GetMapping("/me")
    public ResponseEntity<UserPermissionsResponse> getCurrentUserPermissions(Authentication authentication) {
      
        UserPermissionsResponse response = service.getCurrentUserPermissions(authentication);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PermissionResponse>> getAllPermissions() {
      
        List<PermissionResponse> permissions = service.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }


    @PostMapping
    public ResponseEntity<String> createPermission(@Valid @RequestBody CreatePermissionRequest request) {
        log.info("Creating permission: {}", request.getPermissionName());
        service.createPermission(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Permission créée avec succès");
    }


    @PostMapping("/assign")
    public ResponseEntity<String> assignPermissionToRole(@Valid @RequestBody AssignPermissionRequest request) {
        log.info("Assigning permission {} to role {}", request.getPermissionName(), request.getRoleName());
        service.assignPermissionToRole(request);
        return ResponseEntity.ok("Permission assignée avec succès");
    }


    @DeleteMapping("/assign")
    public ResponseEntity<String> removePermissionFromRole(@Valid @RequestBody AssignPermissionRequest request) {
        log.info("Removing permission {} from role {}", request.getPermissionName(), request.getRoleName());
        service.removePermissionFromRole(request);
        return ResponseEntity.ok("Permission retirée avec succès");
    }

    @GetMapping("/role/{roleName}")
    public ResponseEntity<List<String>> getPermissionsByRole(@PathVariable String roleName) {
        log.info("Getting permissions for role: {}", roleName);
        List<String> permissions = service.getPermissionsByRole(roleName);
        return ResponseEntity.ok(permissions);
    }
}
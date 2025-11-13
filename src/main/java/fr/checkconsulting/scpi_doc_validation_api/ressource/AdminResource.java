package fr.checkconsulting.scpi_doc_validation_api.ressource;


import fr.checkconsulting.scpi_doc_validation_api.dto.UpdateRoleRequest;
import fr.checkconsulting.scpi_doc_validation_api.dto.UserDto;
import fr.checkconsulting.scpi_doc_validation_api.service.UserManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@Slf4j
public class AdminResource {

    private final UserManagementService userManagementService;

    public AdminResource(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {

        List<UserDto> users = userManagementService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @PutMapping("/users/{userId}/role")
    public ResponseEntity<Void> updateUserRole(
            @PathVariable String userId,
            @RequestBody UpdateRoleRequest request) {


        userManagementService.updateUserRole(userId, request.getRole());

        return ResponseEntity.ok().build();
    }
}
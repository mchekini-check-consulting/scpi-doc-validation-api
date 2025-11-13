package fr.checkconsulting.scpi_doc_validation_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignPermissionRequest {
    
    @NotBlank(message = "Le nom de la permission est obligatoire")
    private String permissionName;
    
    @NotBlank(message = "Le rôle est obligatoire")
    private String roleName;  
}

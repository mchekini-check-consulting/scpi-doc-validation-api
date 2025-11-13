package fr.checkconsulting.scpi_doc_validation_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {
    
    private String permissionName;
    private String description;
    private boolean assignedToStandard;  
    private boolean assignedToPremium;   
}
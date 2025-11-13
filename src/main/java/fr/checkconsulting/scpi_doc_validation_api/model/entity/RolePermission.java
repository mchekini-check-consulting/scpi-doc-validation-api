package fr.checkconsulting.scpi_doc_validation_api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "role_permissions")
@CompoundIndex(name = "role_permission_unique", def = "{'roleName': 1, 'permissionName': 1}", unique = true)
public class RolePermission {
    
    @Id
    private String id;
    
    @Indexed
    private String roleName;
    
    private String permissionName;
    
    private String description;
    
    private LocalDateTime createdAt;
}
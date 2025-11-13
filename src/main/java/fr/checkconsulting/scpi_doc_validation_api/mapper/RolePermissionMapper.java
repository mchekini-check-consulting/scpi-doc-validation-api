package fr.checkconsulting.scpi_doc_validation_api.mapper;

import fr.checkconsulting.scpi_doc_validation_api.dto.CreatePermissionRequest;
import fr.checkconsulting.scpi_doc_validation_api.dto.PermissionResponse;
import fr.checkconsulting.scpi_doc_validation_api.model.entity.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "roleName", source = "roleName")
    @Mapping(target = "permissionName", source = "request.permissionName")
    @Mapping(target = "description", source = "request.description")
    RolePermission toEntity(CreatePermissionRequest request, String roleName);


    @Mapping(target = "assignedToStandard", ignore = true)
    @Mapping(target = "assignedToPremium", ignore = true)
    PermissionResponse toDto(RolePermission entity);


    default PermissionResponse toPermissionResponse(String permissionName, String description, 
                                                     List<RolePermission> rolePermissions) {
        boolean assignedToStandard = rolePermissions.stream()
                .anyMatch(rp -> "standard".equalsIgnoreCase(rp.getRoleName()));
        
        boolean assignedToPremium = rolePermissions.stream()
                .anyMatch(rp -> "premium".equalsIgnoreCase(rp.getRoleName()));

        return PermissionResponse.builder()
                .permissionName(permissionName)
                .description(description)
                .assignedToStandard(assignedToStandard)
                .assignedToPremium(assignedToPremium)
                .build();
    }
}
package fr.checkconsulting.scpi_doc_validation_api.repository;

import fr.checkconsulting.scpi_doc_validation_api.model.entity.RolePermission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends MongoRepository<RolePermission, String> {
    
    List<RolePermission> findByRoleName(String roleName);
    
    List<RolePermission> findByPermissionName(String permissionName);
    
    boolean existsByRoleNameAndPermissionName(String roleName, String permissionName);
    
    void deleteByRoleNameAndPermissionName(String roleName, String permissionName);
}
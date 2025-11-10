package fr.checkconsulting.scpi_doc_validation_api.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.checkconsulting.scpi_doc_validation_api.model.entity.RolePermission;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    
    List<RolePermission> findByRoleName(String roleName);
    
    @Query("SELECT rp.permissionName FROM RolePermission rp WHERE rp.roleName = :roleName")
    List<String> findPermissionNamesByRoleName(String roleName);
}

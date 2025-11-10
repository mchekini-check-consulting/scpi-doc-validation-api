package fr.checkconsulting.scpi_doc_validation_api.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import fr.checkconsulting.scpi_doc_validation_api.repository.RolePermissionRepository;

import java.util.List;

@Service
@Slf4j
public class PermissionService {
    
    private final RolePermissionRepository rolePermissionRepository;
    
    public PermissionService(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }
    
    public List<String> getPermissionsByRole(String roleName) {
       
        return rolePermissionRepository.findPermissionNamesByRoleName(roleName.toLowerCase());
    }
}
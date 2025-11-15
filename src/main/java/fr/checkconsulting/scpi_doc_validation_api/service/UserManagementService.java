package fr.checkconsulting.scpi_doc_validation_api.service;

import java.util.Arrays;
import java.util.List;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import fr.checkconsulting.scpi_doc_validation_api.dto.UserDto;
import fr.checkconsulting.scpi_doc_validation_api.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;

@Profile("!test")
@Service
@Slf4j
public class UserManagementService {
    
    @Autowired
    private UsersResource usersResource;
    
    @Autowired
    private RealmResource realmResource;
    
    @Autowired
    private UserMapper userMapper;
    
    public List<UserDto> getAllUsers() {
        List<UserRepresentation> keycloakUsers = usersResource.list();
        List<UserDto> users = userMapper.toDto(keycloakUsers);
        
        users.forEach(user -> {
            user.setRole(getUserRole(user.getId()));
        });
        
        return users;
    }
    
    public void updateUserRole(String userId, String newRole) {
      
        
        var userResource = realmResource.users().get(userId);
        
   
        RoleRepresentation standardRole = realmResource.roles().get("standard").toRepresentation();
        RoleRepresentation premiumRole = realmResource.roles().get("premium").toRepresentation();
        

        userResource.roles().realmLevel().remove(Arrays.asList(standardRole, premiumRole));
        
      
        if ("premium".equals(newRole)) {
            userResource.roles().realmLevel().add(Arrays.asList(premiumRole));
        } else if ("standard".equals(newRole)) {
            userResource.roles().realmLevel().add(Arrays.asList(standardRole));
        } else {
            throw new IllegalArgumentException("Invalid role: " + newRole);
        }
    
    }
    
    private String getUserRole(String userId) {
        var userRoles = realmResource.users()
            .get(userId)
            .roles()
            .realmLevel()
            .listEffective();
        
        boolean isPremium = userRoles.stream()
            .anyMatch(role -> role.getName().equals("premium"));
        
        boolean isStandard = userRoles.stream()
            .anyMatch(role -> role.getName().equals("standard"));
        
        if (isPremium) return "premium";
        if (isStandard) return "standard";
        return "none";
    }
}

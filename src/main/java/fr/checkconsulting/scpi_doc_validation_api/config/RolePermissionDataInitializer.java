package fr.checkconsulting.scpi_doc_validation_api.config;

import fr.checkconsulting.scpi_doc_validation_api.model.entity.RolePermission;
import fr.checkconsulting.scpi_doc_validation_api.repository.RolePermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RolePermissionDataInitializer implements CommandLineRunner {

    private final RolePermissionRepository repository;

    @Override
    public void run(String... args) {
       
        if (repository.count() > 0) {
          
            return;
        }

        log.info("Initializing role permissions data...");

        List<RolePermission> permissions = new ArrayList<>();

      
        permissions.add(createPermission("standard", "VIEW_SCPI_LIST", "Accès à la liste des SCPI"));
        permissions.add(createPermission("standard", "VIEW_PORTFOLIO", "Accès au portefeuille"));
        permissions.add(createPermission("standard", "VIEW_SCHEDULED_PAYMENTS", "Accès aux versements programmés"));
        permissions.add(createPermission("standard", "VIEW_SIMULATIONS", "Accès aux simulations"));
        permissions.add(createPermission("standard", "VIEW_DOCUMENTS", "Accès aux documents réglementaires"));
        permissions.add(createPermission("standard", "VIEW_REQUEST_HISTORY", "Accès à l'historique des demandes"));

     
        permissions.add(createPermission("premium", "VIEW_SCPI_LIST", "Accès à la liste des SCPI"));
        permissions.add(createPermission("premium", "VIEW_PORTFOLIO", "Accès au portefeuille"));
        permissions.add(createPermission("premium", "VIEW_SCHEDULED_PAYMENTS", "Accès aux versements programmés"));
        permissions.add(createPermission("premium", "VIEW_SIMULATIONS", "Accès aux simulations"));
        permissions.add(createPermission("premium", "VIEW_DOCUMENTS", "Accès aux documents réglementaires"));
        permissions.add(createPermission("premium", "VIEW_REQUEST_HISTORY", "Accès à l'historique des demandes"));
        permissions.add(createPermission("premium", "VIEW_COMPARATOR", "Accès au comparateur SCPI"));

    
        repository.saveAll(permissions);

        log.info("Role permissions initialized successfully. Total: {}", permissions.size());
    }

    private RolePermission createPermission(String roleName, String permissionName, String description) {
        return RolePermission.builder()
                .roleName(roleName)
                .permissionName(permissionName)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
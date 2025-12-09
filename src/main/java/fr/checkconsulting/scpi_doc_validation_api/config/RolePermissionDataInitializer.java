package fr.checkconsulting.scpi_doc_validation_api.config;

import fr.checkconsulting.scpi_doc_validation_api.model.entity.RolePermission;
import fr.checkconsulting.scpi_doc_validation_api.repository.RolePermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class RolePermissionDataInitializer implements CommandLineRunner {

    private final RolePermissionRepository repository;
    private final MongoTemplate mongoTemplate;

 
    private static final Map<String, String> PERMISSION_DISPLAY_NAMES = new HashMap<>();

    static {
        PERMISSION_DISPLAY_NAMES.put("VIEW_PORTFOLIO", "Portefeuille");
        PERMISSION_DISPLAY_NAMES.put("VIEW_DOCUMENTS", "Documents Réglementaires");
        PERMISSION_DISPLAY_NAMES.put("VIEW_SIMULATIONS", "Simulations");
        PERMISSION_DISPLAY_NAMES.put("VIEW_SCPI_LIST", "Liste des SCPI");
        PERMISSION_DISPLAY_NAMES.put("VIEW_COMPARATOR", "Comparateur SCPI");
        PERMISSION_DISPLAY_NAMES.put("VIEW_SCHEDULED_PAYMENTS", "Versements programmés");
        PERMISSION_DISPLAY_NAMES.put("VIEW_COMPLENCE", "Compliance");
        PERMISSION_DISPLAY_NAMES.put("VIEW_REQUEST_HISTORY", "Historique des demandes");
    }

    @Override
    public void run(String... args) {
        long existingCount = repository.count();

        // Si des permissions existent déjà, exécuter la migration pour ajouter 'name'
        if (existingCount > 0) {
            log.info("🔄 Permissions existantes détectées ({}), vérification du champ 'name'...", existingCount);
            migrateExistingPermissions();
            return;
        }

        initializePermissions();
    }


    private void migrateExistingPermissions() {
        int totalUpdated = 0;

        for (Map.Entry<String, String> entry : PERMISSION_DISPLAY_NAMES.entrySet()) {
            String permissionName = entry.getKey();
            String displayName = entry.getValue();

            // Mettre à jour seulement les documents qui n'ont pas de 'name'
            Query query = new Query(Criteria.where("permissionName").is(permissionName)
                    .and("name").exists(false));

            Update update = new Update().set("name", displayName);

            long updated = mongoTemplate.updateMulti(query, update, RolePermission.class).getModifiedCount();

            if (updated > 0) {
                log.info("✅ Ajout du nom '{}' à la permission {} : {} document(s)", 
                        displayName, permissionName, updated);
                totalUpdated += updated;
            }
        }

        if (totalUpdated > 0) {
            log.info("🎉 Migration terminée : {} document(s) mis à jour avec le champ 'name'", totalUpdated);
        } else {
            log.info("✅ Aucune migration nécessaire : toutes les permissions ont déjà un champ 'name'");
        }

        // Vérification finale
        long countWithoutName = mongoTemplate.count(
                new Query(Criteria.where("name").exists(false)),
                RolePermission.class
        );

        if (countWithoutName > 0) {
            log.warn("⚠️  Attention : {} permission(s) n'ont toujours pas de champ 'name'", countWithoutName);
        }
    }

    /**
     * Initialisation des permissions pour une nouvelle installation
     */
    private void initializePermissions() {
        List<RolePermission> permissions = new ArrayList<>();

        // Permissions STANDARD
        permissions.add(createPermission("standard", "VIEW_SCPI_LIST", 
                "Liste des SCPI", "Accès à la liste des SCPI"));
        permissions.add(createPermission("standard", "VIEW_PORTFOLIO", 
                "Portefeuille", "Accès au portefeuille"));
        permissions.add(createPermission("standard", "VIEW_SCHEDULED_PAYMENTS", 
                "Versements programmés", "Accès aux versements programmés"));
        permissions.add(createPermission("standard", "VIEW_SIMULATIONS", 
                "Simulations", "Accès aux simulations"));
        permissions.add(createPermission("standard", "VIEW_DOCUMENTS", 
                "Documents Réglementaires", "Accès aux documents réglementaires"));
        permissions.add(createPermission("standard", "VIEW_REQUEST_HISTORY", 
                "Historique des demandes", "Accès à l'historique des demandes"));

        // Permissions PREMIUM (inclut toutes celles de Standard + extras)
        permissions.add(createPermission("premium", "VIEW_SCPI_LIST", 
                "Liste des SCPI", "Accès à la liste des SCPI"));
        permissions.add(createPermission("premium", "VIEW_PORTFOLIO", 
                "Portefeuille", "Accès au portefeuille"));
        permissions.add(createPermission("premium", "VIEW_SCHEDULED_PAYMENTS", 
                "Versements programmés", "Accès aux versements programmés"));
        permissions.add(createPermission("premium", "VIEW_SIMULATIONS", 
                "Simulations", "Accès aux simulations"));
        permissions.add(createPermission("premium", "VIEW_DOCUMENTS", 
                "Documents Réglementaires", "Accès aux documents réglementaires"));
        permissions.add(createPermission("premium", "VIEW_REQUEST_HISTORY", 
                "Historique des demandes", "Accès à l'historique des demandes"));
        permissions.add(createPermission("premium", "VIEW_COMPARATOR", 
                "Comparateur SCPI", "Accès au comparateur SCPI"));

        repository.saveAll(permissions);

        log.info("🎉 Permissions initialisées avec succès. Total : {}", permissions.size());
    }


    private RolePermission createPermission(String roleName, String permissionName, 
                                           String name, String description) {
        return RolePermission.builder()
                .roleName(roleName)
                .permissionName(permissionName)
                .name(name) 
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
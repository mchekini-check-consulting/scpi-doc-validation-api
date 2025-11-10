package fr.checkconsulting.scpi_doc_validation_api.config;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class KeycloakAdminConfig {

    @Value("${keycloak.admin.server-url}")
    private String serverUrl;

    @Value("${keycloak.admin.realm}")
    private String authRealm;

    @Value("${keycloak.admin.target-realm}")
    private String targetRealm;

    @Value("${keycloak.admin.username}")
    private String username;

    @Value("${keycloak.admin.password}")
    private String password;

    @Bean
    public Keycloak keycloak() {
    

        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(authRealm)
                    .username(username)
                    .password(password)
                    .clientId("admin-cli")
                    .grantType("password")
                    .build();

  
            keycloak.serverInfo().getInfo();
            

            return keycloak;
        } catch (Exception e) {
            log.error("Failed to connect to Keycloak", e);
            throw new RuntimeException("Failed to connect to Keycloak", e);
        }
    }

    @Bean
    public RealmResource realmResource(Keycloak keycloak) {

        try {
            RealmResource realmResource = keycloak.realm(targetRealm);

            realmResource.toRepresentation();

            return realmResource;
        } catch (Exception e) {

            throw new RuntimeException("Failed to access realm: " + targetRealm, e);
        }
    }

    @Bean
    public UsersResource usersResource(RealmResource realmResource) {
        return realmResource.users();
    }
}
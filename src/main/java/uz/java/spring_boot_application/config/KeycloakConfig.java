package uz.java.spring_boot_application.config;

import org.keycloak.admin.client.Keycloak;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakConfig {

    @Bean
    public Keycloak keycloak(KeycloakProperties p) {

        return org.keycloak.admin.client.KeycloakBuilder.builder()
                .serverUrl(p.keycloakServerUrl())
                .realm(p.masterRealm()) // master
                .clientId(p.adminClientId())
                .clientSecret(p.clientSecret())
                .username(p.adminUsername())
                .password(p.adminPassword())
                .build();
    }
}

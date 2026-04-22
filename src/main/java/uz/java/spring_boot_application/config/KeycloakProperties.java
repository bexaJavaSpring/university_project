package uz.java.spring_boot_application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.keycloak")
public record KeycloakProperties(String keycloakServerUrl, String masterRealm, String realm, String adminUsername,
                                 String adminPassword, String adminClientId, String clientSecret) {
}

package com.restaurante.api.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AdminBootstrapInitializer {

    private final UserManagementService userManagementService;

    @Value("${app.bootstrap-admin.name:}")
    private String bootstrapAdminName;

    @Value("${app.bootstrap-admin.email:}")
    private String bootstrapAdminEmail;

    @Value("${app.bootstrap-admin.password:}")
    private String bootstrapAdminPassword;

    @Bean
    public ApplicationRunner bootstrapAdminRunner() {
        return args -> {
            userManagementService.bootstrapAdminIfNeeded(
                    bootstrapAdminName,
                    bootstrapAdminEmail,
                    bootstrapAdminPassword
            );
            if (!bootstrapAdminEmail.isBlank()) {
                log.info("Bootstrap admin check completed");
            }
        };
    }
}

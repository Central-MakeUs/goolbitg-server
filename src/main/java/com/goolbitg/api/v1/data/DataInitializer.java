package com.goolbitg.api.v1.data;

import java.util.Set;

import com.goolbitg.api.v1.security.AdminUserDetailsService;
import com.goolbitg.api.v1.security.RoleType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * DataInitializer
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Profile({ "local" })
public class DataInitializer implements CommandLineRunner {

    private final AdminUserDetailsService adminUserDetailsService;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        log.info("Load Initial Data");

        adminUserDetailsService.registerAdminUser(
            "goolbitg@gmail.com",
            adminPassword,
            Set.of(RoleType.ROLE_ADMIN, RoleType.ROLE_MANAGER, RoleType.ROLE_DEVELOPER)
        );
    }


}

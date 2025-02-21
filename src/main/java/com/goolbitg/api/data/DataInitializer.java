package com.goolbitg.api.data;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.goolbitg.api.security.AdminUserDetailsService;
import com.goolbitg.api.security.RoleType;

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
            Set.of(RoleType.ADMIN, RoleType.MANAGER, RoleType.DEVELOPER)
        );
    }


}

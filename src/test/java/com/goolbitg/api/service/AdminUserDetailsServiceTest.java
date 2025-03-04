package com.goolbitg.api.service;

import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.goolbitg.api.v1.security.AdminUserDetailsService;
import com.goolbitg.api.v1.security.RoleType;

/**
 * AdminUserDetailsServiceTest
 */
@SpringBootTest
public class AdminUserDetailsServiceTest {

    @Autowired
    private AdminUserDetailsService adminUserDetailsService;

    @Test
    @Disabled
    void register_a_admin_user() {
        adminUserDetailsService.registerAdminUser("test@gmail.com", "asdf1234", Set.of(RoleType.ROLE_ADMIN));
    }

}

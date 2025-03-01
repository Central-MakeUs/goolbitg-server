package com.goolbitg.api.v1.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import com.goolbitg.api.v1.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.goolbitg.api.v1.entity.AdminUser;

import lombok.extern.slf4j.Slf4j;

/**
 * DatabaseUserDetailsService
 */
@Service
@Slf4j
public class AdminUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminUserRepository adminUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser user = adminUserRepository.findByEmail(username).orElseThrow();

        Collection<SimpleGrantedAuthority> authorities = user.getRoles()
            .stream().map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toSet());

        return new User(user.getEmail(), user.getPassword(), authorities);
    }

    public void registerAdminUser(String email, String password, Set<RoleType> roles) {
        if (adminUserRepository.existsById(email)) {
            log.info("root user already exists.");
            return;
        }

        AdminUser newAdminUser = AdminUser.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(roles)
                .build();
        adminUserRepository.save(newAdminUser);
    }

    public void grantRole(String email, RoleType role) {
        AdminUser adminUser = adminUserRepository.findByEmail(email).orElseThrow();

        adminUser.getRoles().add(role);

        adminUserRepository.save(adminUser);
    }

}

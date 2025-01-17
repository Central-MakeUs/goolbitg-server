package com.goolbitg.api.security;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * AuthUtil
 */
public class AuthUtil {

    public static String getLoginUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String id;
        if (principal instanceof UserDetails) {
            id = ((UserDetails) principal).getUsername();
        } else if (principal instanceof Jwt) {
            id = ((Jwt) principal).getSubject();
        } else {
            id = principal.toString();
        }
        return id;
    }

    public static UserDetails createUserDetails(String userId) {
        UserDetails details = org.springframework.security.core.userdetails.User
            .withUsername(userId)
            .password("")
            .authorities(List.of())
            .build();
        return details;
    }
}

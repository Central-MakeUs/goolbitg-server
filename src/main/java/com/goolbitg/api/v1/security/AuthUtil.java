package com.goolbitg.api.v1.security;

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
}

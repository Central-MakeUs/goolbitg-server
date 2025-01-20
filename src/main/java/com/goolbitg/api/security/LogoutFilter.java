package com.goolbitg.api.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.goolbitg.api.repository.UserTokenRepository;

import lombok.RequiredArgsConstructor;

/**
 * LogoutFilter
 */
@Component
@RequiredArgsConstructor
public class LogoutFilter extends OncePerRequestFilter {

    @Autowired
    private final UserTokenRepository userTokenRepository;
    @Autowired
    private final JwtDecoder jwtDecoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            Jwt jwt = jwtDecoder.decode(token);
            String userId = jwt.getSubject();
            if (!userTokenRepository.isLoggedIn(userId)) {
                return;
            }
        }

        doFilter(request, response, filterChain);
    }

}

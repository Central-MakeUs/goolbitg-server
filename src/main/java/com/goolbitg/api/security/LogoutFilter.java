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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goolbitg.api.exception.AuthException;
import com.goolbitg.api.exception.CommonException;
import com.goolbitg.api.model.ErrorDto;
import com.goolbitg.api.repository.UserTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * LogoutFilter
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogoutFilter extends OncePerRequestFilter {

    @Autowired
    private final UserTokenRepository userTokenRepository;
    @Autowired
    private final JwtDecoder jwtDecoder;
    @Autowired
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            Jwt jwt = jwtDecoder.decode(token);
            String userId = jwt.getSubject();
            if (!userTokenRepository.isLoggedIn(userId)) {
                CommonException ex = AuthException.loggedOut();
                response.setStatus(ex.getStatus().value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                ErrorDto body = new ErrorDto();
                body.setCode(ex.getCode());
                body.setMessage(ex.getMessage());

                String json = objectMapper.writeValueAsString(body);
                response.getWriter().write(json);
                return;
            }
        }

        doFilter(request, response, filterChain);
    }

}

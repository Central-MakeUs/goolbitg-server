package com.goolbitg.api.v1.controller;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * ControllerUtils
 */
public class ControllerUtils {

    public static Optional<NativeWebRequest> getRequest() {
        ServletRequestAttributes attributes =
    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest servletRequest = attributes.getRequest();
            HttpServletResponse servletResponse = attributes.getResponse();
            return Optional.of(new ServletWebRequest(servletRequest, servletResponse));
        }
        return Optional.empty();
    }
    
}

package com.goolbitg.api.v1.component;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.swing.text.AbstractDocument;

@Slf4j
@Component
public class RequestLogginInterceptor implements HandlerInterceptor {

    private static final int BODY_LENGTH_LIMIT = 1000;

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        StringBuilder headerBuilder = new StringBuilder();
        for (Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements();) {
            String header = e.nextElement();
            headerBuilder.append(header + " = [");
            for (Enumeration<String> ve = request.getHeaders(header); ve.hasMoreElements();) {
                headerBuilder.append(ve.nextElement() + ", ");
            }
            headerBuilder.append("], ");
        }
        // TODO: This Interceptor has bug that absorb all request body 
        // before pass to the controller. Should be handled later.

//        request.getInputStream().
//        ContentCachingRequestWrapper wrapper = new ContentCachingRequestWrapper(request);
//        String body = wrapper.getReader().lines().collect(Collectors.joining());
//
//        log.info(
//            "Incoming request: method=({}), uri=({}), ip=({}), headers=({}), body=({})",
//            request.getMethod(),
//            request.getRequestURI(),
//            request.getRemoteAddr(),
//            headerBuilder,
//            body
//        );
//
        return true;
    }

}

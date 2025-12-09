package com.goolbitg.api.v1.component;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

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

        var bodyInputStream = request.getInputStream();
        String body = new String(bodyInputStream.readNBytes(BODY_LENGTH_LIMIT), StandardCharsets.UTF_8);
        if (!bodyInputStream.isFinished()) {
            body = String.format("Body exceeds %d bytes", BODY_LENGTH_LIMIT);
        }

        log.info(
            "Incoming request: method=({}), uri=({}), ip=({}), headers=({}), body=({})",
            request.getMethod(),
            request.getRequestURI(),
            request.getRemoteAddr(),
            headerBuilder.toString(),
            body
        );

        return true;
    }

}

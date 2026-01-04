package com.goolbitg.api.v1.controller;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 */
@RestController
public class TestController {

    @PostMapping("/test")
    public ResponseEntity<String> getTest(TestRequest body) {
        System.out.println(body);
        return new ResponseEntity<>("Test OK", HttpStatus.OK);
    }

    @Data
    public static class TestRequest {
        private String message;
    }

}

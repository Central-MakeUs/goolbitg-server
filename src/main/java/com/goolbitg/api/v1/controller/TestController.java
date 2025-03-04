package com.goolbitg.api.v1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<String> getTest() {
        return new ResponseEntity<>("Test OK", HttpStatus.OK);
    }

}

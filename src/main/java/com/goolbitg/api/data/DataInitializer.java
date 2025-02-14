package com.goolbitg.api.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.goolbitg.api.service.ChallengeService;
import com.goolbitg.api.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * DataInitializer
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("Load Initial Data");
    }

}

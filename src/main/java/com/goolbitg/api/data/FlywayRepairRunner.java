package com.goolbitg.api.data;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * FlywayRepairRunner
 */
@Slf4j
@Component
public class FlywayRepairRunner implements CommandLineRunner {

    @Autowired
    private Flyway flyway;

    @Override
    public void run(String... args) throws Exception {
        flyway.repair();
        log.info("Flyway schema history repaired");
    }

}

package com.wexom.zonkydemo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ZonkyDemoApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZonkyDemoApplicationTests.class);

    @Test
    void contextLoads() {
        LOGGER.info("Application context loaded successfully.");
    }

}

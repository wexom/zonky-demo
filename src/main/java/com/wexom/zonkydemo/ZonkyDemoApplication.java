package com.wexom.zonkydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot main class.
 */
@SpringBootApplication
@EnableScheduling
public class ZonkyDemoApplication {

    /**
     * Main method.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ZonkyDemoApplication.class, args);
    }

}

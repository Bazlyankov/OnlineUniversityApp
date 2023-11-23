package com.studentsystemapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OnlineUniversityApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineUniversityApplication.class, args);
    }
}

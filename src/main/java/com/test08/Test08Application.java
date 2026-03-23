package com.test08;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class Test08Application {

    public static void main(String[] args) {
        SpringApplication.run(Test08Application.class, args);
    }
}

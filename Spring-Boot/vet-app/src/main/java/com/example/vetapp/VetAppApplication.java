package com.example.vetapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class VetAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(VetAppApplication.class, args);
    }
}
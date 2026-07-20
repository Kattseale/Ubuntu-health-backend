package com.ubuntuhealth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ubuntuhealth")
public class UbuntuHealthBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(UbuntuHealthBackendApplication.class, args);
    }
}
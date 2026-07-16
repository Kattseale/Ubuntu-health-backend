package com.ubuntuhealth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UbuntuHealthApplication {

	public static void main(String[] args) {
		SpringApplication.run(UbuntuHealthApplication.class, args);
	}
}
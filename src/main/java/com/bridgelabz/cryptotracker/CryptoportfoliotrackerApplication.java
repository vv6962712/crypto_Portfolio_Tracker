package com.bridgelabz.cryptotracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CryptoportfoliotrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoportfoliotrackerApplication.class, args);
	}

}

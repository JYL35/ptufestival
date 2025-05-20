package com.capstone7.ptufestival;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PtufestivalApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtufestivalApplication.class, args);
	}

}

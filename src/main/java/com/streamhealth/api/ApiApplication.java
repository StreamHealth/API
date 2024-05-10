package com.streamhealth.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Objects;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
		System.setProperty("PASSWORD", Objects.requireNonNull(dotenv.get("PASSWORD")));
		SpringApplication.run(ApiApplication.class, args);
	}

}

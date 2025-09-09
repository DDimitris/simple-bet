package com.dimitris.microservices.simple_bet;

import org.springframework.boot.SpringApplication;

public class TestSimpleBetApplication {

	public static void main(String[] args) {
		SpringApplication.from(SimpleBetApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

package com.dimitris.microservices.simple_bet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SimpleBetApplicationTests {

	@Test
	void contextLoads() {
	}

}

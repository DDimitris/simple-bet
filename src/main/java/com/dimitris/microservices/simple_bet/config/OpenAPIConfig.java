package com.dimitris.microservices.simple_bet.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI simpleBetApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Simple Bet Service API")
                        .version("0.0.1")
                        .license(new License().name("Apache 2.0"))
                        .description("API for managing simple bets in the simple bet service")).externalDocs(new ExternalDocumentation().description("You can refer to the github page README.md for more information").url("https://github.com/DDimitris/simple-bet"));
    }
}

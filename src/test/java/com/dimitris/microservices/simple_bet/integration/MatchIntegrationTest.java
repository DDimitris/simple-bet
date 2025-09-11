package com.dimitris.microservices.simple_bet.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.dimitris.microservices.simple_bet.utils.HelperMethods.mockRequestContent;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("integration")
class MatchIntegrationTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void testCreateAndGetMatch() {
        // create
        given()
                .contentType("application/json")
                .body(mockRequestContent)
                .when()
                .post("/api/matches")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("teamA", is("Olimpiakos"));

        // get
        given()
                .when()
                .get("/api/matches?page=0&size=3&sortBy=id&direction=desc")
                .then()
                .statusCode(200)
                .body("content[0].teamB", is("Panathinaikos"));
    }

    @Test
    void testUpdateMatch() {
        // create
        Integer matchId =
                given()
                        .contentType("application/json")
                        .body(mockRequestContent)
                        .when()
                        .post("/api/matches")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .path("id");

        // update
        String updateRequest = """
        {
            "teamA": "AEK",
            "teamB": "PAO",
            "matchDate": "2025-10-10",
            "matchTime": "20:00:00",
            "sport": "FOOTBALL",
            "odds": [
                {"specifier":"1","odd":1.5},
                {"specifier":"X","odd":2.5},
                {"specifier":"2","odd":3.5}
            ]
        }
        """;

        given()
                .contentType("application/json")
                .body(updateRequest)
                .when()
                .put("/api/matches/{id}", matchId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("teamA", is("AEK"))
                .body("teamB", is("PAO"));
    }

    @Test
    void testDeleteMatch() {
        // create
        Integer matchId =
                given()
                        .contentType("application/json")
                        .body(mockRequestContent)
                        .when()
                        .post("/api/matches")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .path("id");

        // delete
        given()
                .when()
                .delete("/api/matches/{id}", matchId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // verify not found
        given()
                .when()
                .delete("/api/matches/{id}", matchId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

}

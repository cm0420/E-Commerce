package com.miguel.ecommerce;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public abstract class AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/v1";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    protected String createUserAndGetToken(String email, String password, String role, String cpf) {
        given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                {
                    "firstName": "Test",
                    "lastName": "User",
                    "email": "%s",
                    "password": "%s",
                    "phoneNumber": "38999204008",
                    "cpf": "%s",
                    "role": "%s"
                }
                """, email, password, cpf, role))
                .when()
                .post("/users");

        return given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                {
                    "email": "%s",
                    "password": "%s"
                }
                """, email, password))
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }


    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.4");

        private static void startMySQLContainer() {
            Startables.deepStart(Stream.of(mysqlContainer)).join();
        }

        private Map<String, String> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", mysqlContainer.getJdbcUrl(),
                    "spring.datasource.username", mysqlContainer.getUsername(),
                    "spring.datasource.password", mysqlContainer.getPassword()
            );
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startMySQLContainer();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testcontainers = new MapPropertySource("testcontainers", (Map) createConnectionConfiguration());
            environment.getPropertySources().addFirst(testcontainers);
        }


    }
}

package com.miguel.ecommerce.auth;

import com.miguel.ecommerce.AbstractIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
public class AuthControllerTest extends AbstractIntegrationTest {
    @Test
    void shouldLoginSuccessfully() {
        // 1. Criar o usuário primeiro (usando CPF limpo)
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "firstName": "Miguel",
                    "lastName": "Costa",
                    "email": "login@test.com",
                    "password": "senha123",
                    "phoneNumber": "38999204008",
                    "cpf": "52998224725",
                    "role": "CLIENT"
                }
                """)
                .when()
                .post("/users")
                .then()
                .statusCode(201);

        // 2. Tentar o login
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "email": "login@test.com",
                    "password": "senha123"
                }
                """)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue()); // Verifica se o JWT foi gerado
    }

    @Test
    void shouldReturn403WhenCredentialsAreInvalid() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "email": "naoexiste@test.com",
                    "password": "senhaerrada"
                }
                """)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(403);
    }
}

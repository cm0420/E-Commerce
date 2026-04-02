package com.miguel.ecommerce.usercontroller;

import com.miguel.ecommerce.TestUtils;
import com.miguel.ecommerce.AbstractIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
public class UserControllerTest extends AbstractIntegrationTest {
    @Test
    void shouldCreateUserSuccessfully() {
        String email = TestUtils.generateRandomEmail();
        String cpf = TestUtils.generateRandomCpf();

        given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                {
                    "firstName": "Miguel",
                    "lastName": "Costa",
                    "email": "%s",
                    "password": "senha_segura_123",
                    "phoneNumber": "38999204008",
                    "cpf": "%s", 
                    "role": "CLIENT"
                }
                """, email, cpf))
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("email", equalTo(email));
    }

    @Test
    void shouldReturnValidationErrorWhenEmailIsInvalid() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "firstName": "Miguel",
                    "lastName": "Costa",
                    "email": "email-invalido",
                    "password": "senha123",
                    "phoneNumber": "38999204008",
                    "cpf": "529.982.247-25",
                    "role": "CLIENT"
                }
                """)
                .when()
                .post("/users")
                .then()
                .statusCode(422) // Verificando se o Spring Validation barrou o e-mail
                .body("error", equalTo("VALIDATION_ERROR"));
    }

    @Test
    void shouldReturnErrorWhenCpfIsInvalid() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "firstName": "Miguel",
                    "lastName": "Costa",
                    "email": "cpf@test.com",
                    "password": "senha123",
                    "phoneNumber": "38999204008",
                    "cpf": "111.111.111-11",
                    "role": "CLIENT"
                }
                """)
                .when()
                .post("/users")
                .then()
                .statusCode(422) // Verificando se a anotação @CPF funcionou
                .body("error", equalTo("VALIDATION_ERROR"));
    }
}

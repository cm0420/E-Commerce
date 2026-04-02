package com.miguel.ecommerce.category;

import com.miguel.ecommerce.TestUtils;
import com.miguel.ecommerce.AbstractIntegrationTest;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class CategoryControllerTest extends AbstractIntegrationTest {

    private String adminToken;

    @BeforeEach
    void setup() {
        // Criando admin com dados aleatórios e CPF válido para passar no setup
        adminToken = createUserAndGetToken(
                TestUtils.generateRandomEmail(),
                "admin123",
                "ADMIN",
                TestUtils.generateRandomCpf()
        );
    }

    @Test
    void shouldCreateCategorySuccessfully() {
        String categoryName = "Cat-" + UUID.randomUUID().toString().substring(0, 8);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(String.format("""
                {
                    "name": "%s",
                    "description": "Descrição da categoria"
                }
                """, categoryName))
                .when()
                .post("/categories")
                .then()
                .statusCode(201)
                .body("name", equalTo(categoryName))
                .body("isActive", equalTo(true));
    }

    @Test
    void shouldReturn403WhenCreatingCategoryWithoutAdminRole() {
        // Criando um cliente comum dinamicamente
        String clientToken = createUserAndGetToken(
                TestUtils.generateRandomEmail(),
                "senha123",
                "CLIENT",
                TestUtils.generateRandomCpf()
        );

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + clientToken)
                .body("{\"name\": \"Categoria Proibida\"}")
                .when()
                .post("/categories")
                .then()
                .statusCode(403);
    }

    @Test
    void shouldReturn400WhenCategoryNameAlreadyExists() {
        // Geramos um nome único para este teste específico
        String duplicateName = "Duplicate-" + UUID.randomUUID().toString().substring(0, 5);

        // Primeira criação
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(String.format("{\"name\": \"%s\"}", duplicateName))
                .when()
                .post("/categories")
                .then()
                .statusCode(201);

        // Segunda criação com o mesmo nome
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(String.format("{\"name\": \"%s\"}", duplicateName))
                .when()
                .post("/categories")
                .then()
                .statusCode(400)
                .body("error", equalTo("BUSINESS_ERROR"));
    }

    @Test
    void shouldDeactivateCategorySuccessfully() {
        // Criar uma categoria para poder desativar
        String name = "Deactivate-" + UUID.randomUUID().toString().substring(0, 5);

        Integer id = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(String.format("{\"name\": \"%s\"}", name))
                .when()
                .post("/categories")
                .then()
                .extract()
                .path("id");

        // Desativar
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .patch("/categories/" + id + "/deactivate")
                .then()
                .statusCode(204);
    }

    @Test
    void shouldListCategoriesPublicly() {
        given()
                .when()
                .get("/categories")
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }
}
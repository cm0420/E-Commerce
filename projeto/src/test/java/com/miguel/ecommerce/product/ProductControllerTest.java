package com.miguel.ecommerce.product;

import com.miguel.ecommerce.AbstractIntegrationTest;
import com.miguel.ecommerce.TestUtils;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProductControllerTest extends AbstractIntegrationTest {
    private String adminToken;
    private Integer categoryId;

    @BeforeEach
    void setup() {
        // Criando admin dinâmico
        adminToken = createUserAndGetToken(
                TestUtils.generateRandomEmail(),
                "admin123",
                "ADMIN",
                TestUtils.generateRandomCpf()
        );

        // Criando uma categoria base para os produtos
        String catName = "Cat-Prod-" + UUID.randomUUID().toString().substring(0, 5);
        categoryId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(String.format("{\"name\": \"%s\"}", catName))
                .when()
                .post("/categories")
                .then()
                .extract().path("id");
    }

    @Test
    void shouldCreateProductSuccessfully() {
        String sku = "SKU-" + UUID.randomUUID().toString().substring(0, 10);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(String.format("""
                        {
                            "name": "Produto Teste",
                            "description": "Descrição do produto",
                            "price": 199.90,
                            "costPrice": 100.00,
                            "sku": "%s",
                            "categoryId": %d,
                            "imageUrl": "https://example.com/imagem.jpg"
                        
                        }
                        """, sku, categoryId))
                .when()
                .post("/products")
                .then()
                .statusCode(201)
                .body("name", equalTo("Produto Teste"))
                .body("sku", equalTo(sku))
                .body("isActive", equalTo(true));
    }

    @Test
    void shouldReturn400WhenSkuAlreadyExists() {
        String duplicateSku = "SKU-DUP-" + UUID.randomUUID().toString().substring(0, 5);

        // Primeira criação (OK)
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(String.format("""
                        {
                            "name": "Prod 1",
                            "price": 10.0,
                            "costPrice": 5.0,
                            "sku": "%s",
                            "categoryId": %d
                        }
                        """, duplicateSku, categoryId))
                .when()
                .post("/products")
                .then()
                .statusCode(201);

        // Segunda criação com mesmo SKU (ERRO)
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(String.format("""
                        {
                            "name": "Prod 2",
                            "price": 20.0,
                            "costPrice": 10.0,
                            "sku": "%s",
                            "categoryId": %d
                        }
                        """, duplicateSku, categoryId))
                .when()
                .post("/products")
                .then()
                .statusCode(400)
                .body("error", equalTo("BUSINESS_ERROR"));
    }

    @Test
    void shouldReturn403WhenClientTriesToCreateProduct() {
        String clientToken = createUserAndGetToken(
                TestUtils.generateRandomEmail(),
                "senha123",
                "CLIENT",
                TestUtils.generateRandomCpf()
        );

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + clientToken)
                .body(String.format("""
                        {
                            "name": "Produto Proibido",
                            "price": 100.00,
                            "costPrice": 50.00,
                            "sku": "SKU-PROIBIDO",
                            "categoryId": %d
                        }
                        """, categoryId))
                .when()
                .post("/products")
                .then()
                .statusCode(403);
    }

    @Test
    void shouldListProductsPublicly() {
        given()
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }
}

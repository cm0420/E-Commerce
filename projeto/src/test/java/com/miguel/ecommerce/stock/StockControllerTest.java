package com.miguel.ecommerce.stock;

import com.miguel.ecommerce.AbstractIntegrationTest;
import com.miguel.ecommerce.TestUtils;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class StockControllerTest extends AbstractIntegrationTest {
    private String adminToken;
    private Integer productId;

    @BeforeEach
    void setup() {
        // 1. Criar admin dinâmico com TestUtils para evitar "Duplicate Entry"
        adminToken = createUserAndGetToken(
                TestUtils.generateRandomEmail(),
                "admin123",
                "ADMIN",
                TestUtils.generateRandomCpf()
        );

        // 2. Criar Categoria base com nome dinâmico
        String categoryName = "Cat-Stock-" + UUID.randomUUID().toString().substring(0, 5);
        Integer categoryId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(String.format("{\"name\": \"%s\"}", categoryName))
                .when()
                .post("/categories")
                .then()
                .extract().path("id");

        // 3. Criar Produto base com SKU dinâmico e campo imageUrl correto
        String sku = "SKU-STK-" + UUID.randomUUID().toString().substring(0, 8);
        productId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(String.format("""
                        {
                            "name": "Produto Stock",
                            "price": 100.00,
                            "costPrice": 60.00,
                            "sku": "%s",
                            "categoryId": %d,
                            "imageUrl": "https://example.com/foto.jpg"
                        }
                        """, sku, categoryId))
                .when()
                .post("/products")
                .then()
                .extract().path("id");
    }

    @Test
    void shouldGetStockByProductId() {
        given()
                .when()
                .get("/stock/product/" + productId)
                .then()
                .statusCode(200)
                .body("quantity", equalTo(0));
    }

    @Test
    void shouldAddQuantityToStock() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(Map.of("quantity", 10)) // O Jackson vai mapear certinho para o Record
                .when()
                .patch("/stock/product/" + productId + "/add")
                .then()
                .statusCode(200)
                .body("quantity", equalTo(10));
    }

    @Test
    void shouldReturn400WhenRemovingMoreThanAvailable() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(Map.of("quantity", 100))
                .when()
                .patch("/stock/product/" + productId + "/remove")
                .then()
                .statusCode(400)
                .body("error", equalTo("BUSINESS_ERROR"));
    }
}

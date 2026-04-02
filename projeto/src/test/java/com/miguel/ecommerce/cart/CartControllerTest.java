package com.miguel.ecommerce.cart;
import com.miguel.ecommerce.AbstractIntegrationTest;
import com.miguel.ecommerce.TestUtils;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
public class CartControllerTest extends AbstractIntegrationTest {
    private String clientToken;
    private String adminToken;
    private Integer productId;

    @BeforeEach
    void setup() {
        // 1. Criar usuários dinâmicos
        adminToken = createUserAndGetToken(TestUtils.generateRandomEmail(), "admin123", "ADMIN", TestUtils.generateRandomCpf());
        clientToken = createUserAndGetToken(TestUtils.generateRandomEmail(), "senha123", "CLIENT", TestUtils.generateRandomCpf());

        // 2. Criar Categoria
        String catName = "Cat-Cart-" + UUID.randomUUID().toString().substring(0, 5);
        Integer categoryId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(Map.of("name", catName))
                .when()
                .post("/categories")
                .then()
                .extract().path("id");

        // 3. Criar Produto e capturar ID
        String sku = "SKU-CART-" + UUID.randomUUID().toString().substring(0, 8);
        productId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(Map.of(
                        "name", "Produto Cart",
                        "price", 100.00,
                        "costPrice", 50.00,
                        "sku", sku,
                        "categoryId", categoryId,
                        "imageUrl", "http://example.com/imagem.jpg"
                ))
                .when()
                .post("/products")
                .then()
                .extract().path("id");

        // 4. Alimentar o estoque para permitir a venda
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(Map.of("quantity", 100))
                .when()
                .patch("/stock/product/" + productId + "/add");
    }

    @Test
    void shouldAddItemToCartSuccessfully() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + clientToken)
                .body(Map.of(
                        "productId", productId,
                        "quantity", 2
                ))
                .when()
                .post("/cart/items")
                .then()
                .statusCode(201)
                .body("items", hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    void shouldGetEmptyCartSuccessfully() {
        given()
                .header("Authorization", "Bearer " + clientToken)
                .when()
                .get("/cart")
                .then()
                .statusCode(200)
                .body("items", empty());
    }

    @Test
    void shouldReturn400WhenInsufficientStock() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + clientToken)
                .body(Map.of(
                        "productId", productId,
                        "quantity", 999
                ))
                .when()
                .post("/cart/items")
                .then()
                .statusCode(400)
                .body("error", equalTo("BUSINESS_ERROR"));
    }
}

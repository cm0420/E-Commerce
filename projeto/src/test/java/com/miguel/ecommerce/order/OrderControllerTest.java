package com.miguel.ecommerce.order;

import com.miguel.ecommerce.AbstractIntegrationTest;
import com.miguel.ecommerce.TestUtils;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderControllerTest extends AbstractIntegrationTest {
    private String clientToken;
    private String adminToken;
    private Integer addressId;

    @BeforeEach
    void setup() {
        // 1. Criar Admin dinâmico
        adminToken = createUserAndGetToken(
                TestUtils.generateRandomEmail(), "admin123", "ADMIN", TestUtils.generateRandomCpf()
        );

        // 2. Criar Cliente dinâmico e CAPTURAR O ID (para o endereço não falhar)
        String clientEmail = TestUtils.generateRandomEmail();
        String clientPass = "senha123";

        var clientResponse = given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "firstName", "Miguel",
                        "lastName", "Costa",
                        "email", clientEmail,
                        "password", clientPass,
                        "phoneNumber", "38999999999",
                        "cpf", TestUtils.generateRandomCpf(),
                        "role", "CLIENT"
                ))
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract();

        Integer clientUserId = clientResponse.path("id");

        // Pegar o token do cliente
        clientToken = given()
                .contentType(ContentType.JSON)
                .body(Map.of("email", clientEmail, "password", clientPass))
                .when()
                .post("/auth/login")
                .then()
                .extract().path("token");

        // 3. Criar Endereço usando o ID real do usuário
        addressId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + clientToken)
                .body(Map.of(
                        "street", "Rua das Flores",
                        "number", "123",
                        "district", "Centro",
                        "city", "Diamantina",
                        "state", "MG",
                        "zipCode", "39100000",
                        "userId", clientUserId // ID DINÂMICO
                ))
                .when()
                .post("/addresses")
                .then()
                .statusCode(201)
                .extract().path("id");

        // 4. Criar Categoria e Produto
        String catName = "Cat-" + UUID.randomUUID().toString().substring(0, 5);
        Integer categoryId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(Map.of("name", catName))
                .when()
                .post("/categories")
                .then()
                .extract().path("id");

        String sku = "SKU-" + UUID.randomUUID().toString().substring(0, 8);
        Integer productId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(Map.of(
                        "name", "Produto Order",
                        "price", 299.90,
                        "costPrice", 150.0,
                        "sku", sku,
                        "categoryId", categoryId,
                        "imageUrl", "http://image.com"
                ))
                .when()
                .post("/products")
                .then()
                .extract().path("id");

        // 5. Adicionar estoque e item no carrinho
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(Map.of("quantity", 10))
                .when()
                .patch("/stock/product/" + productId + "/add");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + clientToken)
                .body(Map.of("productId", productId, "quantity", 2))
                .when()
                .post("/cart/items");
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + clientToken)
                .body(Map.of("addressId", addressId))
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .body("status", equalTo("PENDING"))
                .body("shippingCost", notNullValue());
    }

    @Test
    void shouldCancelOrderSuccessfully() {
        Integer orderId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + clientToken)
                .body(Map.of("addressId", addressId))
                .when()
                .post("/orders")
                .then()
                .extract().path("id");

        given()
                .header("Authorization", "Bearer " + clientToken)
                .when()
                .patch("/orders/" + orderId + "/cancel")
                .then()
                .statusCode(200)
                .body("status", equalTo("CANCELLED"));
    }

    @Test
    void shouldReturn400WhenCartIsEmpty() {
        String emptyToken = createUserAndGetToken(
                TestUtils.generateRandomEmail(), "senha123", "CLIENT", TestUtils.generateRandomCpf()
        );

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + emptyToken)
                .body(Map.of("addressId", addressId))
                .when()
                .post("/orders")
                .then()
                .statusCode(400)
                .body("error", equalTo("BUSINESS_ERROR"));
    }
}

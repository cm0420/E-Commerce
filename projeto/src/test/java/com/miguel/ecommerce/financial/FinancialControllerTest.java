package com.miguel.ecommerce.financial;
import com.miguel.ecommerce.AbstractIntegrationTest;
import com.miguel.ecommerce.TestUtils;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
public class FinancialControllerTest extends AbstractIntegrationTest {
    private String adminToken;

    @BeforeEach
    void setup() {
        // Criando Admin com dados dinâmicos para evitar erros de duplicidade no banco do container
        adminToken = createUserAndGetToken(
                TestUtils.generateRandomEmail(),
                "admin123",
                "ADMIN",
                TestUtils.generateRandomCpf()
        );
    }

    @Test
    void shouldGetFinancialReportSuccessfully() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                // Usando queryParam para enviar as datas de filtro
                .queryParam("start", "2024-01-01T00:00:00")
                .queryParam("end", "2026-12-31T23:59:59")
                .when()
                .get("/financial/report")
                .then()
                .statusCode(200)
                .body("totalRevenue", notNullValue())
                .body("totalCost", notNullValue())
                .body("grossProfit", notNullValue());
    }

    @Test
    void shouldReturn403WhenClientAccessesFinancialReport() {
        // Cliente comum não deve ter acesso a dados financeiros da empresa
        String clientToken = createUserAndGetToken(
                TestUtils.generateRandomEmail(),
                "senha123",
                "CLIENT",
                TestUtils.generateRandomCpf()
        );

        given()
                .header("Authorization", "Bearer " + clientToken)
                .queryParam("start", "2024-01-01T00:00:00")
                .queryParam("end", "2026-12-31T23:59:59")
                .when()
                .get("/financial/report")
                .then()
                .statusCode(403);
    }
}

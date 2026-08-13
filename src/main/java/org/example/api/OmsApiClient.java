package org.example.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.model.LoginRequest;
import org.example.model.OrderRequest;

import static io.restassured.RestAssured.given;

public class OmsApiClient {

    private final String baseUrl;

    public OmsApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Response login(String email, String password) {
        return given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(new LoginRequest(email, password))
                .post("/api/login");
    }

    public Response createOrder(String token, OrderRequest orderRequest) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .post("/api/orders");
    }

    public Response processPayment(String token, String orderId, String cardNumber) {
        String body = """
                {
                  "orderId": "%s",
                  "cardNumber": "%s"
                }
                """.formatted(orderId, cardNumber);

        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(body)
                .post("/api/payments");
    }

    public Response getOrder(String token, String orderId) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .get("/api/orders/" + orderId);
    }
}

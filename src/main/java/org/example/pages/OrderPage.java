package org.example.pages;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.models.OrderRequest;
import org.example.models.OrderResponse;

import static io.restassured.RestAssured.given;

public class OrderPage {

    private static final String BASE_URL = "https://fakestoreapi.com";

    public Response createOrder(String body) {
        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .post("/carts");
    }

    public Response createOrder(OrderRequest request) {
        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/carts");
    }

    public Response getOrder(String id) {
        return given().baseUri(BASE_URL).get("/carts/" + id);
    }

    public OrderResponse getOrderAsModel(String id) {
        return getOrder(id).as(OrderResponse.class);
    }

    public Response getOrdersByUser(String userId) {
        return given().baseUri(BASE_URL).get("/carts/user/" + userId);
    }

    public Response updateOrder(String id, String body) {
        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .put("/carts/" + id);
    }

    public Response deleteOrder(String id) {
        return given().baseUri(BASE_URL).delete("/carts/" + id);
    }
}

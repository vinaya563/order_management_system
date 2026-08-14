package org.example.tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class OrderApiTest {

    private static final String BASE_URL = "https://fakestoreapi.com";

    @Test
    public void creatingOrderReturnsCreatedOrder() {
        String body = """
                {
                  "userId": 1,
                  "date": "2026-08-14",
                  "products": [{"productId": 1, "quantity": 2}]
                }
                """;

        Response response = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .post("/carts");

        Assert.assertEquals(response.statusCode(), 201);
        Assert.assertNotNull(response.jsonPath().getString("id"));
        Assert.assertEquals(response.jsonPath().getInt("products[0].productId"), 1);
        Assert.assertEquals(response.jsonPath().getInt("products[0].quantity"), 2);
    }

    @Test
    public void fetchingExistingOrderReturnsSameData() {
        Response response = given()
                .baseUri(BASE_URL)
                .get("/carts/1");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("id"), 1);
    }

    @Test
    public void fetchingNonexistentOrderReturnsEmptyBody() {
        Response response = given()
                .baseUri(BASE_URL)
                .get("/carts/99999");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.getBody().asString().trim(), "null");
    }

    @Test
    public void creatingOrderWithMissingProductsIsStillCreated() {
        String body = """
                {
                  "userId": 1,
                  "date": "2026-08-14"
                }
                """;

        Response response = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .post("/carts");

        Assert.assertEquals(response.statusCode(), 201);
        Assert.assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    public void updatingOrderReturnsUpdatedData() {
        String body = """
                {
                  "userId": 1,
                  "date": "2026-08-14",
                  "products": [{"productId": 2, "quantity": 1}]
                }
                """;

        Response response = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .put("/carts/1");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("products[0].productId"), 2);
    }

    @Test
    public void deletingOrderReturnsDeletedOrder() {
        Response response = given()
                .baseUri(BASE_URL)
                .delete("/carts/1");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("id"), 1);
    }

    @Test
    public void fetchingOrdersByUserReturnsList() {
        Response response = given()
                .baseUri(BASE_URL)
                .get("/carts/user/1");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("$").isEmpty());
    }
}

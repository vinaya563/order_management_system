package org.example.tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ProductApiTest {

    private static final String BASE_URL = "https://fakestoreapi.com";

    @Test
    public void fetchingProductByIdReturnsProduct() {
        Response response = given()
                .baseUri(BASE_URL)
                .get("/products/1");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getString("title"));
        Assert.assertTrue(response.jsonPath().getDouble("price") > 0);
    }

    @Test
    public void fetchingNonexistentProductReturnsEmptyBody() {
        Response response = given()
                .baseUri(BASE_URL)
                .get("/products/99999");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(response.getBody().asString().isBlank());
    }

    @Test
    public void fetchingProductWithNonNumericIdReturnsEmptyBody() {
        Response response = given()
                .baseUri(BASE_URL)
                .get("/products/abc");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(response.getBody().asString().isBlank());
    }

    @Test
    public void fetchingProductWithNegativeIdReturnsEmptyBody() {
        Response response = given()
                .baseUri(BASE_URL)
                .get("/products/-1");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(response.getBody().asString().isBlank());
    }

    @Test
    public void fetchingAllProductsReturnsNonEmptyList() {
        Response response = given()
                .baseUri(BASE_URL)
                .get("/products");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("$").isEmpty());
    }

    @Test
    public void fetchingCategoriesReturnsKnownCategory() {
        Response response = given()
                .baseUri(BASE_URL)
                .get("/products/categories");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(response.jsonPath().getList("$", String.class).contains("electronics"));
    }
}

package org.example.pages;

import io.restassured.response.Response;
import org.example.models.ProductResponse;

import static io.restassured.RestAssured.given;

public class ProductPage {

    private static final String BASE_URL = "https://fakestoreapi.com";

    public Response getProduct(String id) {
        return given().baseUri(BASE_URL).get("/products/" + id);
    }

    public ProductResponse getProductAsModel(String id) {
        return getProduct(id).as(ProductResponse.class);
    }

    public Response getAllProducts() {
        return given().baseUri(BASE_URL).get("/products");
    }

    public Response getCategories() {
        return given().baseUri(BASE_URL).get("/products/categories");
    }
}

package org.example.tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class AuthApiTest {

    private static final String BASE_URL = "https://fakestoreapi.com";

    @Test
    public void validLoginReturnsToken() {
        Response response = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body("{\"username\":\"mor_2314\",\"password\":\"83r5^_\"}")
                .post("/auth/login");

        Assert.assertEquals(response.statusCode(), 201);
        Assert.assertNotNull(response.jsonPath().getString("token"));
    }

    @Test
    public void invalidPasswordIsRejected() {
        Response response = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body("{\"username\":\"mor_2314\",\"password\":\"wrongpass\"}")
                .post("/auth/login");

        Assert.assertEquals(response.statusCode(), 401);
    }

    @Test
    public void missingPasswordIsRejected() {
        Response response = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body("{\"username\":\"mor_2314\"}")
                .post("/auth/login");

        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    public void emptyBodyIsRejected() {
        Response response = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body("{}")
                .post("/auth/login");

        Assert.assertEquals(response.statusCode(), 400);
    }
}

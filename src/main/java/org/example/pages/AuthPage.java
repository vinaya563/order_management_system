package org.example.pages;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.models.LoginRequest;

import static io.restassured.RestAssured.given;

public class AuthPage {

    private static final String BASE_URL = "https://fakestoreapi.com";

    public Response login(String username, String password) {
        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
                .post("/auth/login");
    }

    public Response login(LoginRequest request) {
        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/auth/login");
    }
}

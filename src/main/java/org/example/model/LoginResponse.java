package org.example.model;

public record LoginResponse(
        String token,
        String userId
) {
}

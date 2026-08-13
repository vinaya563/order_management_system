package org.example.model;

public record LoginRequest(
        String email,
        String password
) {
}

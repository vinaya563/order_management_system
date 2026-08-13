package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class TestConfig {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = TestConfig.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new IllegalStateException("config.properties was not found");
            }

            PROPERTIES.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Unable to load test configuration", e);
        }
    }

    private TestConfig() {
    }

    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing configuration: " + key);
        }

        return value;
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static String validEmail() {
        return get("valid.email");
    }

    public static String validPassword() {
        return get("valid.password");
    }

    public static String invalidPassword() {
        return get("invalid.password");
    }

    public static String successfulCard() {
        return get("successful.card");
    }

    public static String failureCard() {
        return get("failure.card");
    }
}

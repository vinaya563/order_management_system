package org.example.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.example.config.TestConfig;
import org.example.db.OrderRepository;
import org.example.model.OrderRequest;
import org.example.model.OrderResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MockOmsServer {

    private final HttpServer server;
    private final ObjectMapper mapper = new ObjectMapper();
    private final OrderRepository repository = new OrderRepository();
    private final Set<String> tokens = ConcurrentHashMap.newKeySet();
    private final AtomicInteger orderSequence = new AtomicInteger();

    public MockOmsServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/", this::handleRequest);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    public void clear() {
        repository.clear();
        tokens.clear();
        orderSequence.set(0);
    }

    public String getBaseUrl() {
        return "http://localhost:" + server.getAddress().getPort();
    }

    public OrderRepository getRepository() {
        return repository;
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (method.equals("GET") && path.equals("/")) {
            serveStatic(exchange, "/web/index.html", "text/html");
            return;
        }
        if (method.equals("GET") && path.equals("/app.js")) {
            serveStatic(exchange, "/web/app.js", "application/javascript");
            return;
        }
        if (method.equals("POST") && path.equals("/api/login")) {
            login(exchange);
            return;
        }
        if (method.equals("POST") && path.equals("/api/orders")) {
            createOrder(exchange);
            return;
        }
        if (method.equals("POST") && path.equals("/api/payments")) {
            processPayment(exchange);
            return;
        }
        if (method.equals("GET") && path.startsWith("/api/orders/")) {
            getOrder(exchange, path.substring("/api/orders/".length()));
            return;
        }

        writeJson(exchange, 404, Map.of("message", "Endpoint not found"));
    }

    private void login(HttpExchange exchange) throws IOException {
        JsonNode body = readJson(exchange);
        String email = body.get("email").asText();
        String password = body.get("password").asText();

        if (!email.equals(TestConfig.validEmail()) || !password.equals(TestConfig.validPassword())) {
            writeJson(exchange, 401, Map.of("message", "Invalid credentials"));
            return;
        }

        String token = "token-" + UUID.randomUUID();
        tokens.add(token);

        writeJson(exchange, 200, Map.of("token", token));
    }

    private void createOrder(HttpExchange exchange) throws IOException {
        if (!isAuthenticated(exchange)) {
            writeJson(exchange, 401, Map.of("message", "Unauthorized"));
            return;
        }

        OrderRequest request = mapper.readValue(exchange.getRequestBody(), OrderRequest.class);

        if (request.productName() == null || request.productName().isBlank()) {
            writeJson(exchange, 400, Map.of("message", "Product name is required"));
            return;
        }
        if (request.quantity() <= 0) {
            writeJson(exchange, 400, Map.of("message", "Quantity must be positive"));
            return;
        }

        String orderId = "ORD-" + orderSequence.incrementAndGet();

        OrderResponse order = new OrderResponse(
                orderId,
                request.productName(),
                request.quantity(),
                request.unitPrice(),
                "CREATED",
                "PENDING"
        );

        repository.save(order);
        writeJson(exchange, 201, order);
    }

    private void processPayment(HttpExchange exchange) throws IOException {
        if (!isAuthenticated(exchange)) {
            writeJson(exchange, 401, Map.of("message", "Unauthorized"));
            return;
        }

        JsonNode body = readJson(exchange);
        String orderId = body.get("orderId").asText();
        String cardNumber = body.get("cardNumber").asText();

        OrderResponse order = repository.findById(orderId).orElse(null);
        if (order == null) {
            writeJson(exchange, 404, Map.of("message", "Order not found"));
            return;
        }

        if ("SUCCESS".equals(order.paymentStatus())) {
            writeJson(exchange, 409, Map.of("message", "Order already paid"));
            return;
        }

        if (cardNumber.equals(TestConfig.failureCard())) {
            repository.save(order.withStatus("PAYMENT_FAILED", "FAILED"));
            writeJson(exchange, 402, Map.of("orderId", orderId, "paymentStatus", "FAILED", "message", "Payment failed"));
            return;
        }

        repository.save(order.withStatus("CONFIRMED", "SUCCESS"));
        writeJson(exchange, 200, Map.of("orderId", orderId, "paymentStatus", "SUCCESS", "message", "Payment successful"));
    }

    private void getOrder(HttpExchange exchange, String orderId) throws IOException {
        if (!isAuthenticated(exchange)) {
            writeJson(exchange, 401, Map.of("message", "Unauthorized"));
            return;
        }

        OrderResponse order = repository.findById(orderId).orElse(null);
        if (order == null) {
            writeJson(exchange, 404, Map.of("message", "Order not found"));
            return;
        }

        writeJson(exchange, 200, order);
    }

    private boolean isAuthenticated(HttpExchange exchange) {
        String authorization = exchange.getRequestHeaders().getFirst("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return false;
        }
        return tokens.contains(authorization.substring("Bearer ".length()));
    }

    private JsonNode readJson(HttpExchange exchange) throws IOException {
        return mapper.readTree(exchange.getRequestBody());
    }

    private void serveStatic(HttpExchange exchange, String resource, String contentType) throws IOException {
        try (InputStream input = getClass().getResourceAsStream(resource)) {
            if (input == null) {
                writeJson(exchange, 404, Map.of("message", "Resource not found"));
                return;
            }

            byte[] content = input.readAllBytes();
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, content.length);

            try (OutputStream output = exchange.getResponseBody()) {
                output.write(content);
            }
        }
    }

    private void writeJson(HttpExchange exchange, int status, Object body) throws IOException {
        byte[] content = mapper.writeValueAsBytes(body);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, content.length);

        try (OutputStream output = exchange.getResponseBody()) {
            output.write(content);
        }
    }
}

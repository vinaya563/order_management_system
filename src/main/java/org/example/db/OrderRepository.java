package org.example.db;

import org.example.model.OrderResponse;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class OrderRepository {

    private final Map<String, OrderResponse> orders =
            new ConcurrentHashMap<>();

    public void save(OrderResponse order) {
        orders.put(order.id(), order);
    }

    public Optional<OrderResponse> findById(String orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    public void clear() {
        orders.clear();
    }
}

package org.example.db;

import java.util.HashMap;
import java.util.Map;

public class OrderDatabase {

    private final Map<Integer, String> savedOrders = new HashMap<>();

    public void save(int orderId, String orderData) {
        savedOrders.put(orderId, orderData);
    }

    public String find(int orderId) {
        return savedOrders.get(orderId);
    }
}

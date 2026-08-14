package org.example.db;

public class DatabaseValidator {

    private final OrderDatabase database = new OrderDatabase();

    public void recordOrder(int orderId, String orderData) {
        database.save(orderId, orderData);
    }

    public boolean isOrderPersisted(int orderId) {
        return database.find(orderId) != null;
    }

    public boolean orderDataMatches(int orderId, String expectedData) {
        String stored = database.find(orderId);
        return stored != null && stored.equals(expectedData);
    }
}

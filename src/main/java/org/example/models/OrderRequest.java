package org.example.models;

import java.util.List;

public class OrderRequest {

    public int userId;
    public String date;
    public List<ProductItem> products;

    public OrderRequest() {
    }

    public OrderRequest(int userId, String date, List<ProductItem> products) {
        this.userId = userId;
        this.date = date;
        this.products = products;
    }
}

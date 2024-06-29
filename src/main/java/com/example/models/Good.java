package com.example.models;

import java.math.BigDecimal;

public class Good {
    private String product_name;
    private String description;
    private String producer;
    private int amount;
    private BigDecimal price;
    private String group_name;

    public Good() {
    }

    public Good(String product_name, String description, String producer, int amount, BigDecimal price, String group_name) {
        this.product_name = product_name;
        this.description = description;
        this.producer = producer;
        this.amount = amount;
        this.price = price;
        this.group_name = group_name;
    }

    public String getProduct_name() {
        return product_name;
    }
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getProducer() {
        return producer;
    }
    public void setProducer(String producer) {
        this.producer = producer;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public String getGroup_name() {
        return group_name;
    }
    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    @Override
    public String toString() {
        return  "Product Name: " + product_name + "\n" +
                "Description: " + description + "\n" +
                "Producer: " + producer + "\n" +
                "Amount: " + amount + "\n" +
                "Price: " + String.format("%.2f", price) + "\n" +  // Format price to 2 decimal places
                "Group Name: " + group_name;
    }
}


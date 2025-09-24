package com.expenseTracker.model;

public class Expense {
    private int id;
    private String description;
    private double amount;
    private String date;
    private int categoryId;

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    

}

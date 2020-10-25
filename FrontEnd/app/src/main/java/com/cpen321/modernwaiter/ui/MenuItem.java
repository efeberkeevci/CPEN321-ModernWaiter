package com.cpen321.modernwaiter.ui;

public class MenuItem {
    public String name;
    public String description;
    public String quantity;
    public final int id;
    public final double price;

    public MenuItem(String name, String description, String quantity, String id, double price) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = Double.valueOf(price);
        this.id = Integer.valueOf(id);
    }

    public void incrementQuantity() {
        quantity = String.valueOf(Integer.parseInt(quantity) + 1);
    }

    public void decrementQuantity() {
        if (!"0".equals(quantity))
            quantity = String.valueOf(Integer.parseInt(quantity) - 1);
    }

    @Override
    public int hashCode() {
        return id;
    }

}

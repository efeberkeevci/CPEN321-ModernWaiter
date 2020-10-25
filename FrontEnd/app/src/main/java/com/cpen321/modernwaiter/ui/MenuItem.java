package com.cpen321.modernwaiter.ui;

public class MenuItem {
    public String name;
    public String description;
    public String quantity;
    public final int id;

    public MenuItem(String name, String description, String quantity, String id) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.id = Integer.valueOf(id);
    }

    public void incrementQuantity() {
        quantity = String.valueOf(Integer.parseInt(quantity) + 1);
    }

    public void decrementQuantity() {
        if (!"0".equals(quantity))
            quantity = String.valueOf(Integer.parseInt(quantity) - 1);
    }

}

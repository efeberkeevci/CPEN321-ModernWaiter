package com.cpen321.modernwaiter.ui;

import java.text.DecimalFormat;

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

    public String getPriceString() {
        return "$" + new DecimalFormat("#.##")
                .format(price);
    }

    public String getTotalPriceString() {
        return "$" + new DecimalFormat("#.##")
                .format(price * Integer.parseInt(quantity));
    }

    @Override
    public int hashCode() {
        return id;
    }

}

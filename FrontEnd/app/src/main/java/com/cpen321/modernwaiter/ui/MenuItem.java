package com.cpen321.modernwaiter.ui;

import java.text.DecimalFormat;

public class MenuItem {
    public final int id;
    public final int restaurant_id;
    public final String name;
    public final String type;
    public final double cost;
    public final String description;
    public final int calories;
    public final int popularity_count;
    public final String image;
    public String quantity;
    public boolean recommended;

    public MenuItem(String id, String restaurant_id, String name, String type, String cost, String description, String calories, String popularity_count, String image) {

        this.id = Integer.valueOf(id);
        this.restaurant_id = Integer.valueOf(restaurant_id);
        this.name = name;
        this.type = type;
        this.cost = Integer.valueOf(cost);
        this.description = description;
        this.calories = Integer.valueOf(calories);
        this.popularity_count = Integer.valueOf(popularity_count);
        this.image = image;
        //this.quantity = quantity;
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
                .format(cost);
    }

    public String getTotalPriceString() {
        return "$" + new DecimalFormat("#.##")
                .format(cost * Integer.parseInt(quantity));
    }

    public int getIntegerQuantity() {
        return Integer.parseInt(quantity);
    }

    @Override
    public int hashCode() {
        return id;
    }

}

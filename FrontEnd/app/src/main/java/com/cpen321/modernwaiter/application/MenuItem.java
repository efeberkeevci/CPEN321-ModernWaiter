package com.cpen321.modernwaiter.application;

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

    public MenuItem(String id, String restaurant_id, String name, String type, String cost, String description, String calories, String popularity_count, String image) {

        this.id = Integer.parseInt(id);
        this.restaurant_id = Integer.parseInt(restaurant_id);
        this.name = name;
        this.type = type;
        this.cost = Integer.parseInt(cost);
        this.description = description;
        this.calories = Integer.parseInt(calories);
        this.popularity_count = Integer.parseInt(popularity_count);
        this.image = image;
    }

    public MenuItem(int id) {
        this.id = id;
        restaurant_id = -1;
        name = "dummy";
        type = "dummy";
        cost = -1;
        description = "dummy";
        calories = -1;
        popularity_count = -1;
        image = "dummy";
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

    public String getTotalCartPriceString() {
        return "$" + new DecimalFormat("#.##")
                .format(cost * Integer.parseInt(quantity));
    }

    public String getTotalBillPriceString(int count) {
        return "$" + new DecimalFormat("#.##")
                .format(cost * count);
    }

    public int getIntegerQuantity() {
        return Integer.parseInt(quantity);
    }

    public int getCost() {
        return (int) Math.round(cost * 100);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MenuItem)
            return ((MenuItem) o).id == id;
        return false;
    }

}

package com.cpen321.modernwaiter.application;

import android.graphics.Bitmap;

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
    public Bitmap imageBitmap;

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

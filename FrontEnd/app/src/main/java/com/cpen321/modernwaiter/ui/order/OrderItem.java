package com.cpen321.modernwaiter.ui.order;

import com.cpen321.modernwaiter.application.MenuItem;

public class OrderItem {
    public MenuItem menuItem;
    public boolean selected;

    public OrderItem(MenuItem menuItem, boolean selected) {
        this.menuItem = menuItem;
        this.selected = selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

package com.cpen321.modernwaiter.ui.payment.peritem;

import com.cpen321.modernwaiter.application.MenuItem;

public class PaymentItem {
    public MenuItem menuItem;
    public boolean selected;

    public PaymentItem(MenuItem menuItem, boolean selected) {
        this.menuItem = menuItem;
        this.selected = selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

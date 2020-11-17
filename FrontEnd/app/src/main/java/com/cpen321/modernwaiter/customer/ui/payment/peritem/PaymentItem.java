package com.cpen321.modernwaiter.customer.ui.payment.peritem;

import com.cpen321.modernwaiter.customer.application.MenuItem;

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

package com.cpen321.modernwaiter.customer.ui.payment.peritem;

import com.cpen321.modernwaiter.customer.application.MenuItem;

public class PaymentItem implements Comparable<PaymentItem> {
    public MenuItem menuItem;
    public boolean selected;

    public PaymentItem(MenuItem menuItem, boolean selected) {
        this.menuItem = menuItem;
        this.selected = selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int compareTo(PaymentItem o) {
        if (!menuItem.name.equals(o.menuItem.name)) {
            return menuItem.name.compareTo(o.menuItem.name);
        }
        if (selected == o.selected) {
            return 0;
        }
        if (selected) {
            return -1;
        }
        return 1;
    }
}

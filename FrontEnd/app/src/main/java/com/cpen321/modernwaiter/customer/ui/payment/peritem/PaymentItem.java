package com.cpen321.modernwaiter.customer.ui.payment.peritem;

import com.cpen321.modernwaiter.customer.application.MenuItem;

import static com.cpen321.modernwaiter.customer.application.CustomerActivity.tableSession;

public class PaymentItem implements Comparable<PaymentItem> {
    public final MenuItem menuItem;
    public boolean selected;
    public final int userId;

    public PaymentItem(MenuItem menuItem, boolean selected, int userId) {
        this.menuItem = menuItem;
        this.selected = selected;
        this.userId = userId;
    }

    public String getUsername() {
        return tableSession.getUsernameFromId(userId);
    }

    public boolean selectedByCurrentUser() {
        return tableSession.getUserId() == userId;
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
            return getUsername().compareTo(o.getUsername());
        }
        if (selected) {
            return -1;
        }
        return 1;
    }
}

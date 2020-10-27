package com.cpen321.modernwaiter;

import com.cpen321.modernwaiter.ui.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<MenuItem> ITEMS = new ArrayList<MenuItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Integer, MenuItem> ITEM_MAP = new HashMap<Integer, MenuItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(MenuItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static MenuItem createDummyItem(int position) {

        Random r = new Random();
        double cost = 1 + (20) * r.nextDouble();
        //(String id, String restaurant_id, String name, String type, String cost, String description, String calories, String popularity_count, String image)
        MenuItem menuItem = new MenuItem(String.valueOf(position), "1", "item " + position, "tofu", String.valueOf((int) cost * 100), "Lorem ipsum", "600", "1", "iamge");
        menuItem.quantity = "0";
        return menuItem;
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
}
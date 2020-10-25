package com.cpen321.modernwaiter;

import java.util.ArrayList;

public class Bill {

    public ArrayList<Bill_item> bill_list;

    public class Bill_item {
        public int item_number;
        public String item_name;
        public double item_price;

        public Bill_item(){
            item_number=0;
            item_name="";
            item_price=0;
        }
        public Bill_item(int no, String name, double price){
            item_number = no;
            item_name = name;
            item_price = price;
        }
        @Override
        public String toString() {
            return item_name;
        }

    }

    public Bill(){
        bill_list = new ArrayList<Bill_item>();
    }

    public ArrayList<Bill_item> Bill_add_item(Bill_item item){
        bill_list.add(item);
        return bill_list;
    }

    public ArrayList<Bill_item> Bill_add_item(int item_number, String item_name, double item_price){
        bill_list.add(new Bill_item(item_number, item_name, item_price));
        return bill_list;
    }

    public ArrayList<Bill_item> Bill_remove_item(Bill_item item){
        bill_list.remove(item);
        return bill_list;
    }

    public ArrayList<Bill_item> getOrderBill(){
        return bill_list;
    }

}

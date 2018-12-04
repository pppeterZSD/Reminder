package com.example.zhu.test4;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class Shop {
    /**
     * This is just like a collection of all items
     */
    private static final String STORAGE = "shop";


    public  List<Item> list= new ArrayList<>();

    public static Shop get() {

        return new Shop();
    }

    public List<Item> addItem(Item item){

        this.list.add(item);
        return this.list;
    }

    private SharedPreferences storage;

    private Shop() {
        storage = App.getInstance().getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
    }

    public List<Item> getData() {
                list.addAll(Arrays.asList(
                        new Item(10001,"Custom_name","11-11", "android.resource://com.example.zhu.test4/drawable/third_1"),
                        new Item(10002,"ycy", "10-17" ,"android.resource://com.example.zhu.test4/drawable/ycy"),
                        new Item(10006,"yly","03-11", "android.resource://com.example.zhu.test4/drawable/dream"),
                        new Item(10003,"md","07-09",  "android.resource://com.example.zhu.test4/drawable/aa"),
                        new Item(10005,"yyh", "08-25", "android.resource://com.example.zhu.test4/drawable/exercise_1"),
                        new Item(10007,"exhausted", "11-29", "android.resource://com.example.zhu.test4/drawable/jobs")));
        Collections.sort(list);
        return list;
    }

    public boolean isRated(String uriId) {
        return storage.getBoolean(uriId, false);
    }

    public void setRated(String uriId, boolean isRated) {
        storage.edit().putBoolean(uriId, isRated).apply();
    }


}

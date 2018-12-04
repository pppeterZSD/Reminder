package com.example.zhu.test4;

import java.io.Serializable;

/**
 * I create my own Item here
 * Later I need to send my item from the main activity to the detail activity
 * so I need to make my item serializable
 * @author zhusd
 * @version 1.0
 */
public class Item implements Serializable, Comparable<Item>  {


        public int id;
        public  String name;
        public  String date;
        public  String uri;

        public Item(int id, String name, String date, String uri) {
            this.id = id;
            this.name = name;
            this.date = date;
            this.uri = uri;
        }


        public void setId(int id) {
            this.id=id;
        }
        public void setName(String s) {
            this.name = s;
        }
        public void setDate(String s) {
        this.date = s;
    }
        public void setUri(String newUri) {
        this.uri = newUri;
    }



    public String getUri() {
        return uri;
    }
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public String getDate() {
        return date;
    }

    /**
     * @param newItem
     * @return I need to sort all items, this is a measure of each item date
     */
    @Override
    public int compareTo(Item newItem) {
            int sum=0;
            String[] s1 = this.getDate().split("-");
            String[] s2 = newItem.getDate().split("-");
            for(int i=0;i<2;i++){
                sum=sum*100+Integer.parseInt(s1[i])-Integer.parseInt(s2[i]);
            }

        return sum;
    }

    }
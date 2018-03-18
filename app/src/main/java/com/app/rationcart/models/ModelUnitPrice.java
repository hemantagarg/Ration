package com.app.rationcart.models;

/**
 * Created by hemanta on 17-03-2018.
 */

public class ModelUnitPrice {
    private String id;

    private String unit;

    private String unitprice;

    private String price;

    private String dis_price;

    private int cart_count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDis_price() {
        return dis_price;
    }

    public void setDis_price(String dis_price) {
        this.dis_price = dis_price;
    }

    public int getCart_count() {
        return cart_count;
    }

    public void setCart_count(int cart_count) {
        this.cart_count = cart_count;
    }
}

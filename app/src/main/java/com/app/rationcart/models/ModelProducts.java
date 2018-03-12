package com.app.rationcart.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hemanta on 12-03-2018.
 */

public class ModelProducts {

    private String product_cart_count;

    private String unitType;

    private String discount_value;

    private String image;

    private String quantity;

    private String productPrice;

    private String productName;

    private String productId;
    private int rowType;
    private int sp_position = 0;


    public int getCustomPosition() {
        return customPosition;
    }

    public void setCustomPosition(int customPosition) {
        this.customPosition = customPosition;
    }

    int customPosition;

    public int getSp_position() {
        return sp_position;
    }

    public void setSp_position(int sp_position) {
        this.sp_position = sp_position;
    }


    public boolean isCustomoption() {
        return isCustomoption;
    }

    public void setIsCustomoption(boolean isCustomoption) {
        this.isCustomoption = isCustomoption;
    }


    boolean isCustomoption;
    ArrayList<HashMap<String, String>> setCustomOption;

    public ArrayList<HashMap<String, String>> getSetCustomOption() {
        return setCustomOption;
    }

    public void setSetCustomOption(ArrayList<HashMap<String, String>> setCustomOption) {
        this.setCustomOption = setCustomOption;
    }


    public String getProduct_cart_count() {
        return product_cart_count;
    }

    public void setProduct_cart_count(String product_cart_count) {
        this.product_cart_count = product_cart_count;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(String discount_value) {
        this.discount_value = discount_value;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }
}

package com.app.rationcart.models;

/**
 * Created by admin on 26-11-2015.
 */
public class ModelAddressDetail {
    private String name;
    private String address;
    private String landmark;
    private String zipcode;
    private String mobileno;
    private String city, email;
    private int rowType = 1;

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    private String address_id;
    private int selection_position;

    private int bubble_position;

    public ModelAddressDetail() {
    }

    public ModelAddressDetail(String name, String address, String landmark, String zipcode, String mobileno, String city) {
        this.name = name;
        this.landmark = landmark;
        this.address = address;
        this.mobileno = mobileno;
        this.zipcode = zipcode;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getBubble_position() {
        return bubble_position;
    }

    public void setBubble_position(int bubble_position) {
        this.bubble_position = bubble_position;
    }

    public int getSelection_position() {
        return selection_position;
    }

    public void setSelection_position(int selection_position) {
        this.selection_position = selection_position;
    }


    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

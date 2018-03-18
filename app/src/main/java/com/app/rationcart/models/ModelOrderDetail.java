package com.app.rationcart.models;

/**
 * Created by hemanta on 18-03-2018.
 */

public class ModelOrderDetail {

    private String total_amount;

    private String lname;

    private String email;

    private String address;

    private String status;

    private String quantity;

    private String fname;

    private String orderId,dataJson;

    private String mobile;
    private int rowType = 1;

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }

    private String unit_value;

    private String image;

    private String productName;

    private String productQuanitity;

    private String productPrice;

    private String productId;

    public String getUnit_value ()
    {
        return unit_value;
    }

    public void setUnit_value (String unit_value)
    {
        this.unit_value = unit_value;
    }

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public String getProductName ()
    {
        return productName;
    }

    public void setProductName (String productName)
    {
        this.productName = productName;
    }

    public String getProductQuanitity ()
    {
        return productQuanitity;
    }

    public void setProductQuanitity (String productQuanitity)
    {
        this.productQuanitity = productQuanitity;
    }

    public String getProductPrice ()
    {
        return productPrice;
    }

    public void setProductPrice (String productPrice)
    {
        this.productPrice = productPrice;
    }

    public String getProductId ()
    {
        return productId;
    }

    public void setProductId (String productId)
    {
        this.productId = productId;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }
}

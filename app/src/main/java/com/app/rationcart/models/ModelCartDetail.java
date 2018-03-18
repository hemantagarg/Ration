package com.app.rationcart.models;

/**
 * Created by admin on 18-12-2015.
 */
public class ModelCartDetail {

    String quote_id;
    String total_items;
    String total_quantity;
    String sub_total;
    String grand_total;
    String shipping_charges;


    public String getDeliverySlot() {
        return deliverySlot;
    }

    public void setDeliverySlot(String deliverySlot) {
        this.deliverySlot = deliverySlot;
    }

    public String getSelecteddate() {
        return selecteddate;
    }

    public void setSelecteddate(String selecteddate) {
        this.selecteddate = selecteddate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFromtime() {
        return fromtime;
    }

    public void setFromtime(String fromtime) {
        this.fromtime = fromtime;
    }

    public String getTotime() {
        return totime;
    }

    public void setTotime(String totime) {
        this.totime = totime;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public int getSelection_position() {
        return selection_position;
    }

    public void setSelection_position(int selection_position) {
        this.selection_position = selection_position;
    }

    String deliverySlot,selecteddate,date,fromtime,totime,checked,disabled,style,value,delivery_date;
    int selection_position;

    public ModelCartDetail() {
    }

    public ModelCartDetail(String quote_id, String grand_total, String shipping_charges,
                           String total_items, String total_quantity, String sub_total) {

        this.quote_id=quote_id;
        this.grand_total=grand_total;
        this.shipping_charges=shipping_charges;
        this.total_items=total_items;
        this.total_quantity=total_quantity;
        this.sub_total=sub_total;


    }


    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }

    public String getTotal_items() {
        return total_items;
    }

    public void setTotal_items(String total_items) {
        this.total_items = total_items;
    }

    public String getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(String quote_id) {
        this.quote_id = quote_id;
    }

    public String getShipping_charges() {
        return shipping_charges;
    }

    public void setShipping_charges(String shipping_charges) {
        this.shipping_charges = shipping_charges;
    }




}

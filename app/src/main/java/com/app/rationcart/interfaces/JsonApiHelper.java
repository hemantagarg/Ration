package com.app.rationcart.interfaces;

/**
 * Created by hemanta on 29-07-2017.
 */

public interface JsonApiHelper {

    // http://stackmindz.com/dev/rationcart/api/category.php

    String BASEURL = JsonApiHelper.TESTURL;
    String TESTURL = "http://stackmindz.com/dev/rationcart/api/";
    String LOGIN = "login?";
    String REGISTER = "register?";
    String FORGOT = "forget-password?";
    String CHANGE_PASSWORD = "change-password?";
    String CATEGORIES = "category.php?";
    String ADD_ADDRESS = "addAddress?";
    String UPDATE_PROFILE = "update-profile?";
    String CITY = "city";
    String GET_PROFILE = "getProfile?";
    String GET_PINCODE = "getpincode?";
    String GET_ADDRESS = "getAddress?";
    String GET_ORDERS = "orderhistory?";
    String NOTIFICATION_LIST = "notification-list.php?";
    String CATEGORY_PRODUCT = "categoryproduct.php?";
    String SEARCH = "search.php?";
    String SUBCATEGORY_PRODUCT = "subcategoryproduct.php?";
    String SUBTOSUBCATEGORY_PRODUCT = "subtosubcatproduct.php?";
    String CART_PRODUCT = "cartproduct.php?";
    String PLACE_ORDER = "placeorder?";
    String ADD_PRODUCT = "product_cart_count.php?";
    String DELETE_CART_ITEM = "deletecartitem?";
    String DELETE_ADDRESS = "deleteaddress?";
    String PRODUCT_DETAIL = "product-detail.php?";

    String SIGNUP = "";
}

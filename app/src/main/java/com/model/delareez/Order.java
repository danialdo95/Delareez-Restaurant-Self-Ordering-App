package com.model.delareez;

/**
 * Created by User on 26/9/2017.
 */

public class Order {

    String orderID;
    String orderDate;
    String orderOption;
    String orderStatus;
    String paymentStatus;
    Double totalPaymentPrice;
    Integer numberOfMenu;
    String cart;
    String menuID;
    String custID;
    String staffID;

    public Order() {

    }

    public Order(String orderID, String orderDate, String orderOption, String orderStatus, String paymentStatus, Double totalPaymentPrice, Integer numberOfMenu, String cart, String menuID, String custID, String staffID) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.orderOption = orderOption;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.totalPaymentPrice = totalPaymentPrice;
        this.numberOfMenu = numberOfMenu;
        this.cart = cart;
        this.menuID = menuID;
        this.custID = custID;
        this.staffID = staffID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderOption() {
        return orderOption;
    }

    public void setOrderOption(String orderOption) {
        this.orderOption = orderOption;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getTotalPaymentPrice() {
        return totalPaymentPrice;
    }

    public void setTotalPaymentPrice(Double totalPaymentPrice) {
        this.totalPaymentPrice = totalPaymentPrice;
    }

    public Integer getNumberOfMenu() {
        return numberOfMenu;
    }

    public void setNumberOfMenu(Integer numberOfMenu) {
        this.numberOfMenu = numberOfMenu;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

    public String getCustID() {
        return custID;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }
}
package com.system.gocery_final.Model;

public class AdminOrders {

    private String address, date, name, number, state, time, totalAmount, uid, orderid;

    public AdminOrders() {

    }

    public AdminOrders(String address, String date, String name, String number, String state, String time, String totalAmount, String uid, String orderid) {
        this.address = address;
        this.date = date;
        this.name = name;
        this.number = number;
        this.state = state;
        this.time = time;
        this.totalAmount = totalAmount;
        this.uid = uid;
        this.orderid = orderid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
}

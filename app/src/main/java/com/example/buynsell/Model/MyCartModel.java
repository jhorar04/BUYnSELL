package com.example.buynsell.Model;

public class MyCartModel {
    String productName,productPrice,currentDate,currentTime,totalQuantity,pid;

    public MyCartModel() {

    }

    public MyCartModel(String productName, String productPrice, String currentDate, String currentTime, String totalQuantity, String productID) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.totalQuantity = totalQuantity;
        this.pid = productID;
       // this.totalPrice=totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getProductID() {
        return pid;
    }

    public void setProductID(String productID) {
        this.pid = productID;
    }

//    public String getTotalPrice() {
//        return totalPrice;
//    }
//
//    public void setTotalPrice(String productID) {
//        this.totalPrice = totalPrice;
//    }
}

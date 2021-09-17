package com.shareware.saregama.Model;

public class SecondMobPurchase {
    private String buyerAddress, buyerName, color, companyName, mobileNo, mobilePrice, iemiNumber, modelName, sellerName, date, billdate;
    private String charger,headphone;
    public SecondMobPurchase() {
    }

    public SecondMobPurchase(String buyerAddress, String buyerName, String color, String companyName, String mobileNo, String mobilePrice, String iemiNumber, String modelName, String sellerName, String date, String billdate, String charger, String headphone) {
        this.buyerAddress = buyerAddress;
        this.buyerName = buyerName;
        this.color = color;
        this.companyName = companyName;
        this.mobileNo = mobileNo;
        this.mobilePrice = mobilePrice;
        this.iemiNumber = iemiNumber;
        this.modelName = modelName;
        this.sellerName = sellerName;
        this.date = date;
        this.billdate = billdate;
        this.charger = charger;
        this.headphone = headphone;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMobilePrice() {
        return mobilePrice;
    }

    public void setMobilePrice(String mobilePrice) {
        this.mobilePrice = mobilePrice;
    }

    public String getIemiNumber() {
        return iemiNumber;
    }

    public void setIemiNumber(String iemiNumber) {
        this.iemiNumber = iemiNumber;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBilldate() {
        return billdate;
    }

    public void setBilldate(String billdate) {
        this.billdate = billdate;
    }

    public String getCharger() {
        return charger;
    }

    public void setCharger(String charger) {
        this.charger = charger;
    }

    public String getHeadphone() {
        return headphone;
    }

    public void setHeadphone(String headphone) {
        this.headphone = headphone;
    }
}
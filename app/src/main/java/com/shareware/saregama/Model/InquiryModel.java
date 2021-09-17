package com.shareware.saregama.Model;

public class InquiryModel {
    private String buyerAddress,buyerName,color,companyName,mobileNo,mobilePrice,mobileSerialNumber,modelName,sellerName,date,comment;

    public InquiryModel() {
    }

    public InquiryModel(String buyerAddress, String buyerName, String color, String companyName, String mobileNo, String mobilePrice, String modelName, String sellerName, String date, String comment) {
        this.buyerAddress = buyerAddress;
        this.buyerName = buyerName;
        this.color = color;
        this.companyName = companyName;
        this.mobileNo = mobileNo;
        this.mobilePrice = mobilePrice;
        this.modelName = modelName;
        this.sellerName = sellerName;
        this.date = date;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getMobileSerialNumber() {
        return mobileSerialNumber;
    }

    public void setMobileSerialNumber(String mobileSerialNumber) {
        this.mobileSerialNumber = mobileSerialNumber;
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
}

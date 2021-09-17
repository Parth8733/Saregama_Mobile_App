package com.shareware.saregama.Model;

public class AccessoriesInquiryModel {
    public String buyerName,mobileNo,billdate,requirement,sellerName;

    public AccessoriesInquiryModel() {
    }

    public AccessoriesInquiryModel(String buyerName, String mobileNo, String billdate, String requirement, String sellerName) {
        this.buyerName = buyerName;
        this.mobileNo = mobileNo;
        this.billdate = billdate;
        this.requirement = requirement;
        this.sellerName = sellerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getBilldate() {
        return billdate;
    }

    public void setBilldate(String billdate) {
        this.billdate = billdate;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }
}

package com.shareware.saregama.Model;

public class RepairInquiryModel {
    public String buyerName,date,engineer,mobileNo,modelName,problem,receiveitem,sellerName,slipNumber;

    public RepairInquiryModel() {
    }

    public RepairInquiryModel(String buyerName, String date, String engineer, String mobileNo, String modelName, String problem, String receiveitem, String sellerName, String slipNumber) {
        this.buyerName = buyerName;
        this.date = date;
        this.engineer = engineer;
        this.mobileNo = mobileNo;
        this.modelName = modelName;
        this.problem = problem;
        this.receiveitem = receiveitem;
        this.sellerName = sellerName;
        this.slipNumber = slipNumber;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEngineer() {
        return engineer;
    }

    public void setEngineer(String engineer) {
        this.engineer = engineer;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getReceiveitem() {
        return receiveitem;
    }

    public void setReceiveitem(String receiveitem) {
        this.receiveitem = receiveitem;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSlipNumber() {
        return slipNumber;
    }

    public void setSlipNumber(String slipNumber) {
        this.slipNumber = slipNumber;
    }
}

package com.shareware.saregama.Model;

public class SellerModel {

    private String buyerAddress,buyerName,color,companyName,mobileNo,mobilePrice,Requirement,mobileSerialNumber,Quantity,modelName,sellerName,date,billdate,charger,headphone,iemiNumber;
    private String slipNumber,Problem,Receiveitem,Engineer,name,number;

    public SellerModel() {
    }

    public SellerModel (String buyerAddress, String buyerName, String color, String companyName, String mobileNo, String mobilePrice, String mobileSerialNumber, String modelName, String sellerName, String date, String quantity, String d) {
        this.buyerAddress = buyerAddress;
        this.buyerName = buyerName;
        this.color = color;
        this.companyName = companyName;
        this.mobileNo = mobileNo;
        this.mobilePrice = mobilePrice;
        this.mobileSerialNumber = mobileSerialNumber;
        this.modelName = modelName;
        this.sellerName = sellerName;
        this.date = date;
        this.Quantity = quantity;
    }

    public SellerModel (String buyerAddress, String buyerName, String color, String companyName, String mobileNo, String mobilePrice, String modelName, String sellerName, String date) {
        this.buyerAddress = buyerAddress;
        this.buyerName = buyerName;
        this.color = color;
        this.companyName = companyName;
        this.mobileNo = mobileNo;
        this.mobilePrice = mobilePrice;
        this.modelName = modelName;
        this.sellerName = sellerName;
        this.date = date;
    }

    public SellerModel (String buyerAddress, String buyerName, String color, String companyName, String mobileNo, String mobilePrice, String modelName, String iemiNumber, String billDate, String charger, String headphone,String SellerName,String Date) {
        this.buyerAddress = buyerAddress;
        this.buyerName = buyerName;
        this.color = color;
        this.companyName = companyName;
        this.mobileNo = mobileNo;
        this.mobilePrice = mobilePrice;
        this.modelName = modelName;
        this.iemiNumber = iemiNumber;
        this.billdate = billDate;
        this.charger = charger;
        this.headphone = headphone;
        this.sellerName = SellerName;
        this.date = Date;
    }

    public SellerModel (String buyerName,String mobileNo, String billDate, String requirement, String sellername) {
        this.buyerName = buyerName;
        this.mobileNo = mobileNo;
        this.billdate = billDate;
        this.Requirement = requirement;
        this.sellerName = sellername;
    }

    public SellerModel (String name,String number, String billDate) {
        this.name = name;
        this.number = number;
        this.billdate = billDate;
    }

    public SellerModel (String slipnumber, String buyerName,String mobileNo, String model, String enginer, String problem, String receiveitem,String sellername,String currdate,String demo) {
        this.slipNumber = slipnumber;
        this.buyerName = buyerName;
        this.mobileNo = mobileNo;
        this.modelName = model;
        this.Engineer = enginer;
        this.Problem = problem;
        this.Receiveitem = receiveitem;
        this.sellerName = sellername;
        this.date = currdate;
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

    public String getSlipNumber() {
        return slipNumber;
    }

    public void setSlipNumber(String slipNumber) {
        this.slipNumber = slipNumber;
    }

    public String getProbelm() {
        return Problem;
    }

    public void setProbelm(String probelm) {
        Problem = probelm;
    }

    public String getReceiveitem() {
        return Receiveitem;
    }

    public void setReceiveitem(String receiveitem) {
        Receiveitem = receiveitem;
    }

    public String getEngineer() {
        return Engineer;
    }

    public void setEngineer(String engineer) {
        Engineer = engineer;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getRequirement() {
        return Requirement;
    }

    public void setRequirement(String requirement) {
        Requirement = requirement;
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

    public String getIemiNumber() {
        return iemiNumber;
    }

    public void setIemiNumber(String iemiNumber) {
        this.iemiNumber = iemiNumber;
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

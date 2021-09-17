package com.shareware.saregama.Model;

public class Model {
    private String Name,
            Image,
            Price,
            BrandId,
            Brand,
            Color,
            Quantity;

    public Model() {
    }

    public Model(String name, String image, String price, String brandId, String brand, String color, String quantity) {
        Name = name;
        Image = image;
        Price = price;
        BrandId = brandId;
        Brand = brand;
        Color = color;
        Quantity = quantity;
    }

    public Model(String name, String price, String brandId, String brand, String color, String quantity) {
        Name = name;
        Price = price;
        BrandId = brandId;
        Brand = brand;
        Color = color;
        Quantity = quantity;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getBrandId() {
        return BrandId;
    }

    public void setBrandId(String brandId) {
        BrandId = brandId;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}

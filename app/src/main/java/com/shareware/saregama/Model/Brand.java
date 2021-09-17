package com.shareware.saregama.Model;

/**
 * Created by SID on 2018-03-29.
 */

public class Brand {
    private String Name;
    private String Image;

    public Brand() {
    }

    public Brand(String name, String image) {
        Name = name;
        Image = image;
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
}

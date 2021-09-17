package com.shareware.saregama.Model;

public class User {
    public String name,password,date,number;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(String name, String number, String date) {
        this.name = name;
        this.number = number;
        this.date = date;
    }


    public User() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

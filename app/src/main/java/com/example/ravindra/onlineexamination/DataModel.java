package com.example.ravindra.onlineexamination;

/**
 * Created by Ravindra on 04-Mar-18.
 */

public class DataModel {
    String name;
    String phone;

    public DataModel() {
    }

    public DataModel(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

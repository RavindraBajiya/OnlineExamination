package com.example.ravindra.onlineexamination;

public class SetMenu {
    String name;
    Boolean status;

    public SetMenu(String name, Boolean status) {
        this.name = name;
        this.status = status;
    }

    public SetMenu() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}

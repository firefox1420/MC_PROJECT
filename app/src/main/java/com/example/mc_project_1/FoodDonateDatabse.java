package com.example.mc_project_1;

public class FoodDonateDatabse {
    String username, email, phoneNo, serves, time, type, best_bef, typeVeg,food_donate_image;

    public FoodDonateDatabse(String username, String email, String phoneNo, String serves, String time, String type, String best_bef, String typeVeg,String food_donate_image) {
        this.username = username;
        this.email = email;
        this.phoneNo = phoneNo;
        this.serves = serves;
        this.time = time;
        this.type = type;
        this.best_bef = best_bef;
        this.typeVeg = typeVeg;
        this.food_donate_image=food_donate_image;
    }

    public String getFood_donate_image() {
        return food_donate_image;
    }

    public void setFood_donate_image(String food_donate_image) {
        this.food_donate_image = food_donate_image;
    }

    public FoodDonateDatabse() {
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getServes() {
        return serves;
    }

    public void setServes(String serves) {
        this.serves = serves;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeVeg() {
        return typeVeg;
    }

    public void setTypeVeg(String typeVeg) {
        this.typeVeg = typeVeg;
    }

    public String getBest_bef() {
        return best_bef;
    }

    public void setBest_bef(String best_bef) {
        this.best_bef = best_bef;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

}

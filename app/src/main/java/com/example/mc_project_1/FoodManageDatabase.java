package com.example.mc_project_1;

public class FoodManageDatabase {

//    new UserHelperClass(username, email, phoneNo, amountOfFoodWaste, time, typeOfFoodWaste, reasonForWaste, typeVeg, desc);


    String username, email, phoneNo, amountofFoodWaste, time, typeOfFoodWaste, reasonForWaste, typeVeg, desc;

    public FoodManageDatabase(String username, String email, String phoneNo, String amountofFoodWaste, String time, String typeOfFoodWaste, String reasonForWaste, String typeVeg, String desc) {
        this.username = username;
        this.email = email;
        this.phoneNo = phoneNo;
        this.amountofFoodWaste = amountofFoodWaste;
        this.time = time;
        this.typeOfFoodWaste = typeOfFoodWaste;
        this.reasonForWaste = reasonForWaste;
        this.typeVeg = typeVeg;
        this.desc = desc;
    }


    public FoodManageDatabase() {
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAmountofFoodWaste() {
        return amountofFoodWaste;
    }

    public void setAmountofFoodWaste(String amountofFoodWaste) {
        this.amountofFoodWaste = amountofFoodWaste;
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

    public String getTypeOfFoodWaste() {
        return typeOfFoodWaste;
    }

    public String getTypeVeg() {
        return typeVeg;
    }

    public void setTypeVeg(String typeVeg) {
        this.typeVeg = typeVeg;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTypeOfFoodWaste(String typeOfFoodWaste) {
        this.typeOfFoodWaste = typeOfFoodWaste;
    }

    public String getReasonForWaste() {
        return reasonForWaste;
    }

    public void setReasonForWaste(String reasonForWaste) {
        this.reasonForWaste = reasonForWaste;
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

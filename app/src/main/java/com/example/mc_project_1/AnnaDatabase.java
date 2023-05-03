package com.example.mc_project_1;


public class AnnaDatabase
{
    String devicetoken;
    String emailid;
    String name;
    String contact;
    String address;
    String pincode;
    double Latitude;
    double Longtitude;
    FoodManageDatabase foodManageDatabase;
    FoodDonateDatabse foodDonateDatabse;
    ComplaintDatabase complaintDatabase;
    boolean status;

    public AnnaDatabase(String devicetoken,String emailid,String name, String contact, String address, String pincode, double latitude, double longtitude, FoodManageDatabase foodManageDatabase, FoodDonateDatabse foodDonateDatabse, ComplaintDatabase complaintDatabase, boolean status) {
        this.devicetoken=devicetoken;
        this.emailid=emailid;
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.pincode = pincode;
        Latitude = latitude;
        Longtitude = longtitude;
        this.foodManageDatabase = foodManageDatabase;
        this.foodDonateDatabse = foodDonateDatabse;
        this.complaintDatabase = complaintDatabase;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongtitude() {
        return Longtitude;
    }

    public void setLongtitude(double longtitude) {
        Longtitude = longtitude;
    }

    public FoodManageDatabase getFoodManageDatabase() {
        return foodManageDatabase;
    }

    public void setFoodManageDatabase(FoodManageDatabase foodManageDatabase) {
        this.foodManageDatabase = foodManageDatabase;
    }

    public FoodDonateDatabse getFoodDonateDatabse() {
        return foodDonateDatabse;
    }

    public void setFoodDonateDatabse(FoodDonateDatabse foodDonateDatabse) {
        this.foodDonateDatabse = foodDonateDatabse;
    }

    public ComplaintDatabase getComplaintDatabase() {
        return complaintDatabase;
    }

    public void setComplaintDatabase(ComplaintDatabase complaintDatabase) {
        this.complaintDatabase = complaintDatabase;
    }

    public boolean isStatus() {
        return status;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }
}

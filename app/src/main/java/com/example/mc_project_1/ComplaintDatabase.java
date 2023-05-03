package com.example.mc_project_1;

public class ComplaintDatabase {
    String username, email, phoneNo, complaint;

    public ComplaintDatabase() {
    }
    public ComplaintDatabase(String username, String email, String phoneNo, String complaint) {
        this.username = username;
        this.email = email;
        this.phoneNo = phoneNo;
        this.complaint = complaint;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
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

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }
}

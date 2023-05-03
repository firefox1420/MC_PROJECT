package com.example.mc_project_1;

public class UserDetails {


        public String UserName;
        public String Address;
        public String ContactNo;
        public String pincode;

        public UserDetails() {
            // Default constructor required for calls to DataSnapshot.getValue(UserData.class)
        }

        public UserDetails(String UserName, String Address, String ContactNo,String pincode) {
            this.UserName = UserName;
            this.Address = Address;
            this.ContactNo = ContactNo;
            this.pincode = pincode;
        }

}


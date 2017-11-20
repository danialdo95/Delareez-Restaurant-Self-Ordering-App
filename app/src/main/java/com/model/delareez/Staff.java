package com.model.delareez;

/**
 * Created by User on 26/9/2017.
 */

public class Staff {

    String staffID,staffEmail,staffPassword;

    public Staff(){

    }

    public Staff(String staffID, String staffEmail, String staffPassword) {
        this.staffID = staffID;
        this.staffEmail = staffEmail;
        this.staffPassword = staffPassword;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

    public String getStaffPassword() {
        return staffPassword;
    }

    public void setStaffPassword(String staffPassword) {
        this.staffPassword = staffPassword;
    }
}

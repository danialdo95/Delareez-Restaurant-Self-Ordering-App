package com.model.delareez;

/**
 * Created by User on 26/9/2017.
 */

public class Staff {

    String staffID,staffEmail,staffPassword, staffType, managingStaffID;

    public Staff(){

    }

    public Staff(String staffID, String staffEmail, String staffPassword, String staffType, String managingStaffID) {
        this.staffID = staffID;
        this.staffEmail = staffEmail;
        this.staffPassword = staffPassword;
        this.staffType = staffType;
        this.managingStaffID = managingStaffID;
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

    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    public String getManagingStaffID() {
        return managingStaffID;
    }

    public void setManagingStaffID(String managingStaffID) {
        this.managingStaffID = managingStaffID;
    }
}

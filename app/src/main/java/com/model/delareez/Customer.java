package com.model.delareez;

/**
 * Created by User on 26/9/2017.
 */

public class Customer {

    String custID,custEmail,custPassword;

    public Customer(){

    }

    public Customer(String custID, String custEmail, String custPassword) {
        this.custID = custID;
        this.custEmail = custEmail;
        this.custPassword = custPassword;
    }

    public String getCustID() {
        return custID;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public String getCustPassword() {
        return custPassword;
    }

    public void setCustPassword(String custPassword) {
        this.custPassword = custPassword;
    }
}

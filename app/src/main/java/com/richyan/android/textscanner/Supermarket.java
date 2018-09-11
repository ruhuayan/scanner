package com.richyan.android.textscanner;

/**
 * Created by ruhua on 9/3/2017.
 */

public class Supermarket {
    private String supername;
    private String address;
    private String email;
    private String phone;
    private String owner;
    private String passwood;
    private Boolean authorized;
    public Supermarket(){
    }

    public Supermarket(String supername, String address, String email, String phone, String owner, String passwood){
        this.supername = supername;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.owner = owner;
        this.passwood = passwood;
    }

    public String getSupername() {
        return supername;
    }

    public void setSupername(String supername) {
        this.supername = supername;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPasswood() {
        return passwood;
    }

    public void setPasswood(String passwood) {
        this.passwood = passwood;
    }

    public Boolean getAuthorized() {
        return authorized;
    }

}

package com.richyan.android.textscanner;

/**
 * Created by ruhua on 9/3/2017.
 */

public class User {

    private String username;
    private String email;
    private String supermarket;
    private String address;
    private Boolean authorized;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String supermarket, String address) {
        this.username = username;
        this.email = email;
        this.supermarket = supermarket;
        this.address = address;
        this.authorized = false;
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

    public Boolean getAuthorized(){
        return authorized;
    }
}

package com.e2b.model.request;

/**
 * Created by gaurav on 8/2/17.
 */

public class SignupRequest {

      private String mobile;
      private String password;
      private String name;
      private String role;
      private String address;
      private Coordinate co_ordinates;


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Coordinate getCo_ordinates() {
        return co_ordinates;
    }

    public void setCo_ordinates(Coordinate co_ordinates) {
        this.co_ordinates = co_ordinates;
    }
}

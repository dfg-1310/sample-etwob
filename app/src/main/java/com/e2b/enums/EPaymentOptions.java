package com.e2b.enums;

/**
 * Created by gaurav on 10/5/17.
 */

public enum EPaymentOptions {

    COD("COD"),
    PAYTM("Paytm")
    ;

    private final String name;

    EPaymentOptions(String name) {
        this.name = name;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }

}

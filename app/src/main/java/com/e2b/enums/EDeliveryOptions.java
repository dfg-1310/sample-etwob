package com.e2b.enums;

/**
 * Created by gaurav on 10/5/17.
 */

public enum EDeliveryOptions {

    PICKUP("Pickup"),
    DELIVERY("Delivery")
    ;

    private final String name;

    EDeliveryOptions(String name) {
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

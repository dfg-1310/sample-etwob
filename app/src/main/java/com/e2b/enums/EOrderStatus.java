package com.e2b.enums;

/**
 * Created by gaurav on 10/5/17.
 */

public enum EOrderStatus {

    ACCEPTED("Accepted"),
    PENDING("Pending"),
    REJECT("Rejected"),
    CANCEL("Cancelled"),
    CONFIRM("Confirmed"),
    READY_FOR_DELIVERY("Ready For Delivery"),
    READY_FOR_PICKUP("Ready For Pickup"),
    COMPLETED("Completed");

    private final String name;

    EOrderStatus(String name) {
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

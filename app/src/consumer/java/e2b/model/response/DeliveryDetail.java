package e2b.model.response;

import java.io.Serializable;

/**
 * Created by gaurav on 6/3/17.
 */

public class DeliveryDetail implements Serializable {

    private boolean isAvailable;
    private String timing;
    private int radius;
    private int minAmount;

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

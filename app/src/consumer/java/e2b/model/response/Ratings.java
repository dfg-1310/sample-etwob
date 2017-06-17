package e2b.model.response;

import com.e2b.model.response.PlaceOrder;

import java.util.List;

/**
 * Created by gaurav on 6/5/17.
 */

public class Ratings {

    private List<Rating> ratings;

    public List<Rating> getOrders() {
        return ratings;
    }

    public void setOrders(List<Rating> ratings) {
        this.ratings = ratings;
    }
}

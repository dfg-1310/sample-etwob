package e2b.model.response;

import com.e2b.model.response.PlaceOrder;

import java.util.List;

/**
 * Created by gaurav on 6/5/17.
 */

public class Orders {

    private List<PlaceOrder> orders;

    public List<PlaceOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<PlaceOrder> orders) {
        this.orders = orders;
    }
}

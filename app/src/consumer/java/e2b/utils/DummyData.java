package e2b.utils;

import com.e2b.model.response.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaurav on 17/2/17.
 */

public class DummyData {

    public static List<Order> getOrders(){
            List<Order> orders = new ArrayList<>();

        Order order = new Order();
            order.setDate("Date : 22nd Jan 2017");
            order.setOrderTitle("# ORDER 1: Order is being processing for 6 items");
            order.setShopName("Shop Name : Easy day general store");

        orders.add(order);

        order = new Order();
        order.setDate("Date : 22nd Jan 2017");
        order.setOrderTitle("# ORDER 1: Order is being processing for 6 items");
        order.setShopName("Shop Name : Easy day general store");

        orders.add(order);
        order = new Order();
        order.setDate("Date : 22nd Jan 2017");
        order.setOrderTitle("# ORDER 1: Order is being processing for 6 items");
        order.setShopName("Shop Name : Easy day general store");

        orders.add(order);
        order = new Order();
        order.setDate("Date : 22nd Jan 2017");
        order.setOrderTitle("# ORDER 1: Order is being processing for 6 items");
        order.setShopName("Shop Name : Easy day general store");

        orders.add(order);

        order = new Order();
        order.setDate("Date : 22nd Jan 2017");
        order.setOrderTitle("# ORDER 1: Order is being processing for 6 items");
        order.setShopName("Shop Name : Easy day general store");

        orders.add(order);
        order = new Order();
        order.setDate("Date : 22nd Jan 2017");
        order.setOrderTitle("# ORDER 1: Order is being processing for 6 items");
        order.setShopName("Shop Name : Easy day general store");

        orders.add(order);

        return orders;
    }



}

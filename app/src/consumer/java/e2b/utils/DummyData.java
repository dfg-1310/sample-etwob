package e2b.utils;

import com.e2b.model.response.Order;

import java.util.ArrayList;
import java.util.List;

import e2b.model.response.Notification;

/**
 * Created by gaurav on 17/2/17.
 */

public class DummyData {


    public static String DEFAULT_URL = "http://placehold.it/400";
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


    public static List<Notification> getNotifications() {
        List<Notification> notifications = new ArrayList<>();
        Notification notification = new Notification();
        notification.setDate("11:30 AM");
        notification.setShopName("# We have received your request for 5 items, we will notify you when your order gets ready ");
        notification.setTitle("Store Home 99");

        notifications.add(notification);

        notification = new Notification();
        notification.setDate("11:30 AM");
        notification.setShopName("# We have received your request for 5 items, we will notify you when your order gets ready ");
        notification.setTitle("Store Home 99");

        notifications.add(notification);


        notification = new Notification();
        notification.setDate("11:30 AM");
        notification.setShopName("# We have received your request for 5 items, we will notify you when your order gets ready ");
        notification.setTitle("Store Home 99");

        notifications.add(notification);


        notification = new Notification();
        notification.setDate("11:30 AM");
        notification.setShopName("# We have received your request for 5 items, we will notify you when your order gets ready ");
        notification.setTitle("Store Home 99");

        notifications.add(notification);

        notification = new Notification();
        notification.setDate("11:30 AM");
        notification.setShopName("# We have received your request for 5 items, we will notify you when your order gets ready ");
        notification.setTitle("Store Home 99");

        notifications.add(notification);


        return notifications;

    }


}

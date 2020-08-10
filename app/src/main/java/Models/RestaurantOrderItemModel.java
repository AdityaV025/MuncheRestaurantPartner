package Models;

import java.util.ArrayList;

public class RestaurantOrderItemModel {

    public RestaurantOrderItemModel() {
    }

    public RestaurantOrderItemModel(ArrayList<String> ordered_items, String short_time, String order_id, String delivery_address, String total_amount, String payment_method, String ordered_at) {
        this.ordered_items = ordered_items;
        this.short_time = short_time;
        this.order_id = order_id;
        this.delivery_address = delivery_address;
        this.total_amount = total_amount;
        this.payment_method = payment_method;
        this.ordered_at = ordered_at;
    }

    public ArrayList<String> getOrdered_items() {
        return ordered_items;
    }

    public void setOrdered_items(ArrayList<String> ordered_items) {
        this.ordered_items = ordered_items;
    }

    public String getShort_time() {
        return short_time;
    }

    public void setShort_time(String short_time) {
        this.short_time = short_time;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getOrdered_at() {
        return ordered_at;
    }

    public void setOrdered_at(String ordered_at) {
        this.ordered_at = ordered_at;
    }

    private ArrayList<String> ordered_items;
    private String short_time;
    private String order_id;
    private String delivery_address;
    private String total_amount;
    private String payment_method;
    private String ordered_at;

}

package com.e2b.model.response;

/**
 * Created by gaurav on 6/5/17.
 */

public class PlaceOrder {

    private String __v;
    private String orderImg;
    private String orderAudio;
    private String consumer;
    private String merchant;
    private String _id;
    private String status;
    private String createdOn;

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    public String getOrderImg() {
        return orderImg;
    }

    public void setOrderImg(String orderImg) {
        this.orderImg = orderImg;
    }

    public String getOrderAudio() {
        return orderAudio;
    }

    public void setOrderAudio(String orderAudio) {
        this.orderAudio = orderAudio;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

}

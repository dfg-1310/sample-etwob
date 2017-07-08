package com.e2b.model.response;

/**
 * Created by gaurav on 8/7/17.
 */

public class Data {
    private Payload payload;
    private String msg;
    private String consumer;
    private String merchant;

    /**
     *  "msg": "A new order has been placed against you.",
     "consumer": "596093a4c3004606ef4ecf70",
     "merchant": "59609918d7ad9c0e8d0abe85"
     */
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}


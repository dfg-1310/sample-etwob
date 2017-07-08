package com.e2b.model.response;

/**
 * Created by gaurav on 8/7/17.
 */

public class Notification {

    private String msg;
    private Payload payload;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}

package com.e2b.model.response;

/**
 * Created by gaurav on 7/7/17.
 */

public class Notification {
      private String msg;
      private Payload payload;

    /**
     * {msg: "new mssage", payload:{"s":"New", id:"order id"}}
     */

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

class Payload{
    private String s;
    private String id;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

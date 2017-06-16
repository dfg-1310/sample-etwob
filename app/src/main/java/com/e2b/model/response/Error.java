package com.e2b.model.response;

public class Error {

    private String code;
    private String errmsg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return errmsg;
    }

    public void setMsg(String errmsg) {
        this.errmsg = errmsg;
    }
}

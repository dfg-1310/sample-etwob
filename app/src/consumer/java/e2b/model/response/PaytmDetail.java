package e2b.model.response;

import java.io.Serializable;

/**
 * Created by gaurav on 6/3/17.
 */

public class PaytmDetail implements Serializable {
    private String mobile;
    private String _id;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
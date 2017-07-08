package com.e2b.model.response;

/**
 * Created by gaurav on 7/7/17.
 */

public class Notification {
      private String _id;
      private Data data;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    /**
     * {
     "_id": "5960a13fd7ad9c0e8d0abe8c",
     "data": {
     "payload": {
     "s": "New",
     "id": "5960a13dd7ad9c0e8d0abe8b"
     },
     "msg": "A new order has been placed against you.",
     "consumer": "596093a4c3004606ef4ecf70",
     "merchant": "59609918d7ad9c0e8d0abe85"
     }
     }
     */


}


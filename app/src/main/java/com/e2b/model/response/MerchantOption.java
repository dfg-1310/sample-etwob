package com.e2b.model.response;

import java.io.Serializable;

import e2bmerchant.model.response.DeliveryDetail;
import e2bmerchant.model.response.PaytmDetail;


/**
 * Created by gaurav on 15/5/17.
 */

public class MerchantOption implements Serializable {

    private PaytmDetail paytmDetail;
    private DeliveryDetail deliveryDetail;

    public PaytmDetail getPaytmDetail() {
        return paytmDetail;
    }

    public void setPaytmDetail(PaytmDetail paytmDetail) {
        this.paytmDetail = paytmDetail;
    }

    public DeliveryDetail getDeliveryDetail() {
        return deliveryDetail;
    }

    public void setDeliveryDetail(DeliveryDetail deliveryDetail) {
        this.deliveryDetail = deliveryDetail;
    }
}

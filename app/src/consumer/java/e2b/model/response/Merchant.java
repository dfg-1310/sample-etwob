package e2b.model.response;

import com.e2b.model.request.Coordinate;

import java.io.Serializable;
import java.util.Arrays;

public class Merchant implements Serializable {

    private String _id;
    private String mobile;
    private String __v;
    private Coordinate coordinates;
    private DeliveryDetail deliveryDetail;
    private PaytmDetail paytmDetail;
    private ShopTiming shopTiming;
    private String shopStreet;
    private String shopAddress;
    private String shopImage;
    private String shopName;
    private int status;
    private String updatedBy;
    private String updatedOn;
    private String createdBy;
    private String createdOn;
    private String[] closingDays;
    private String discount;


    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    public DeliveryDetail getDeliveryDetail() {
        return deliveryDetail;
    }

    public void setDeliveryDetail(DeliveryDetail deliveryDetail) {
        this.deliveryDetail = deliveryDetail;
    }

    public PaytmDetail getPaytmDetail() {
        return paytmDetail;
    }

    public void setPaytmDetail(PaytmDetail paytmDetail) {
        this.paytmDetail = paytmDetail;
    }

    public ShopTiming getShopTiming() {
        return shopTiming;
    }

    public void setShopTiming(ShopTiming shopTiming) {
        this.shopTiming = shopTiming;
    }

    public String getShopStreet() {
        return shopStreet;
    }

    public void setShopStreet(String shopStreet) {
        this.shopStreet = shopStreet;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String[] getClosingDays() {
        return closingDays;
    }

    public void setClosingDays(String[] closingDays) {
        this.closingDays = closingDays;
    }

    @Override
    public String toString() {
        return "Merchant{" +
                "_id='" + _id + '\'' +
                ", mobile='" + mobile + '\'' +
                ", __v='" + __v + '\'' +
                ", coordinates=" + coordinates +
                ", deliveryDetail=" + deliveryDetail +
                ", paytmDetail=" + paytmDetail +
                ", shopTiming=" + shopTiming +
                ", shopStreet='" + shopStreet + '\'' +
                ", shopAddress='" + shopAddress + '\'' +
                ", shopImage='" + shopImage + '\'' +
                ", shopName='" + shopName + '\'' +
                ", status=" + status +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", closingDays=" + Arrays.toString(closingDays) +
                '}';
    }
}








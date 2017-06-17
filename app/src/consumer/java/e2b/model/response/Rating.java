package e2b.model.response;

/**
 * Created by gaurav on 17/6/17.
 */

public class Rating {

    private String _id;
    private String consumer;
    private String merchant;
    private int __v;
    private double rating;
     private String review;
    private String givenOn;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getGivenOn() {
        return givenOn;
    }

    public void setGivenOn(String givenOn) {
        this.givenOn = givenOn;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "_id='" + _id + '\'' +
                ", consumer='" + consumer + '\'' +
                ", merchant='" + merchant + '\'' +
                ", __v=" + __v +
                ", rating=" + rating +
                ", review='" + review + '\'' +
                ", givenOn='" + givenOn + '\'' +
                '}';
    }
}

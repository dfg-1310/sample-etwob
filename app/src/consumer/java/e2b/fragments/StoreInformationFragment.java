package e2b.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.e2b.R;
import com.e2b.activity.BaseActivity;
import com.e2b.api.ApiCallback;
import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;
import com.e2b.enums.EOrderStatus;
import com.e2b.fragments.BaseFragment;
import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.Error;
import com.e2b.model.response.PlaceOrder;
import com.e2b.views.CustomTextView;
import com.google.gson.JsonObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import e2b.activity.ConsumerBaseActivity;
import e2b.activity.MapActivity;
import e2b.activity.OrderDetailActivity;
import e2b.activity.PlaceOrderActivity;
import e2b.adapter.ReviewAdapter;
import e2b.intrface.ICustomCallback;
import e2b.model.response.DeliveryDetail;
import e2b.model.response.Merchant;
import e2b.model.response.Ratings;
import e2b.utils.RatingDialog;
import retrofit2.Call;

/**
 * Created by gaurav on 30/3/17.
 */

public class StoreInformationFragment extends BaseFragment {

    @Bind(R.id.tv_store_Name)
    CustomTextView name;

    @Bind(R.id.tv_store_address)
    CustomTextView storeAddress;

    @Bind(R.id.tv_store_phone)
    CustomTextView storePhone;

    @Bind(R.id.rb_review)
    RatingBar avgRatingBar;

    @Bind(R.id.tv_place_order)
    CustomTextView placeOrder;

    @Bind(R.id.tv_shop_closing_day)
    CustomTextView closingdays;

    @Bind(R.id.tv_offers)
    CustomTextView offers;

    @Bind(R.id.tv_home_delivery)
    CustomTextView homeDelivery;

    @Bind(R.id.tv_home_delivery_title)
    CustomTextView homeDeliveryTitle;

    @Bind(R.id.lv_review)
    RecyclerView reviewListView;

    private String TAG = StoreInformationFragment.class.getCanonicalName();
    private Merchant merchant;

//    @Bind(R.id.tv_important_info)
//    CustomTextView importantInfo;

    @Bind(R.id.iv_store_image)
    ImageView storeImageView;
    private ReviewAdapter orderAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_merchant_detail, container, false);
        ButterKnife.bind(this, view);
        ((ConsumerBaseActivity) activity).setHeaderText("Store Information");
        reviewListView.setLayoutManager(new LinearLayoutManager(activity));
        reviewListView.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupData();
        if (getActivity() != null) {
            ((ConsumerBaseActivity) getActivity()).managebackIconVisiblity(true);
        }
    }

    private void setupData() {
        merchant = ((MapActivity) activity).getSelectedMerchant();
        if (merchant != null) {
            Log.d(TAG, "Merchant : " + merchant.toString());
            name.setText(merchant.getShopName());
            storeAddress.setText("Address : " + merchant.getShopAddress());
            storePhone.setText("Phone : " + merchant.getMobile());
            closingdays.setText("Shop Remain Close On : "+ getArrayString(merchant.getClosingDays()));
            offers.setText("Offers : "+ merchant.getDiscount()+"%");
            setHomeDeliveryText(merchant);
            ((BaseActivity) getActivity()).loadMerchantImageGlide(merchant.getShopImage(), storeImageView);
        }

        getRatings();
        new RatingDialog(getActivity(), 0, new ICustomCallback() {
            @Override
            public void onOkClicked(Dialog dialog, float rating, String comment) {
                Log.d(TAG, "rate value :: " + rating);
                dialog.dismiss();
                postRate(rating, comment);
            }
        });
    }

    private String setHomeDeliveryText(Merchant merchant) {
        String deliverynfo = "Home Delivery : ";
        DeliveryDetail deliveryDetail = merchant.getDeliveryDetail();
        if(deliveryDetail != null){
            String deliveryDetailInfo = "";
            deliverynfo += "Available";
            deliveryDetailInfo += "Minimum Amount For Home Delivery : "+deliveryDetail.getMinAmount()+"\n";
            deliveryDetailInfo += "Home Delivery Radius : "+deliveryDetail.getRadius()+"Kms"+"\n";
            deliveryDetailInfo += "Home Delivery Timimg : "+deliveryDetail.getTiming();

            homeDelivery.setText(deliveryDetailInfo);
        }else{
            deliverynfo += "Not Available";
            homeDelivery.setVisibility(View.GONE);
        }
        homeDeliveryTitle.setText(deliverynfo);
        return deliverynfo;
    }

    private String getArrayString(String[] closingDays) {
        String value = "";
        for(String day: closingDays){
            if(!value.equalsIgnoreCase("")){
                value+= ", ";
            }
            value+= day;
        }
        return value;
    }

    private void postRate(float rating, String comment) {
        // make api call for confirm order
        activity.showProgressBar();
        IApiRequest request = ApiClient.getRequest();

        JsonObject rateJsoonObject = new JsonObject();
        rateJsoonObject.addProperty("rating", rating);
        rateJsoonObject.addProperty("review", comment);

        Call<BaseResponse<PlaceOrder>> call = request.postRate(merchant.get_id(), rateJsoonObject);
        call.enqueue(new ApiCallback<PlaceOrder>(getActivity()) {
            @Override
            public void onSucess(PlaceOrder userResponse) {
                activity.hideProgressBar();
                activity.showToast("Your review placed successfully.");
            }

            @Override
            public void onError(Error error) {
                activity.hideProgressBar();
                activity.showToast(error.getMsg());
                Log.d(TAG, error.getMsg());
            }
        });

    }

    @OnClick(R.id.tv_place_order)
    public void goToPlaceOrder() {
        Bundle bundle = new Bundle();
        bundle.putString("merchantId", merchant.get_id());
        Intent intent = new Intent(getActivity(), PlaceOrderActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    public void getRatings() {
        // make api call for confirm order
        activity.showProgressBar();
        IApiRequest request = ApiClient.getRequest();

        Call<BaseResponse<Ratings>> call = request.getRatings(merchant.get_id());
        call.enqueue(new ApiCallback<Ratings>(getActivity()) {
            @Override
            public void onSucess(Ratings ratings) {
                activity.hideProgressBar();
                orderAdapter = new ReviewAdapter(getActivity(), ratings.getRatings());
                reviewListView.setAdapter(orderAdapter);
                orderAdapter.notifyDataSetChanged();
                setAvgRating(ratings, avgRatingBar);
            }

            @Override
            public void onError(Error error) {
                activity.hideProgressBar();
                activity.showToast(error.getMsg());
                Log.d(TAG, error.getMsg());
            }
        });
    }

    private void setAvgRating(Ratings ratings, RatingBar ratingBar) {
        double avgRating = 0;
        if(ratings != null && ratings.getRatings().size()>0){
            for(int index =0; index < ratings.getRatings().size(); index++){
                avgRating += ratings.getRatings().get(index).getRating();
            }

            avgRating = avgRating/ratings.getRatings().size();
            ratingBar.setRating((float) avgRating);
        }
    }

}

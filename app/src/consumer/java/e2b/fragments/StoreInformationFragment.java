package e2b.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import e2b.intrface.ICustomCallback;
import e2b.model.response.Merchant;
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

    @Bind(R.id.tv_store_email)
    CustomTextView storeEmail;

    @Bind(R.id.tv_place_order)
    CustomTextView placeOrder;


    private String TAG = StoreInformationFragment.class.getCanonicalName();
    private Merchant merchant;

    @Bind(R.id.tv_important_info)
    CustomTextView importantInfo;

    @Bind(R.id.iv_store_image)
    ImageView storeImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_merchant_detail, container, false);
        ButterKnife.bind(this, view);
        ((ConsumerBaseActivity)activity).setHeaderText("Store Information");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupData();
        if(getActivity() != null){
            ((ConsumerBaseActivity)getActivity()).managebackIconVisiblity(true);
        }
    }

    private void setupData() {
        merchant = ((MapActivity)activity).getSelectedMerchant();
        if(merchant != null) {
            Log.d(TAG, "Merchant : "+ merchant.toString());
            name.setText(merchant.getShopName());
            storeAddress.setText("Address : "+ merchant.getShopAddress());
            storePhone.setText("Phone : "+ merchant.getMobile());
            storeEmail.setText("Email : "+merchant.getShopName());
            importantInfo.setText(importantInfo.getText().toString().replace("MIN_ORDER_AMOUNT", ""+merchant.getDeliveryDetail().getMinAmount()));
            ((BaseActivity)getActivity()).loadImageGlide(merchant.getShopImage(), storeImageView);
        }

        new RatingDialog(getActivity(), 0, new ICustomCallback() {
            @Override
            public void onOkClicked(Dialog dialog, float rating, String comment) {
                Log.d(TAG, "rate value :: "+rating);
                dialog.dismiss();
                postRate(rating, comment);
            }
        });
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
    public void goToPlaceOrder(){
        Bundle bundle = new Bundle();
        bundle.putString("merchantId", merchant.get_id());
        Intent intent = new Intent(getActivity(), PlaceOrderActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

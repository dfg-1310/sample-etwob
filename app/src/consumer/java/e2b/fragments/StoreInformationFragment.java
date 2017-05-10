package e2b.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e2b.R;
import com.e2b.fragments.BaseFragment;
import com.e2b.views.CustomTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import e2b.activity.ConsumerBaseActivity;
import e2b.activity.MapActivity;
import e2b.activity.OrderDetailActivity;
import e2b.activity.PlaceOrderActivity;
import e2b.model.response.Merchant;

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
//    @Bind(R.id.tv_store_Name)
//    CustomTextView name;
//
//    @Bind(R.id.tv_store_Name)
//    CustomTextView name;
//
//    @Bind(R.id.tv_store_Name)
//    CustomTextView name;

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
    }

    private void setupData() {
        merchant = ((MapActivity)activity).getSelectedMerchant();
        if(merchant != null) {
            Log.d(TAG, "Merchant : "+ merchant.toString());
            name.setText(merchant.getShopName());
            storeAddress.setText("Address : "+ merchant.getShopAddress());
            storePhone.setText("Phone : "+ merchant.getMobile());
            storeEmail.setText("Email : "+merchant.getShopName());
        }
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

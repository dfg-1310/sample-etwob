package e2b.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e2b.R;
import com.e2b.views.CustomTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import e2b.activity.ConsumerBaseActivity;
import e2b.activity.MapActivity;
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
        Merchant merchant = ((MapActivity)activity).getSelectedMerchant();
        if(merchant != null) {
            name.setText(merchant.getShopName());
            storeAddress.setText("Address : "+ merchant.getShopAddress());
            storePhone.setText("Phone : "+ merchant.getMobile());
            storeEmail.setText("Email : "+merchant.getShopName());
        }
    }
}

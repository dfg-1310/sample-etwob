package e2b.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.e2b.R;
import com.e2b.api.ApiCallback;
import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;
import com.e2b.enums.EOrderStatus;
import com.e2b.fragments.BaseFragment;
import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.Error;
import com.e2b.model.response.PlaceOrder;
import com.e2b.utils.AppConstant;
import com.e2b.views.CustomTextView;
import com.google.gson.JsonObject;

import e2b.utils.DummyData;
import retrofit2.Call;

import static e2b.activity.PlaceOrderActivity.FileNameArg;

public class OrderDetailActivity extends ConsumerBaseActivity {

    private BaseFragment currentFragment;
    private PlaceOrder placeOrder;
    private CustomTextView orderIdCustomTextView;
    private CustomTextView orderStatusCustomTextView;
    private CustomTextView orderAmountTitleCustomTextView;
    private CustomTextView orderAmountCustomTextView;
    private CustomTextView waitTimeTitleCustomTextView;
    private CustomTextView waitTimeCustomTextView;
    private CustomTextView orderReceiptTitleCustomTextView;
    private CustomTextView deliveryOptionCustomTextView;
    private CustomTextView paymentOptionCustomTextView;


    private RelativeLayout orderReceiptLayout;
    private CustomTextView paymentOptionTitleCustomTextView;
    private Spinner paymentOptionSpinner;
    private CustomTextView deliveryOptionTitleCustomTextView;
    private Spinner deliveryOptionSpinner;
    private RelativeLayout footerButtonLayout;
    private Button confirmButton;
    private Button cancelmButton;
    Button playAudioButton;
    private ImageView orderImg;
    private ImageView orderReceiptImg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setHeaderText("Order Detail");
        setFooterState(AppConstant.FOOTER_INDEX.ORDER);
        managebackIconVisiblity(true);
        initViewControls();
        getDataFromItent();
//        getPlaceOrder();
        updateUI(placeOrder);
    }

    private void initViewControls() {
        orderIdCustomTextView = (CustomTextView) findViewById(R.id.order_id);
        orderStatusCustomTextView = (CustomTextView) findViewById(R.id.order_status);
        orderAmountTitleCustomTextView = (CustomTextView) findViewById(R.id.order_amount_title);
        orderAmountCustomTextView = (CustomTextView) findViewById(R.id.order_amount);
        waitTimeTitleCustomTextView = (CustomTextView) findViewById(R.id.wait_time_title);
        waitTimeCustomTextView = (CustomTextView) findViewById(R.id.wait_time);
        orderReceiptTitleCustomTextView = (CustomTextView) findViewById(R.id.order_receipt_title);
        deliveryOptionCustomTextView = (CustomTextView) findViewById(R.id.delivery_options_selected);
        paymentOptionCustomTextView = (CustomTextView) findViewById(R.id.payment_options_selected);

        orderReceiptLayout = (RelativeLayout) findViewById(R.id.order_receipt_layout);
        paymentOptionTitleCustomTextView = (CustomTextView) findViewById(R.id.payment_option_title);
        paymentOptionSpinner = (Spinner) findViewById(R.id.spinner_payment_option);
        deliveryOptionTitleCustomTextView = (CustomTextView) findViewById(R.id.delivery_option_title);
        deliveryOptionSpinner = (Spinner) findViewById(R.id.spinner_delivery_option);
        footerButtonLayout = (RelativeLayout) findViewById(R.id.footer_button_layout);
        confirmButton = (Button) findViewById(R.id.btn_confirm);
        cancelmButton = (Button) findViewById(R.id.btn_cancel);
        playAudioButton = (Button) findViewById(R.id.btn_place_order_play);

        orderImg = (ImageView) findViewById(R.id.iv_place_order_media);
        orderReceiptImg = (ImageView) findViewById(R.id.iv_receipt_order);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> deiveryAdapter = ArrayAdapter.createFromResource(this,
                R.array.delivery_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        deiveryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        deliveryOptionSpinner.setAdapter(deiveryAdapter);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> paymentAdapter = ArrayAdapter.createFromResource(this,
                R.array.payment_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        deiveryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        paymentOptionSpinner.setAdapter(paymentAdapter);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                showToast("deliveryOptionSpinner : "+deliveryOptionSpinner.getSelectedItem().toString());
//                showToast("paymentOptionSpinner : "+paymentOptionSpinner.getSelectedItem().toString());

                // make api call for confirm order
                showProgressBar();
                IApiRequest request = ApiClient.getRequest();

                JsonObject updateOrderJsonObject = new JsonObject();
                updateOrderJsonObject.addProperty("status", EOrderStatus.CONFIRM.toString());
                updateOrderJsonObject.addProperty("deliveryOptions", deliveryOptionSpinner.getSelectedItem().toString());
                updateOrderJsonObject.addProperty("paymentOptions", paymentOptionSpinner.getSelectedItem().toString());

                Call<BaseResponse<PlaceOrder>> call = request.updateOrder(placeOrder.get_id(), updateOrderJsonObject);
                call.enqueue(new ApiCallback<PlaceOrder>(OrderDetailActivity.this) {
                    @Override
                    public void onSucess(PlaceOrder userResponse) {
                        hideProgressBar();
                        showToast("Your order confirmed successfully.");
                        finish();
                    }

                    @Override
                    public void onError(Error error) {
                        hideProgressBar();
                        showToast(error.getMsg());
                        Log.d(TAG, error.getMsg());
                    }
                });
            }
        });

        cancelmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make api call for cancel order

                showProgressBar();
                IApiRequest request = ApiClient.getRequest();

                JsonObject updateOrderJsonObject = new JsonObject();
                updateOrderJsonObject.addProperty("status", EOrderStatus.CANCEL.toString());

                Call<BaseResponse<PlaceOrder>> call = request.updateOrder(placeOrder.get_id(), updateOrderJsonObject);
                call.enqueue(new ApiCallback<PlaceOrder>(OrderDetailActivity.this) {
                    @Override
                    public void onSucess(PlaceOrder userResponse) {
                        hideProgressBar();
                        showToast("Your order canceled successfully.");
                        finish();
                    }

                    @Override
                    public void onError(Error error) {
                        hideProgressBar();
                        showToast(error.getMsg());
                        Log.d(TAG, error.getMsg());
                    }
                });

            }
        });

        playAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(placeOrder.getOrderAudio())){
                    Intent i = new Intent(OrderDetailActivity.this, AudioPlaybackActivity.class);
                    Log.d(TAG, "audio file name : "+ placeOrder.getOrderAudio());
                    i.putExtra(FileNameArg, placeOrder.getOrderAudio());
                    startActivityForResult(i, 0);
                }
            }
        });
    }

    private void getPlaceOrder() {
        showProgressBar();
        IApiRequest request = ApiClient.getRequest();

        Call<BaseResponse<PlaceOrder>> call = request.getOrder(placeOrder.get_id());
        call.enqueue(new ApiCallback<PlaceOrder>(OrderDetailActivity.this) {
            @Override
            public void onSucess(PlaceOrder userResponse) {
                hideProgressBar();
                updateUI(userResponse);
            }

            @Override
            public void onError(Error error) {
                hideProgressBar();
                Log.d(TAG, error.getMsg());
            }
        });
    }

    private void updateUI(PlaceOrder placeOrder) {
        orderIdCustomTextView.setText(placeOrder.get_id());
        orderStatusCustomTextView.setText(placeOrder.getStatus());

        if (!TextUtils.isEmpty(placeOrder.getOrderImg())) {
            loadImageGlide(placeOrder.getOrderImg(), orderImg);
        } else {
            playAudioButton.setVisibility(View.VISIBLE);
        }

        if (!placeOrder.getStatus().equalsIgnoreCase("pending") &&
                !placeOrder.getStatus().equalsIgnoreCase("rejected")) {
            orderAmountTitleCustomTextView.setVisibility(View.VISIBLE);
            orderAmountCustomTextView.setVisibility(View.VISIBLE);
            waitTimeTitleCustomTextView.setVisibility(View.VISIBLE);
            waitTimeCustomTextView.setVisibility(View.VISIBLE);
            orderReceiptTitleCustomTextView.setVisibility(View.VISIBLE);
            orderReceiptLayout.setVisibility(View.VISIBLE);

            orderAmountCustomTextView.setText("Rs. " + placeOrder.getOrderAmount());
            waitTimeCustomTextView.setText(placeOrder.getWaitTime() + " Min.");

            loadImageGlide(placeOrder.getOrderReceipt(), orderReceiptImg);

        }

        if (placeOrder.getStatus().equalsIgnoreCase("accepted")) {
            paymentOptionTitleCustomTextView.setVisibility(View.VISIBLE);
            paymentOptionSpinner.setVisibility(View.VISIBLE);
            deliveryOptionTitleCustomTextView.setVisibility(View.VISIBLE);
            deliveryOptionSpinner.setVisibility(View.VISIBLE);
            footerButtonLayout.setVisibility(View.VISIBLE);
        } else if (placeOrder.getStatus().equalsIgnoreCase("cancelled")) {

        } else if (placeOrder.getStatus().equalsIgnoreCase("confirmed") ||
                placeOrder.getStatus().equalsIgnoreCase("ready for deliver") ||
                placeOrder.getStatus().equalsIgnoreCase("ready for pickup") ||
                placeOrder.getStatus().equalsIgnoreCase("delivered") ||
                placeOrder.getStatus().equalsIgnoreCase("pickedup")
                ) {
            if (!TextUtils.isEmpty(placeOrder.getDeliveryOptions())) {
                deliveryOptionTitleCustomTextView.setVisibility(View.VISIBLE);
                deliveryOptionCustomTextView.setVisibility(View.VISIBLE);
                deliveryOptionCustomTextView.setText(placeOrder.getDeliveryOptions());
            }

            if (!TextUtils.isEmpty(placeOrder.getPaymentOptions())) {
                paymentOptionTitleCustomTextView.setVisibility(View.VISIBLE);
                paymentOptionCustomTextView.setVisibility(View.VISIBLE);
                paymentOptionCustomTextView.setText(placeOrder.getPaymentOptions());
            }
        }
    }

    private void getDataFromItent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            placeOrder = (PlaceOrder) bundle.getSerializable("order");
            Log.d(TAG, "place order : " + placeOrder.toString());
        }
    }
}

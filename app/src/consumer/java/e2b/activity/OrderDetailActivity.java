package e2b.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.e2b.R;
import com.e2b.api.ApiCallback;
import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;
import com.e2b.fragments.BaseFragment;
import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.Error;
import com.e2b.model.response.PlaceOrder;
import com.e2b.utils.AppConstant;
import com.e2b.views.CustomTextView;

import retrofit2.Call;

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
    private RelativeLayout orderReceiptLayout;
    private CustomTextView paymentOptionTitleCustomTextView;
    private Spinner paymentOptionSpinner;
    private CustomTextView deliveryOptionTitleCustomTextView;
    private Spinner deliveryOptionSpinner;
    private RelativeLayout footerButtonLayout;
    private Button confirmButton;
    private Button cancelmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setHeaderText("Order Detail");
        setFooterState(AppConstant.FOOTER_INDEX.ORDER);
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
        orderReceiptLayout = (RelativeLayout) findViewById(R.id.order_receipt_layout);
        paymentOptionTitleCustomTextView = (CustomTextView) findViewById(R.id.payment_option_title);
        paymentOptionSpinner = (Spinner) findViewById(R.id.spinner_payment_option);
        deliveryOptionTitleCustomTextView = (CustomTextView) findViewById(R.id.delivery_option_title);
        deliveryOptionSpinner = (Spinner) findViewById(R.id.spinner_delivery_option);
        footerButtonLayout = (RelativeLayout) findViewById(R.id.footer_button_layout);
        confirmButton = (Button) findViewById(R.id.btn_confirm);
        cancelmButton = (Button) findViewById(R.id.btn_confirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancelmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        if (!placeOrder.getStatus().equalsIgnoreCase("pending") &&
                !placeOrder.getStatus().equalsIgnoreCase("rejected")) {
            orderAmountTitleCustomTextView.setVisibility(View.VISIBLE);
            orderAmountCustomTextView.setVisibility(View.VISIBLE);
            waitTimeTitleCustomTextView.setVisibility(View.VISIBLE);
            waitTimeCustomTextView.setVisibility(View.VISIBLE);
            orderReceiptTitleCustomTextView.setVisibility(View.VISIBLE);
            orderReceiptLayout.setVisibility(View.VISIBLE);
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
// TODO - selected value in spinner for payment option and delivery option.
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

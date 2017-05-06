package e2b.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.e2b.R;
import com.e2b.api.ApiCallback;
import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;
import com.e2b.fragments.BaseFragment;
import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.Error;
import com.e2b.model.response.PlaceOrder;
import com.e2b.utils.AppConstant;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import e2b.enums.EScreenType;
import e2b.fragments.FragmentFactory;
import e2b.model.response.UserResponse;
import retrofit2.Call;

public class PlaceOrderActivity extends ConsumerBaseActivity {

//    @Nullable
//    @Bind(R.id.iv_place_order_media)
    ImageView placeOrderImageView;

//    @Nullable
//    @Bind(R.id.btn_place_order_play)
    Button playButton;

//    @Nullable
//    @Bind(R.id.btn_take_photo)
    Button takePhotoButton;

//    @Nullable
//    @Bind(R.id.btn_take_audio)
    Button takeAudioButton;

//    @Nullable
//    @Bind(R.id.btn_place_order)
    Button placeOrderButton;

    private BaseFragment currentFragment;
    private String merchantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        setHeaderText("Place Order");
        setFooterState(AppConstant.FOOTER_INDEX.HOME);
        getDataFromBundle();
        initviewcontrols();
    }

    private void initviewcontrols() {
        placeOrderImageView = (ImageView) findViewById(R.id.iv_place_order_media);
        playButton = (Button) findViewById(R.id.btn_place_order_play);
        takePhotoButton = (Button) findViewById(R.id.btn_take_photo);
        takeAudioButton = (Button) findViewById(R.id.btn_take_audio);
        placeOrderButton = (Button) findViewById(R.id.btn_place_order);

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           showToast("take photo");
            }
        });

        takeAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("take audio");
            }
        });

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("place order");
                showProgressBar();
                IApiRequest request = ApiClient.getRequest();

                JsonObject placeOrderJsonObject = new JsonObject();
                placeOrderJsonObject.addProperty("orderImg", "");
                placeOrderJsonObject.addProperty("orderAudio", "");
                placeOrderJsonObject.addProperty("merchant", merchantId);


                Call<BaseResponse<PlaceOrder>> call = request.placeOrder(placeOrderJsonObject);
                call.enqueue(new ApiCallback<PlaceOrder>(PlaceOrderActivity.this) {
                    @Override
                    public void onSucess(PlaceOrder userResponse) {
                        hideProgressBar();
                        showToast("Your order placed successfully.");
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
    }

    private void getDataFromBundle() {
        if (getIntent().getExtras() != null) {
            merchantId = getIntent().getExtras().getString("merchantId", "");
            showToast("merchantId : " + merchantId);
        }
    }


}

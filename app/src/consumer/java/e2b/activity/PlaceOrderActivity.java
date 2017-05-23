package e2b.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.e2b.R;
import com.e2b.activity.CameraActivity;
import com.e2b.api.ApiCallback;
import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;
import com.e2b.fragments.BaseFragment;
import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.Error;
import com.e2b.model.response.PlaceOrder;
import com.e2b.utils.AppConstant;
import com.e2b.utils.BitmapUtils;
import com.google.gson.JsonObject;

import java.io.File;

import e2b.intrface.ICameraCallback;
import e2b.utils.CameraDialog;
import e2b.utils.DummyData;
import retrofit2.Call;

public class PlaceOrderActivity extends ConsumerBaseActivity {

    ImageView placeOrderImageView;
    Button playButton;
    Button takePhotoButton;
    Button takeAudioButton;
    Button placeOrderButton;

    private BaseFragment currentFragment;
    private String merchantId;
    private File finalFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        setHeaderText("Place Your Order");
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
                takeOrderPhoto();
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
//                showToast("place order");
                showProgressBar();
                IApiRequest request = ApiClient.getRequest();

                JsonObject placeOrderJsonObject = new JsonObject();
                placeOrderJsonObject.addProperty("orderImg", DummyData.DEFAULT_URL);
                placeOrderJsonObject.addProperty("orderAudio", "");
                placeOrderJsonObject.addProperty("merchant", merchantId);


                Call<BaseResponse<PlaceOrder>> call = request.placeOrder(placeOrderJsonObject);
                call.enqueue(new ApiCallback<PlaceOrder>(PlaceOrderActivity.this) {
                    @Override
                    public void onSucess(PlaceOrder userResponse) {
                        hideProgressBar();
                        showToast("Your order placed successfully.");
                        launchActivity(OrdersActivity.class);
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
    }

    private void getDataFromBundle() {
        if (getIntent().getExtras() != null) {
            merchantId = getIntent().getExtras().getString("merchantId", "");
//            showToast("merchantId : " + merchantId);
        }
    }

    public void takeOrderPhoto() {
        CameraDialog dialog = new CameraDialog(this);
        dialog.setListner(new ICameraCallback() {
            @Override
            public void pickCamera() {
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstant.BUNDLE_KEY.IS_FROM_CAMERA, 0);
                Intent i = new Intent(PlaceOrderActivity.this, CameraActivity.class);
                i.putExtras(bundle);
                startActivityForResult(i, AppConstant.REQ.IMAGE_CAMERA);
            }

            @Override
            public void pickPhoto() {
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstant.BUNDLE_KEY.IS_FROM_CAMERA, 1);
                Intent i = new Intent(PlaceOrderActivity.this, CameraActivity.class);
                i.putExtras(bundle);
                startActivityForResult(i, AppConstant.REQ.IMAGE_GALLERY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AppConstant.REQ.IMAGE_CAMERA:
                case AppConstant.REQ.IMAGE_GALLERY:
                    finalFile = (File) data.getExtras().getSerializable(AppConstant.FILE_PATH_IMAGE);
                    if (finalFile != null) {
// TODO upload image on s3 from here
//                        ui.ivCameraIcon.setVisibility(View.GONE);
//                        uploadImageAPI(finalFile);

                        Bitmap myBitmap = BitmapFactory.decodeFile(finalFile.getAbsolutePath());
                        placeOrderImageView.setImageBitmap(myBitmap);

                    }
                    break;

            }
        }
    }

}

package e2b.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.e2b.R;
import com.e2b.activity.CameraActivity;
import com.e2b.fragments.BaseFragment;
import com.e2b.listener.ICameraCallback;
import com.e2b.listener.IImageUploadOnS3Listner;
import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.Error;
import com.e2b.model.response.PlaceOrder;
import com.e2b.utils.AppConstant;
import com.e2b.utils.CameraDialog;
import com.e2b.utils.DialogUtils;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.Date;

import com.e2b.api.ApiCallback;
import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;
import retrofit2.Call;

public class PlaceOrderActivity extends ConsumerBaseActivity {

    ImageView placeOrderImageView;
    Button playButton;
    Button takePhotoButton;
    Button takeAudioButton;
    Button placeOrderButton;

    private BaseFragment currentFragment;
    private String merchantId;
    private File finalImageFile;
    private File finalAudioFile;
    public static String FileNameArg = "arg_filename";

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 500;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

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
                takeOrderPhoto();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlaceOrderActivity.this, AudioPlaybackActivity.class);
                Log.d(TAG, "audio file name : "+ finalAudioFile.getPath());
                i.putExtra(FileNameArg, finalAudioFile.getPath());
                startActivityForResult(i, 0);
            }
        });

        takeAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(PlaceOrderActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PlaceOrderActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
                }else{
                startRecording();
                }

            }
        });

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showToast("place order");
                if(finalImageFile == null && finalAudioFile == null){
                    DialogUtils.showDialog(PlaceOrderActivity.this, "Please take photo or audio file for order.");
                    return;
                }
                if(finalImageFile != null) {
                    showProgressBar();
                    uploadImage("" + (new Date().getTime()), finalImageFile.getPath(), new IImageUploadOnS3Listner() {
                        @Override
                        public void uploaded(String imagepath) {
                            if (imagepath != null) {
                                IApiRequest request = ApiClient.getRequest();

                                JsonObject placeOrderJsonObject = new JsonObject();
                                placeOrderJsonObject.addProperty("orderImg", imagepath);
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
                            } else {
                                hideProgressBar();
                                showToast("Please try again");
                            }
                        }
                    });
                }else{
                    showProgressBar();
                    uploadAudio("" + (new Date().getTime()), finalAudioFile.getPath(), new IImageUploadOnS3Listner() {
                        @Override
                        public void uploaded(String audiopath) {
                            if (audiopath != null) {
                                IApiRequest request = ApiClient.getRequest();

                                JsonObject placeOrderJsonObject = new JsonObject();
                                placeOrderJsonObject.addProperty("orderImg", "");
                                placeOrderJsonObject.addProperty("orderAudio", audiopath);
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
                            } else {
                                hideProgressBar();
                                showToast("Please try again");
                            }
                        }
                    });
                }
            }
        });
    }

    private void startRecording() {
        Intent intent = new Intent(PlaceOrderActivity.this, AudioRecordingActivity.class);
        startActivityForResult(intent, AppConstant.REQ.IMAGE_AUDIO);
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
                    finalImageFile = (File) data.getExtras().getSerializable(AppConstant.FILE_PATH_IMAGE);
                    if (finalImageFile != null) {
                        Log.d(TAG, "image path : " + finalImageFile);
                            // upload image on s3 from here
                        // ui.ivCameraIcon.setVisibility(View.GONE);
                        Bitmap myBitmap = BitmapFactory.decodeFile(finalImageFile.getAbsolutePath());
                        placeOrderImageView.setImageBitmap(myBitmap);
                        finalAudioFile = null;
                        playButton.setVisibility(View.GONE);
                    }
                    break;
                case AppConstant.REQ.IMAGE_AUDIO:
                    if(data.getExtras() != null) {
                        String audioFilePath = data.getExtras().getString(AppConstant.FILE_PATH_AUDIO);
                        if(audioFilePath != null){
                            Log.d(TAG, "audio file path : " + audioFilePath);
                            finalAudioFile = new File(audioFilePath);
                            finalImageFile = null;
                            playButton.setVisibility(View.VISIBLE);
                        }

                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(permissionToRecordAccepted){
                    startRecording();
                }else{
                    ActivityCompat.requestPermissions(PlaceOrderActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
                }
                break;
        }
    }
}

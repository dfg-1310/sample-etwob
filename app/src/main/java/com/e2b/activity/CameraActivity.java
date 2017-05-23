package com.e2b.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


import com.e2b.utils.AppConstant;
import com.e2b.utils.BitmapUtils;

import java.io.File;

public class CameraActivity extends BaseActivity {
    private Uri mCapturedImageURI;
    private String imageFinalPath;
    private File galleryPathFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
    }

    private void getIntentData() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            int isCamera = getIntent().getExtras().getInt(AppConstant.BUNDLE_KEY.IS_FROM_CAMERA);
            try {
                captureMedia(isCamera);
            } catch (Exception e) {
                showToast("Camera is not Supported");
                finish();
            }
        }
    }


    private void captureMedia(int isCamera) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isCamera == 0) {
                checkCameraPerimissionforApi23();
            } else if (isCamera == 1) {
                checkGalleryPerimissionforApi23();
            } else {

            }
        } else {
            if (isCamera == 0) {
                mCapturedImageURI = BitmapUtils.onOpenCameraImage(CameraActivity.this);
            } else if (isCamera == 1) {
                mCapturedImageURI = BitmapUtils.onOpenGallary(CameraActivity.this);
            } else {

            }
        }
    }


    private void checkCameraPerimissionforApi23() {
        String[] whatPermission = {AppConstant.PERMISSION.CAMERA, AppConstant.PERMISSION.WRITE_EXTERNAL};
        if (ContextCompat.checkSelfPermission(CameraActivity.this, whatPermission[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    whatPermission,
                    AppConstant.REQUEST_CODE.CAMERA_PERMISSION);
        } else {
            mCapturedImageURI = BitmapUtils.onOpenCameraImage(CameraActivity.this);
        }
    }

    private void checkGalleryPerimissionforApi23() {
        String whatPermission = AppConstant.PERMISSION.WRITE_EXTERNAL;
        if (ContextCompat.checkSelfPermission(CameraActivity.this, whatPermission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{whatPermission},
                    AppConstant.REQUEST_CODE.WRITE_EXTERNAL_PERMISSION);
        } else {
            mCapturedImageURI = BitmapUtils.onOpenGallary(CameraActivity.this);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppConstant.REQUEST_CODE.CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    mCapturedImageURI = BitmapUtils.onOpenCameraImage(CameraActivity.this);
                }
                break;
            case AppConstant.REQUEST_CODE.WRITE_EXTERNAL_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    mCapturedImageURI = BitmapUtils.onOpenGallary(CameraActivity.this);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AppConstant.REQUEST_CODE.CAPTURE_IMAGE:
                    imageFinalPath = BitmapUtils.getPath(this, data, mCapturedImageURI);
                    if (imageFinalPath == null) {
                        Uri uriImage = data.getData();
                        galleryPathFile = new File(uriImage.getPath());
                    } else {
                        File imgFile = new File(imageFinalPath);
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        if (myBitmap != null) {
                            Bitmap bitmap = BitmapUtils.imageOreintationValidator(BitmapUtils.getScaledBitmap(myBitmap), imageFinalPath);
                            galleryPathFile = BitmapUtils.saveBitmap(bitmap);
                        } else {
                            galleryPathFile = new File(imageFinalPath);
                        }
                    }
                    break;
                case AppConstant.REQUEST_CODE.GALLARY_IMAGE:
                    imageFinalPath = BitmapUtils.getPath(this, data, mCapturedImageURI);
                    if (imageFinalPath == null) {
                        Uri uriImage = data.getData();
                        galleryPathFile = new File(uriImage.getPath());
                    } else {
                        galleryPathFile = new File(imageFinalPath);
                    }
                    break;
                default:
                    break;
            }
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstant.FILE_PATH_IMAGE, galleryPathFile);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

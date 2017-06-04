package com.e2b.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.e2b.R;
import com.e2b.fragments.BaseFragment;
import com.e2b.listener.IImageUploadOnS3Listner;
import com.e2b.utils.AppConstant;
import com.e2b.utils.AppUtils;

import e2bmerchant.model.response.MerchantUserResponse;
import e2bmerchant.utils.MerchantPreferenceKeeper;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener, ISaveUserInfo {

    public static final String TAG = BaseActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    public String fragmentTag;
    private BaseFragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void launchActivity(Class<? extends BaseActivity> activityClass) {
        if (activityClass != null) {
            launchActivity(activityClass, null);
        }
    }

    public void launchActivity(Class<? extends BaseActivity> activityClass, Bundle bundle) {
        Intent intent = new Intent(this, activityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void launchActivityMain(Class<? extends BaseActivity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    public void showToast(String message) {
        if (!TextUtils.isEmpty(message))
            Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
    }


    public void replaceFragment(int containerId, BaseFragment fragment) {
        replaceFragment(containerId, fragment, null);
    }

    public void replaceFragment(int containerId, BaseFragment fragment, Bundle bundle) {
        replaceFragment(containerId, fragment, false, bundle);
    }

    public void replaceFragment(int containerId, BaseFragment fragment, boolean isNextFragmentNeedsTobeAdded) {
        replaceFragment(containerId, fragment, isNextFragmentNeedsTobeAdded, null);
    }

    public void replaceFragment(int containerId, BaseFragment fragment, boolean isNextFragmentNeedsTobeAdded, Bundle bundle) {
        replaceFragment(containerId, fragment, 0, 0, 0, 0, isNextFragmentNeedsTobeAdded, bundle);
    }

    public void removeFragment(int containerId, Fragment fragment) {

    }

    public void replaceFragment(int containerId, BaseFragment fragment, int enter, int exit, int enterPop, int exitPop, boolean isNextFragmentNeedsTobeAdded, Bundle bundle) {
        FragmentManager manager = getSupportFragmentManager();
        if (manager != null) {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            if (fragment == null) {
                return;
            }
            fragmentTransaction.setCustomAnimations(enter, exit, enterPop, exitPop);
            fragmentTag = fragment.getClass().getSimpleName();

            if (isNextFragmentNeedsTobeAdded) {
                fragmentTransaction.addToBackStack(fragmentTag);
            }

            if (bundle != null) {
                fragment.setArguments(bundle);
            }

            if (fragment.isAdded()) {
                return;
            }

            currentFragment = fragment;

            fragmentTransaction.replace(containerId, fragment, fragmentTag);
            fragmentTransaction.commit();

        }
    }

    public void showProgressBar() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();
    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public void hideProgressBar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(android.R.id.content)
                        .getRootView().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


//    public void setStatusBarColorRuntime(int colorStatusBar) {
//        Window window = this.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.setStatusBarColor(colorStatusBar);
//        }
//    }

    public int getColor(Context context, int color) {
        return ContextCompat.getColor(context, color);
    }

    @Override
    public void onClick(View v) {

    }

    public void loadCircleImageGlide(String url, final ImageView imageView) {
        Glide.with(this).
                load(url)
                .placeholder(R.drawable.user_pic)
                .error(R.drawable.user_pic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(imageView);
    }


    public Uri takePhoto() {
        Uri mCapturedImageURI = null;
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.TITLE, getPackageName());
            mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            startActivityForResult(cameraIntent, AppConstant.TAKE_PICTURE);

        } catch (ActivityNotFoundException anfe) {
            showToast(getString(R.string.unable_to_access_camera));
        }

        return mCapturedImageURI;
    }

    // Select picture from gallery

    public Uri choosePhoto() {
        Uri mCapturedImageURI = null;
        if (Environment.getExternalStorageState().equals("mounted")) {

            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.TITLE, getPackageName());
            mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageIntent.setType("image/*");
            AppUtils.setCropImage(pickImageIntent, mCapturedImageURI);
            pickImageIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(pickImageIntent, AppConstant.CHOOSE_PICTURE);
        }
        return mCapturedImageURI;
    }


    @Override
    public void saveUserInfo(final MerchantUserResponse userResponse) {
        final MerchantPreferenceKeeper keeper = MerchantPreferenceKeeper.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (keeper != null) {
                    keeper.saveUser(userResponse);
                }
            }
        }).start();
    }

    public void loadImageGlide(final String url, final ImageView imageView) {

        Glide.with(getApplicationContext()).load(url).asBitmap().centerCrop().placeholder(R.drawable.banner).
                error(R.drawable.banner).diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;

                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        super.onResourceReady(bitmap, anim);
                        if (imageView.getContext() != null)
                            Glide.with(getApplicationContext()).load(url).centerCrop().into(imageView);
                    }
                });

    }


    public void uploadImage(String fineName, String filePath, IImageUploadOnS3Listner listner) {
        new ImageUploadAsync(fineName, filePath, listner).execute();
    }

    public void uploadAudio(String fineName, String filePath, IImageUploadOnS3Listner listner) {
        new FileUploadAsync(fineName, filePath, listner).execute();
    }

    class ImageUploadAsync extends AsyncTask {
        String fileName;
        String filePath;
        IImageUploadOnS3Listner listner;

        ImageUploadAsync(String fileName, String filePath, IImageUploadOnS3Listner listner) {
            this.fileName = fileName;
            this.filePath = filePath;
            this.listner = listner;
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(AppConstant.MY_ACCESS_KEY_ID, AppConstant.MY_SECRET_KEY));
//            s3Client.createBucket(AppConstant.MY_PICTURE_BUCKET);
                PutObjectRequest por = new PutObjectRequest(AppConstant.MY_PICTURE_BUCKET, fileName, new java.io.File(filePath));
                s3Client.putObject(por);

                ResponseHeaderOverrides override = new ResponseHeaderOverrides();
                override.setContentType("image/jpeg");
                GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(AppConstant.MY_PICTURE_BUCKET, fileName);
                urlRequest.setResponseHeaders(override);

                URL url = s3Client.generatePresignedUrl(urlRequest);

                Log.d("Uploaded Image Url : ", url.toString());
                return url.toString();
            }catch (Exception e){
                e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object imagePath) {

            if(imagePath != null && imagePath instanceof String){
                listner.uploaded((String) imagePath);
            }else{
                listner.uploaded(null);
            }
        }
    }

    class FileUploadAsync extends AsyncTask {
        String fileName;
        String filePath;
        IImageUploadOnS3Listner listner;

        FileUploadAsync(String fileName, String filePath, IImageUploadOnS3Listner listner) {
            this.fileName = fileName;
            this.filePath = filePath;
            this.listner = listner;
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                ClientConfiguration configuration = new ClientConfiguration();
                configuration.setMaxErrorRetry(3);
                configuration.setConnectionTimeout(5*60*1000);
                configuration.setSocketTimeout(5*60*1000);
                configuration.setProtocol(Protocol.HTTP);

                AmazonS3 s3Client = new AmazonS3Client(new BasicAWSCredentials(AppConstant.MY_ACCESS_KEY_ID, AppConstant.MY_SECRET_KEY), configuration);

                // Create a list of UploadPartResponse objects. You get one of these for
               // each part upload.
                List<PartETag> partETags = new ArrayList<PartETag>();

                // Step 1: Initialize.
                InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(
                        AppConstant.MY_PICTURE_BUCKET, fileName);
                InitiateMultipartUploadResult initResponse =
                        s3Client.initiateMultipartUpload(initRequest);

                File file = new File(filePath);
                long contentLength = file.length();
                long partSize = 4 * 1024 * 1024; // Set part size to 5 MB.

                try {
                    // Step 2: Upload parts.
                    long filePosition = 0;
                    for (int i = 1; filePosition < contentLength; i++) {
                        // Last part can be less than 5 MB. Adjust part size.
                        partSize = Math.min(partSize, (contentLength - filePosition));

                        // Create request to upload a part.
                        UploadPartRequest uploadRequest = new UploadPartRequest()
                                .withBucketName(AppConstant.MY_PICTURE_BUCKET).withKey(fileName)
                                .withUploadId(initResponse.getUploadId()).withPartNumber(i)
                                .withFileOffset(filePosition)
                                .withFile(file)
                                .withPartSize(partSize);

                        // Upload part and add response to our list.
                        partETags.add(s3Client.uploadPart(uploadRequest).getPartETag());

                        filePosition += partSize;
                    }

                    // Step 3: Complete.
                    CompleteMultipartUploadRequest compRequest = new
                            CompleteMultipartUploadRequest(AppConstant.MY_PICTURE_BUCKET,
                            fileName,
                            initResponse.getUploadId(),
                            partETags);

                    CompleteMultipartUploadResult completeMultipartUploadResult =  s3Client.completeMultipartUpload(compRequest);

                    ResponseHeaderOverrides override = new ResponseHeaderOverrides();
                    override.setContentType("audio/mpeg");

                    GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(AppConstant.MY_PICTURE_BUCKET, fileName);
                    urlRequest.setResponseHeaders(override);

                    URL url = s3Client.generatePresignedUrl(urlRequest);
                    Log.d(TAG, " CompleteMultipartUploadResult :"+ completeMultipartUploadResult.toString());
                    Log.d(TAG, " url :"+ url.toString());
                    return url.toString();

                } catch (Exception e) {
                    s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
                            AppConstant.MY_PICTURE_BUCKET, fileName, initResponse.getUploadId()));
                    Log.d(TAG, " CompleteMultipartUploadResult Exception :"+ e.getMessage());

                }
            }catch (Exception e){
                e.getMessage();
                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object imagePath) {

            if(imagePath != null && imagePath instanceof String){
                listner.uploaded((String) imagePath);
            }else{
                listner.uploaded(null);
            }
        }
    }

}

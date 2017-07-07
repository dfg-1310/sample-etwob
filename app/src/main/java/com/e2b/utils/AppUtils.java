package com.e2b.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;

import com.e2b.activity.BaseActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Class is used to contain utils method.
 */
public class AppUtils {

    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private static final String TAG = AppUtils.class.getName();

    public static boolean isNetworkAvailable(BaseActivity context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        boolean isnewtwork = (activeNetworkInfo != null && activeNetworkInfo.isConnected());
        if (!isnewtwork) {
            //show dialog
        }
        return isnewtwork;
    }

    public static Typeface findTypeface(Context context, String initPath,
                                        String typeface) {
        AssetManager assets = context.getAssets();
        return Typeface.createFromAsset(assets, (initPath + File.separator)
                + typeface);
    }

    public static void key(Activity activity) {
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void setCropImage(Intent intent, Uri mCapturedImageURI) {
        intent.putExtra("crop", "true");
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    }


    // Captured or picked photo's image path
    public static String getImagePath(Context context, Intent intent, Uri mCapturedImageURI) {
        Cursor cursor = null;
        String fileImagePath = null;
        try {
            Uri selectedImage;
            if (intent == null) {
                selectedImage = mCapturedImageURI;
            } else {
                if (intent.getData() == null) {
                    selectedImage = mCapturedImageURI;
                } else {
                    selectedImage = intent.getData();
                }
            }
            String[] filePathColumn = {MediaStore.MediaColumns.DATA};
            cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                fileImagePath = cursor.getString(columnIndex);
                cursor.close();
            }
        } catch (Exception ignored) {

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return fileImagePath;
    }

    public static String getFormatedDateStringInProfileFragment(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String date = formatter.format(cal.getTime());
        return date;
    }

    public static Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
    }


    public static int getInPixel(Activity activity, int value) {
        Resources r = activity.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics());
        return px;
    }

    public static String getES_SHA_256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public static boolean checkPlayServiceExits(Context ctx) {
        Log.d(TAG, "checkPlayServiceExits() called with: " + "");
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(ctx);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Log.d(TAG, "checkPlayServiceExits: result code" + resultCode);
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) ctx, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }


    public static String encodeToBase64(String text) {
        // Sending side
        byte[] data = new byte[0];
        String base64 = null;
        try {
            data = text.getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.NO_WRAP | Base64.URL_SAFE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return base64;
    }


    public static String decodeToBase64(String base64) {
        // Receiving side
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        String text = null;
        try {
            text = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  text;
    }


    public static String getUniqueDeviceId(Activity activity){
        String deviceId;
        final TelephonyManager mTelephony = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null) {
            deviceId = mTelephony.getDeviceId();
        }
        else {
            deviceId = Settings.Secure.getString(
                    activity.getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }

        return deviceId;
    }
}

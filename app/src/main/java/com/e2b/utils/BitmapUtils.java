package com.e2b.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class is used to contain bitmap utils methods - getPath, getBitmap , change orientation.
 */

public class BitmapUtils {
    private static final String TAG = BitmapUtils.class.getSimpleName();

    // get sd card path of image
    public static String getPath(Context context, Intent intent, Uri mCapturedImageURI) {
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


    public static Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }

    public static Uri onOpenCameraImage(Activity context) {
        Uri mCapturedImageURI = null;
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.TITLE, context.getPackageName());
            mCapturedImageURI = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

            context.startActivityForResult(cameraIntent, AppConstant.REQUEST_CODE.CAPTURE_IMAGE);

        } catch (ActivityNotFoundException e) {
            Log.d(TAG, "onOpenCameraImage() called with: context = [" + context + "]");
        }

        return mCapturedImageURI;
    }

    public static Uri onOpenGallary(Activity context) {
        Uri mCapturedImageURI = null;
        if (Environment.getExternalStorageState().equals("mounted")) {

            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.TITLE, context.getPackageName());
            mCapturedImageURI = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageIntent.setType("image/*");
//            BitmapUtils.setCropImage(pickImageIntent, mCapturedImageURI);
            pickImageIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            context.startActivityForResult(pickImageIntent, AppConstant.REQUEST_CODE.GALLARY_IMAGE);
        }

        return mCapturedImageURI;
    }

    public static Uri getImageUri(Activity context) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.TITLE, context.getPackageName());
        return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
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

    public static Bitmap getScaledBitmap(Bitmap myBitmap) {
        int height = myBitmap.getHeight();
        int width = myBitmap.getWidth();
        if (height > 2048) {
            width = width * 2048 / height;
            height = 2048;
        }
        if (width > 2048) {
            height = height * 2048 / width;
            width = 2048;
        }
        if (width == 2048 || height == 2048) {
            return Bitmap.createScaledBitmap(myBitmap, width, height, false);
        }
        return myBitmap;
    }


    public static File saveBitmap(Bitmap bitmap) {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/crowdsorz_image";
        File dir = new File(file_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String format = new SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault()).format(new Date());
        File file = new File(dir, format + ".png");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {

        }

        return file;
    }
}

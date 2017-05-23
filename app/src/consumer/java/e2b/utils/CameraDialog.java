package e2b.utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.e2b.R;
import com.e2b.activity.BaseActivity;

import e2b.intrface.ICameraCallback;


public class CameraDialog {

    private View view;
    private Activity activity;
    private AlertDialog.Builder builder;
    private AlertDialog pickerDialog;


    public CameraDialog(BaseActivity activity) {
        this.activity = activity;
        this.view = activity.getLayoutInflater().inflate(R.layout.dialog_camera, null);
        build();
    }

    private ICameraCallback listner;

    public void setListner(ICameraCallback listner) {
        this.listner = listner;
    }

    private void build() {

        builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setTitle(activity.getString(R.string.app_name));

        view.findViewById(R.id.tv_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listner != null) {
                    pickerDialog.dismiss();
                    listner.pickCamera();
                }
            }
        });
        view.findViewById(R.id.tv_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listner != null) {
                    pickerDialog.dismiss();
                    listner.pickPhoto();
                }
            }
        });
        pickerDialog = builder.create();
        pickerDialog.show();
    }
}
package e2b.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.e2b.activity.BaseActivity;
import com.e2b.model.request.Coordinate;


public class BaseFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = BaseFragment.class.getSimpleName() ;
    protected BaseActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachContext(context);
        Log.i(TAG, "onAttach : "+this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onAttachContext(activity);
    }

    private void onAttachContext(Context context) {
        if (context instanceof Activity)
            activity = (BaseActivity) context;
    }
    @Override
    public void onClick(View view) {

    }

    public void getData(Object object) {

    }

    Coordinate getLocationCoordinate() {
        return new Coordinate();
    }


}

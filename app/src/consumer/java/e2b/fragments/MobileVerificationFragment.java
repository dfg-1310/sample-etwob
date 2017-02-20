package e2b.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.e2b.R;
import com.e2b.activity.BaseActivity;
import com.e2b.api.ApiCallback;
import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;
import com.e2b.listener.IDialogListener;
import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.Error;
import com.e2b.utils.DialogUtils;
import com.e2b.views.CustomTextView;
import com.google.gson.JsonObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import e2b.activity.HomeActivity;
import e2b.enums.EScreenType;
import e2b.model.response.UserResponse;
import e2b.utils.ConsumerPreferenceKeeper;
import retrofit2.Call;

/**
 * Created by gaurav on 11/2/17.
 */

public class MobileVerificationFragment extends BaseFragment {

    private static final String TAG = MobileVerificationFragment.class.getSimpleName();

    @Bind(R.id.et_mobile_number)
    EditText etMobileNumber;

    @Bind(R.id.tv_why)
    CustomTextView tv_why;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mobile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void clearData() {
        etMobileNumber.setText("");
    }

    @OnClick(R.id.tv_why)
    public void showWhyPopUp() {
        DialogUtils.showDialogDoubleButton(getActivity(), getString(R.string.why_dialog_text), "OK,PROCEED.", "Cancel", new IDialogListener() {
            @Override
            public void positiveClick() {
                // go to api call
                signIn();

            }

            @Override
            public void negativeClick() {
                // do nothing
            }
        });

    }


    public void signIn() {
        String mobileNumber = etMobileNumber.getText().toString().trim();

        if (!openDialogSignin(activity, mobileNumber)) {
            return;
        }

        ConsumerPreferenceKeeper.getInstance().setAccessToken("");
//        activity.showProgressBar();
        IApiRequest request = ApiClient.getRequest();

        mobileNumber  = "+91-"+mobileNumber;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile",mobileNumber);
        Call<BaseResponse<UserResponse>> call = request.consumer(jsonObject);
        call.enqueue(new ApiCallback<UserResponse>(activity) {
            @Override
            public void onSucess(UserResponse userResponse) {
                activity.hideProgressBar();
                if(userResponse.isNewUser()){
                    activity.replaceFragment(R.id.container_auth, FragmentFactory.getInstance().getFragment(EScreenType.OTP_CONFRM));
                }else{
                    activity.launchActivity(HomeActivity.class);
                    activity.finish();
                }
                clearData();
                Log.d(TAG, "[USER RESPONSE : ]"+userResponse.toString());
            }

            @Override
            public void onError(Error error) {
                activity.hideProgressBar();
                activity.showToast(error.getMsg());
                Log.d(TAG, error.getMsg());
            }


        });
    }

    public boolean openDialogSignin(BaseActivity activity, String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            DialogUtils.showDialog(activity, activity.getString(R.string.enter_mobile_number));
            return false;
        }
        return true;
    }


}



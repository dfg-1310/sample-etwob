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
import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.Error;
import com.e2b.utils.DialogUtils;
import com.e2b.views.CustomTextView;
import com.google.gson.JsonObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import e2b.enums.EScreenType;
import e2b.model.response.VerifiedOTPResponse;
import e2b.utils.ConsumerPreferenceKeeper;
import retrofit2.Call;

/**
 * Created by gaurav on 11/2/17.
 */

public class OTPConfirmFragment extends BaseFragment {

    private static final String TAG = OTPConfirmFragment.class.getSimpleName();

    @Bind(R.id.et_otp_code)
    EditText etOTPCode;

    @Bind(R.id.tv_verify_otp)
    CustomTextView tv_verify_otp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp_verify, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void clearData() {
        etOTPCode.setText("");
    }

    @OnClick(R.id.tv_verify_otp)
    public void verifyOTP() {
       // need to api call for verify otp
        verifyOtp();
    }

    public void verifyOtp() {
        String otp = etOTPCode.getText().toString().trim();

        if (!openDialogSignin(activity, otp)) {
            return;
        }

        ConsumerPreferenceKeeper.getInstance().setAccessToken("");
        activity.showProgressBar();
        IApiRequest request = ApiClient.getRequest();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile", "+91-8447628001");
        jsonObject.addProperty("authCode", otp);


        Call<BaseResponse<VerifiedOTPResponse>> call = request.verifyotp(jsonObject);
        call.enqueue(new ApiCallback<VerifiedOTPResponse>(activity) {
            @Override
            public void onSucess(VerifiedOTPResponse userResponse) {
//                ConsumerPreferenceKeeper.getInstance().setIsLogin(true);
//                ConsumerPreferenceKeeper.getInstance().setAccessToken(userResponse.getUser().getAccessToken());
//                ConsumerPreferenceKeeper.getInstance().saveUser(userResponse.getUser());
                  activity.replaceFragment(R.id.container_auth, FragmentFactory.getInstance().getFragment(EScreenType.SIGN_UP_SCREEN));
                activity.hideProgressBar();
//                activity.launchActivity(HomeActivity.class);
                clearData();
            }

            @Override
            public void onError(Error error) {
                activity.hideProgressBar();
                activity.showToast(error.getMsg());
                Log.d(TAG, error.getMsg());
            }


        });
    }

    public boolean openDialogSignin(BaseActivity activity, String otp) {
        if (TextUtils.isEmpty(otp)) {
            DialogUtils.showDialog(activity, activity.getString(R.string.enter_otp_code));
            return false;
        }
//        if (!Validator.isValidEmail(email)) {
//            DialogUtils.showDialog(activity, activity.getString(R.string.enter_email_validation));
//            return false;
//        }
//
//        if (TextUtils.isEmpty(password)) {
//            DialogUtils.showDialog(activity, activity.getString(R.string.please_enter_password));
//            etPasscode.setText("");
//            return false;
//        }
//        if (password.length() <= AppConstant.PASSWARD_LENGTH) {
//            DialogUtils.showDialog(activity, activity.getString(R.string.passwordValidation));
//            etPasscode.setText("");
//            return false;
//        }
        return true;
    }


//    @OnClick(R.id.tv_have_no_account)
//    public void goToSignUpScreenClick() {
//        activity.replaceFragment(R.id.container_auth, FragmentFactory.getInstance().getFragment(EScreenType.SIGN_UP_SCREEN));
//    }

}



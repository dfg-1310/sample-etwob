package e2b.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.e2b.R;
import com.e2b.api.ApiCallback;
import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;
import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.Error;
import com.e2b.utils.AppConstant;
import com.e2b.utils.DialogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import e2b.activity.HomeActivity;
import e2b.enums.EScreenType;
import e2b.model.response.UserResponse;
import e2b.utils.ConsumerPreferenceKeeper;
import retrofit2.Call;

/**
 * Created by gaurav on 6/2/17.
 */

public class ProfileFragment extends BaseFragment {

    private String TAG = SignUpFragment.class.getSimpleName();
    private View view;

//    @Bind(R.id.et_signup_mobile_number)
//    EditText etMobileNumber;

    @Bind(R.id.et_signup_full_name)
    EditText etFullName;

//    @Bind(R.id.et_signup_password)
//    EditText etPassword;

    @Bind(R.id.et_signup_address1)
    EditText etSignUpAddress1;

    @Bind(R.id.et_signup_address2)
    EditText etSignUpAddress2;

//    @Bind(R.id.tv_signup_terms_n_conditions)
//    TextView termsNCondTextView;

    private String mobileNumber;
    private String fullName;
    private String address1;
    private String address2;
    private String password;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.tv_save)
    public void signUpClick() {
        fullName = etFullName.getText().toString().trim();
        address1 = etSignUpAddress1.getText().toString().trim();
        address2 = etSignUpAddress2.getText().toString().trim();

        if (signUpValidation()) {
            signUpApi();
        }

    }

    private void signUpApi() {
//        ConsumerPreferenceKeeper.getInstance().setAccessToken("");
//        activity.showProgressBar();
//        IApiRequest request = ApiClient.getRequest();
//        Call<BaseResponse<UserResponse>> call = request.profileSetup(mobileNumber, fullName, password, address1+address2);
//        call.enqueue(new ApiCallback<UserResponse>(activity) {
//            @Override
//            public void onSucess(UserResponse userResponse) {
//                activity.hideProgressBar();
//                activity.launchActivity(HomeActivity.class);
//                clearData();
//            }
//
//            @Override
//            public void onError(Error msg) {
//                activity.showToast(msg.getMsg());
//                Log.d(TAG + "signup api msg", msg.getMsg());
//                activity.hideProgressBar();
//
//            }
//
//        });

    }

    private void clearData() {
        etSignUpAddress1.setText("");
        etFullName.setText("");
    }

    private boolean signUpValidation() {

        if (TextUtils.isEmpty(fullName)) {
            DialogUtils.showDialog(activity, activity.getString(R.string.please_enter_userName));
            return false;
        }

        if (!TextUtils.isEmpty(mobileNumber)) {
            DialogUtils.showDialog(activity, activity.getString(R.string.enter_mobile_number));
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            DialogUtils.showDialog(activity, activity.getString(R.string.please_enter_password));
            return false;
        }
        if (password.length() <= AppConstant.PASSWARD_LENGTH) {
            DialogUtils.showDialog(activity, activity.getString(R.string.passwordValidation));
            return false;
        }

        if (TextUtils.isEmpty(address1) && TextUtils.isEmpty(address2)) {
            DialogUtils.showDialog(activity, activity.getString(R.string.please_enter_address));
            return false;
        }

        return true;
    }


}

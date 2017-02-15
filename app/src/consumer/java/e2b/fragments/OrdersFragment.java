package e2b.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class OrdersFragment extends BaseFragment {

    private static final String TAG = OrdersFragment.class.getSimpleName();
    @Bind(R.id.et_mobile_number)
    EditText etMobileNumber;

    @Bind(R.id.et_passcode)
    EditText etPasscode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void clearData() {
        etMobileNumber.setText("");
        etPasscode.setText("");
    }

    @OnClick(R.id.tv_sign_in)
    public void signInClick() {
        String email = etMobileNumber.getText().toString().trim();
        String password = etPasscode.getText().toString().trim();

        if (!openDialogSignin(activity, email, password)) {
            return;
        }

        ConsumerPreferenceKeeper.getInstance().setAccessToken("");
        activity.showProgressBar();
        IApiRequest request = ApiClient.getRequest();

        Call<BaseResponse<UserResponse>> call = request.signInAPI(email, password);
        call.enqueue(new ApiCallback<UserResponse>(activity) {
            @Override
            public void onSucess(UserResponse userResponse) {
//                ConsumerPreferenceKeeper.getInstance().setIsLogin(true);
//                ConsumerPreferenceKeeper.getInstance().setAccessToken(userResponse.getUser().getAccessToken());
//                ConsumerPreferenceKeeper.getInstance().saveUser(userResponse.getUser());
                activity.hideProgressBar();
                activity.launchActivity(HomeActivity.class);
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
    public  boolean openDialogSignin(BaseActivity activity, String email, String password) {
//        if (TextUtils.isEmpty(email)) {
//            DialogUtils.showDialog(activity, activity.getString(R.string.enter_email));
//            return false;
//        }
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


    @OnClick(R.id.tv_have_no_account)
    public void goToSignUpScreenClick() {
        activity.replaceFragment(R.id.container_auth, FragmentFactory.getInstance().getFragment(EScreenType.SIGN_UP_SCREEN));
    }

}

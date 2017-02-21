package e2b.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.e2b.R;
import com.e2b.utils.DialogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaurav on 6/2/17.
 */

public class ProfileFragment extends BaseFragment {

    private String TAG = ProfileSetupFragment.class.getSimpleName();
    private View view;

    @Bind(R.id.et_profile_full_name)
    EditText etFullName;

    @Bind(R.id.et_profile_address1)
    EditText etSignUpAddress1;

    @Bind(R.id.et_profile_address2)
    EditText etSignUpAddress2;

    private String fullName;
    private String address1;
    private String address2;

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
    public void saveProfile() {
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

        if (TextUtils.isEmpty(address1) && TextUtils.isEmpty(address2)) {
            DialogUtils.showDialog(activity, activity.getString(R.string.please_enter_address));
            return false;
        }

        return true;
    }


}

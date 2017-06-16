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
import com.e2b.api.ApiCallback;
import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;
import com.e2b.fragments.BaseFragment;
import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.Error;
import com.e2b.utils.DialogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import e2b.activity.AuthActivity;
import e2b.activity.HelpActivity;
import e2b.model.request.ProfileSetup;
import e2b.model.response.UserResponse;
import retrofit2.Call;


public class ProfileSetupFragment extends BaseFragment {
    private String TAG = ProfileSetupFragment.class.getSimpleName();
    private View view;

    @Bind(R.id.et_signup_full_name)
    EditText etFullName;

    @Bind(R.id.et_signup_address1)
    EditText etSignUpAddress1;

//    @Bind(R.id.et_signup_address2)
//    EditText etSignUpAddress2;

    private String fullName;
    private String address1;
//    private String address2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.tv_save)
    public void signUpClick() {
        fullName = etFullName.getText().toString().trim();
        address1 = etSignUpAddress1.getText().toString().trim();
//        address2 = etSignUpAddress2.getText().toString().trim();

        ProfileSetup profileSetup = new ProfileSetup();

        profileSetup.setName(fullName);
        profileSetup.setAddress(address1);
        profileSetup.setCoordinate(getLocationCoordinate());

        if (profileValidation()) {
            profileUpdateApi(profileSetup);
        }
    }


    private void profileUpdateApi(ProfileSetup profileSetup) {
        activity.showProgressBar();
        IApiRequest request = ApiClient.getRequest();
        Call<BaseResponse<UserResponse>> call = request.profileSetup(((AuthActivity)activity).userResponse.getConsumer(), profileSetup.toJsonObject());
        call.enqueue(new ApiCallback<UserResponse>(activity) {
            @Override
            public void onSucess(UserResponse userResponse) {
                clearData();
                activity.hideProgressBar();
                activity.launchActivity(HelpActivity.class);
                activity.finish();
                activity.saveUserInfo(userResponse);
            }

            @Override
            public void onError(Error msg) {
                activity.showToast(msg.getMsg());
                Log.d(TAG + "signup api msg", msg.getMsg());
                activity.hideProgressBar();
            }
        });
    }

    private void clearData() {
        etSignUpAddress1.setText("");
        etFullName.setText("");
    }

    private boolean profileValidation() {

        if (TextUtils.isEmpty(fullName)) {
            DialogUtils.showDialog(activity, activity.getString(R.string.please_enter_userName));
            return false;
        }

        if (TextUtils.isEmpty(address1)) {
            DialogUtils.showDialog(activity, activity.getString(R.string.please_enter_address));
            return false;
        }

        return true;
    }

}


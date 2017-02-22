package e2b.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e2b.R;
import com.e2b.activity.BaseActivity;
import com.e2b.api.ApiCallback;
import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;
import com.e2b.listener.IDialogListener;
import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.Error;
import com.e2b.utils.DialogUtils;
import com.e2b.views.CustomEditText;
import com.e2b.views.CustomTextView;
import com.google.gson.JsonObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import e2b.activity.AuthActivity;
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
    CustomEditText etMobileNumber;

    @Bind(R.id.tv_why)
    CustomTextView tv_why;

    private String lastEntered;
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");
                if(etMobileNumber.getText().toString().length() == 1 && !etMobileNumber.getText().subSequence(0,1).toString().equals("+")){
                    etMobileNumber.setText("+"+etMobileNumber.getText().toString());
                    etMobileNumber.setSelection(etMobileNumber.getText().length());
                }else if(etMobileNumber.getText().toString().length() == 3 && !getLastDiff(lastEntered, new String(s.toString())).equals(" ")){
                    etMobileNumber.setText(etMobileNumber.getText().toString()+" ");
                    etMobileNumber.setSelection(etMobileNumber.getText().length());
                }else if(etMobileNumber.getText().toString().length() == 7 && !getLastDiff(lastEntered, new String(s.toString())).equals(" ")){
                    etMobileNumber.setText(etMobileNumber.getText().toString()+" ");
                    etMobileNumber.setSelection(etMobileNumber.getText().length());
                }else if(etMobileNumber.getText().toString().length() == 11 && !getLastDiff(lastEntered, new String(s.toString())).equals(" ")){
                    etMobileNumber.setText(etMobileNumber.getText().toString()+" ");
                    etMobileNumber.setSelection(etMobileNumber.getText().length());
                }
                lastEntered = etMobileNumber.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private String getLastDiff(String lastEntered, String newEntered) {
        if(lastEntered.length() > newEntered.length()){
          return lastEntered.replace(newEntered,"");
        }else{
            return newEntered.replace(lastEntered, "");
        }
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
        activity.showProgressBar();
        String mobileNumber = etMobileNumber.getText().toString().trim().replace(" ","");
        if (!openDialogSignin(activity, mobileNumber)) {
            return;
        }
        ConsumerPreferenceKeeper.getInstance().setAccessToken("");
        IApiRequest request = ApiClient.getRequest();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile",mobileNumber);
        Call<BaseResponse<UserResponse>> call = request.consumer(jsonObject);
        call.enqueue(new ApiCallback<UserResponse>(activity) {
            @Override
            public void onSucess(UserResponse userResponse) {
                activity.hideProgressBar();

                if(activity != null && activity instanceof AuthActivity){
                    ((AuthActivity)activity).userResponse = userResponse;
                }
                    activity.replaceFragment(R.id.container_auth, FragmentFactory.getInstance().getFragment(EScreenType.OTP_CONFRM));
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



package e2b.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

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
import e2b.activity.AuthActivity;
import e2b.activity.HomeActivity;
import e2b.enums.EScreenType;
import e2b.model.response.UserResponse;
import e2b.model.response.VerifiedOTPResponse;
import e2b.utils.ConsumerPreferenceKeeper;
import retrofit2.Call;

public class OTPConfirmFragment extends BaseFragment {

    private static final String TAG = OTPConfirmFragment.class.getSimpleName();

    @Bind(R.id.et_otp_code)
    EditText etOTPCode;

    @Bind(R.id.tv_verify_otp)
    CustomTextView tv_verify_otp;

    @Bind(R.id.tv_resend_otp)
    CustomTextView tv_resend_otp;

    @Bind(R.id.otp_code_layout)
    LinearLayout otpCodeLayout;

    @Bind(R.id.success_msg_layout)
    LinearLayout successMsgLayout;

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

//        ConsumerPreferenceKeeper.getInstance().setAccessToken("");
        activity.showProgressBar();
        IApiRequest request = ApiClient.getRequest();

        JsonObject jsonObject = new JsonObject();
        if (activity != null && activity instanceof AuthActivity) {
            UserResponse userResponse = ((AuthActivity) activity).userResponse;
            if (userResponse != null && userResponse.getUsername() != null) {
                jsonObject.addProperty("mobile", userResponse.getUsername());
            }
        }
        jsonObject.addProperty("authCode", otp);

        Call<BaseResponse<VerifiedOTPResponse>> call = request.verifyotp(jsonObject);
        call.enqueue(new ApiCallback<VerifiedOTPResponse>(activity) {
            @Override
            public void onSucess(VerifiedOTPResponse response) {
                clearData();
                ConsumerPreferenceKeeper keeper = ConsumerPreferenceKeeper.getInstance();
                keeper.setAccessToken(response.getSession());
                keeper.setUserId(response.getConsumer());
                otpCodeLayout.setVisibility(View.GONE);
                successMsgLayout.setVisibility(View.VISIBLE);
                activity.hideProgressBar();

                if (((AuthActivity) activity).userResponse.isNewUser()) {
                    activity.replaceFragment(R.id.container_auth, FragmentFactory.getInstance().getFragment(EScreenType.PROFILE_SETUP_SCREEN));
                } else {
                    activity.launchActivity(HomeActivity.class);
                    activity.finish();
                }
                ((AuthActivity) activity).saveUserInfo();
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
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupHtmlTest();
    }

    private void setupHtmlTest() {

//        String htmlText = "<body><p>This tutorial "
//                + "explains how to display "
//                + "<strong>HTML </strong>text in android text view.&nbsp;</p>"
//                + "Example from <a href=\"www.ushatek.com\">"
//                + "Ushatek<a>,<a href=\"www.google.com\">"
//                + "Google<a>,<a href=\"Male\">"
//                + "Male<a>,<a href=\"Female\">"
//                + "Female<a></body>";

        String htmlText = "If you do not receive confirmation code in 2 minutes" +
                " <a href=\"www.google.com\"> click here <a> to resend again.";

        CharSequence sequence = Html.fromHtml(htmlText);

        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(),URLSpan.class);


        for (URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }

        tv_resend_otp.setText(strBuilder);
        tv_resend_otp.setMovementMethod(LinkMovementMethod.getInstance());
    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                resendOtp();
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    private void resendOtp() {
        activity.showProgressBar();
        String mobileNumber = ((AuthActivity)activity).userResponse.getUsername();
        if (!openDialogSignin(activity, mobileNumber)) {
            return;
        }
//        ConsumerPreferenceKeeper.getInstance().setAccessToken("");
        IApiRequest request = ApiClient.getRequest();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile",mobileNumber);
        Call<BaseResponse<UserResponse>> call = request.resendOtp(jsonObject);
        call.enqueue(new ApiCallback<UserResponse>(activity) {
            @Override
            public void onSucess(UserResponse userResponse) {
                activity.hideProgressBar();
                activity.showToast("Otp sent successfully.");
            }

            @Override
            public void onError(Error error) {
                activity.hideProgressBar();
                activity.showToast(error.getMsg());
                Log.d(TAG, error.getMsg());
            }
        });

    }
}



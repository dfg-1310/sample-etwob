package e2b.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.e2b.R;
import com.e2b.api.ApiCallback;
import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;
import com.e2b.fragments.BaseFragment;
import com.e2b.model.request.Coordinate;
import com.e2b.model.request.Place;
import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.Error;
import com.e2b.utils.DialogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import e2b.activity.ConsumerBaseActivity;
import e2b.model.request.ProfileSetup;
import e2b.model.response.UserResponse;
import e2b.utils.ConsumerPreferenceKeeper;
import retrofit2.Call;

import static com.e2b.R.id.et_profile_address1;

public class ProfileFragment extends BaseFragment {

    private String TAG = ProfileSetupFragment.class.getSimpleName();
    private View view;

    @Bind(R.id.et_profile_full_name)
    EditText etFullName;

    @Bind(et_profile_address1)
    EditText etProfileAddress;

    @Bind(R.id.et_profile_moileno)
    EditText etProfileMobileNo;

    private String fullName;
    private String address1;

    private Place place;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        ((ConsumerBaseActivity)activity).setHeaderText("Profile");
        ((ConsumerBaseActivity)activity).managebackIconVisiblity(false);

        initListener();
        getUserProfile();
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initListener() {
        etProfileAddress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "event : " + event.getAction());
                if (event.getAction() == 0) {
                    ProfileFragment.this.place = null;
                    etProfileAddress.setText("");

//                    LocationSearchFragment locationSearchFragment = new LocationSearchFragment();
//                    locationSearchFragment.setListener(new LocationSearchFragment.ILocationSearch() {
//                        @Override
//                        public void onComplete(Place place) {
//                            ProfileFragment.this.place = place;
//                            etProfileAddress.setText(place.getDesc());
//                        }
//                    });
//                    activity.replaceFragment(R.id.container_home, locationSearchFragment, true);
                }
                return true;
            }
        });

    }

    private void getUserProfile() {
        ConsumerPreferenceKeeper keeper = ConsumerPreferenceKeeper.getInstance();

        activity.showProgressBar();
        IApiRequest request = ApiClient.getRequest();
        Call<BaseResponse<UserResponse>> call = request.getProfile(keeper.getUserId());
        call.enqueue(new ApiCallback<UserResponse>(activity) {
            @Override
            public void onSucess(UserResponse user) {
                activity.hideProgressBar();
                etFullName.setText(user.getName());
                if(place != null){
                    etProfileAddress.setText(place.getDesc());
                }else{
                    etProfileAddress.setText(user.getAddress());
                }
                etProfileMobileNo.setText(user.getMobile());
            }

            @Override
            public void onError(Error msg) {
                Log.d(TAG + "signup api msg", msg.getMsg());
                activity.hideProgressBar();
                activity.showToast(msg.getMsg());
            }
        });
    }

    @OnClick(R.id.tv_save)
    public void saveProfile() {
        fullName = etFullName.getText().toString().trim();

        ProfileSetup profileSetup = new ProfileSetup();

        profileSetup.setName(fullName);
        if(place != null){
            address1 = place.getDesc();
            Coordinate coordinate = new Coordinate();
            coordinate.setLat(place.getLat());
            coordinate.setLng(place.getLng());
            profileSetup.setCoordinates(coordinate);
            profileSetup.setAddress(address1);
        }else{
            address1 = "";
            profileSetup.setCoordinates(null);
        }

        if (updateProfileValidation()) {
            updateProfileApi(profileSetup);
        }

    }

    private void updateProfileApi(ProfileSetup profileSetup) {
        ConsumerPreferenceKeeper keeper = ConsumerPreferenceKeeper.getInstance();

        activity.showProgressBar();
        IApiRequest request = ApiClient.getRequest();
        Call<BaseResponse<UserResponse>> call = request.profileSetup(keeper.getUserId(), profileSetup.toJsonObject());
        call.enqueue(new ApiCallback<UserResponse>(activity) {
            @Override
            public void onSucess(UserResponse userResponse) {
                activity.hideProgressBar();
                activity.showToast("Profile updated successfully.");
                activity.saveUserInfo(userResponse);
                place = null;
            }

            @Override
            public void onError(Error msg) {
                Log.d(TAG + "signup api msg", msg.getMsg());
                activity.hideProgressBar();
                activity.showToast(msg.getMsg());
            }
        });

    }

    private boolean updateProfileValidation() {

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

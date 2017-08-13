package e2b.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
import com.e2b.model.request.Coordinate;
import com.e2b.model.request.Place;
import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.Error;
import com.e2b.utils.AppConstant;
import com.e2b.utils.DialogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import e2b.activity.ConsumerBaseActivity;
import e2b.model.request.ProfileSetup;
import e2b.model.response.UserResponse;
import e2b.utils.ConsumerPreferenceKeeper;
import retrofit2.Call;

public class ProfileFragment extends BaseFragment {

    private String TAG = ProfileSetupFragment.class.getSimpleName();
    private View view;

    @Bind(R.id.et_profile_full_name)
    EditText etFullName;

    @Bind(R.id.et_profile_address1)
    EditText etProfileAddress;

    @Bind(R.id.et_signup_profile_location)
    EditText etProfileLocation;

    @Bind(R.id.et_profile_moileno)
    EditText etProfileMobileNo;

    private String fullName;
    private String address1;

    private String[] locPermission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private LocationManager locationManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ConsumerBaseActivity)activity).setHeaderText("Profile");
        ((ConsumerBaseActivity)activity).managebackIconVisiblity(false);
        getUserProfile();
        requestLocation();
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
                etProfileAddress.setText(user.getAddress());
                etProfileMobileNo.setText(user.getMobile());

                Coordinate coordinate = user.getCoordinates();
                if(coordinate != null){
                    etProfileLocation.setText(coordinate.getLat()+":"+coordinate.getLng());
                }else{
                    etProfileLocation.setText("");
                    DialogUtils.showDialog(getActivity(), "Location point is required.");
                }
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
        address1 = etProfileAddress.getText().toString().trim();

        ProfileSetup profileSetup = new ProfileSetup();

        profileSetup.setName(fullName);
        profileSetup.setAddress(address1);
        String loc = etProfileLocation.getText().toString().trim();

        if (loc.length() > 0) {
            Coordinate coordinate = new Coordinate();
            try {
                coordinate.setLat(Double.parseDouble(loc.split(":")[0]));
                coordinate.setLng(Double.parseDouble(loc.split(":")[1]));
                profileSetup.setCoordinates(coordinate);
            }catch (Exception e){
                profileSetup.setCoordinates(null);
            }
        } else {
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

    public void requestLocation() {
        Log.d(TAG,"************************* requestLocation");
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(activity,
                    locPermission,
                    AppConstant.REQUEST_CODE.LOCATION_PERMISSION);
            return;
        }

        if (!checkLocation())
            return;

        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 5 * 1000, 10, locationListenerNetwork);
        }else if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5 * 1000, 10, locationListenerNetwork);
        }else{
            activity.showToast("Location Provider is not present");
        }
    }

    private double longitudeNetwork;
    private double latitudeNetwork;
    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeNetwork = location.getLongitude();
            latitudeNetwork = location.getLatitude();

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "**********************************"+latitudeNetwork +":"+ longitudeNetwork);
                    activity.showToast("location received");
                    etProfileLocation.setText("");
                    etProfileLocation.setText(latitudeNetwork +":"+ longitudeNetwork);
                    locationManager.removeUpdates(locationListenerNetwork);
                }
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(myIntent, AppConstant.REQ.LOCATION_REQUEST_CODE);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppConstant.REQ.LOCATION_REQUEST_CODE){
            requestLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppConstant.REQUEST_CODE.LOCATION_PERMISSION:
                Log.d(TAG, "************** LOCATION_PERMISSION Callback");
                requestLocation();
                break;
        }
    }

}

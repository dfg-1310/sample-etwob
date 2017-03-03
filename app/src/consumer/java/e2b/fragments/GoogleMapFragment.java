package e2b.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e2b.R;
import com.e2b.activity.BaseActivity;
import com.e2b.api.ApiCallback;
import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;
import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.Error;
import com.e2b.utils.DialogUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import e2b.model.response.Merchant;
import e2b.model.response.MerchantResponse;
import retrofit2.Call;

/**
 * Created by gaurav on 6/2/17.
 */

public class GoogleMapFragment extends BaseFragment implements OnMapReadyCallback {

    private static final String TAG = GoogleMapFragment.class.getSimpleName();
    private static final int PERMISSION = 100;
    private View view;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private HashMap<Integer, Marker> integerMarkerHashMap = new HashMap<>();
    private boolean isFromBoostLive;
    public static float ZOOM_LEVEL = (float) 12.50;
    private BaseActivity mActivity;
    public MerchantResponse merchantResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.googlemap_fragment, container, false);
        init();
        mActivity = (BaseActivity) getActivity();
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentLocation();
        getMerchants();
    }

    private void getMerchants() {
         IApiRequest request = ApiClient.getRequest();
         Call<BaseResponse<MerchantResponse>> call = request.getMerchants();
         call.enqueue(new ApiCallback<MerchantResponse>(activity) {
            @Override
            public void onSucess(MerchantResponse merchantResponse) {
                if(merchantResponse != null && merchantResponse.getMerchants().size()>0){
                    GoogleMapFragment.this.merchantResponse = merchantResponse;
                }else{
                    DialogUtils.showDialog(activity, getString(R.string.no_merchant_text));
                }

                setupMarker(mMap, merchantResponse.getMerchants());
            }

            @Override
            public void onError(Error msg) {
                activity.showToast(msg.getMsg());
            }
        });
    }

    private void setupMarker(GoogleMap googleMap, List<Merchant> merchants) {
        LatLng latLng = null;
        for(Merchant merchant : merchants){
            latLng = new LatLng(merchant.getCoordinates().getLat(), merchant.getCoordinates().getLng());
            googleMap.addMarker(new MarkerOptions().position(latLng)
                    .title(merchant.getShopName())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));
        }

        if(latLng != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));
        }

    }

    private void init() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void currentLocation() {
//        if (mMap != null) {
//            double lat = Double.parseDouble(ConsumerPreferenceKeeper.getInstance().getLat());
//            double longitute = Double.parseDouble(ConsumerPreferenceKeeper.getInstance().getLong());
//            LatLng selectedLatLng = new LatLng(lat, longitute);
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedLatLng));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, ZOOM_LEVEL));
//        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        currentLocation();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION);
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
        }
        if(merchantResponse != null){
            setupMarker(googleMap, merchantResponse.getMerchants());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                return;
            }
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
        }
    }


}


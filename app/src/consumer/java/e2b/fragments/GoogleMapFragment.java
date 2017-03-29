package e2b.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.e2b.R;
import com.e2b.activity.BaseActivity;
import com.e2b.api.ApiCallback;
import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;
import com.e2b.mapmarker.MapWrapperLayout;
import com.e2b.mapmarker.OnInfoWindowElemTouchListener;
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
    private MapWrapperLayout mapWrapperLayout;
    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.googlemap_fragment, container, false);
        init();
        mActivity = (BaseActivity) getActivity();
        mapWrapperLayout = (MapWrapperLayout) view.findViewById(R.id.map_relative_layout);
        return view;
    }

public void setupMapMarkers(){

    mapWrapperLayout.init(mMap, getPixelsFromDp(activity, 39 + 20));

    // We want to reuse the info window for all the markers,
    // so let's create only one class member instance
    this.infoWindow = (ViewGroup)activity.getLayoutInflater().inflate(R.layout.info_window, null);
    this.infoTitle = (TextView)infoWindow.findViewById(R.id.title);
    this.infoSnippet = (TextView)infoWindow.findViewById(R.id.snippet);
    this.infoButton = (Button)infoWindow.findViewById(R.id.button);

    // Setting custom OnTouchListener which deals with the pressed state
    // so it shows up
    this.infoButtonListener = new OnInfoWindowElemTouchListener(infoButton,
            getResources().getDrawable(R.drawable.banner), //btn_default_normal_holo_light
            getResources().getDrawable(R.drawable.user_icn)) //btn_default_pressed_holo_light
    {
        @Override
        protected void onClickConfirmed(View v, Marker marker) {
            // Here we can perform some action triggered after clicking the button
            Toast.makeText(activity, marker.getTitle() + "'s button clicked!", Toast.LENGTH_SHORT).show();
        }
    };
    this.infoButton.setOnTouchListener(infoButtonListener);


    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            // Setting up the infoWindow with current's marker info
            infoTitle.setText(marker.getTitle());
            infoSnippet.setText(marker.getSnippet());
            infoButtonListener.setMarker(marker);

            // We must call this to set the current marker and infoWindow references
            // to the MapWrapperLayout
            mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
            return infoWindow;
        }
    });

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
                if (merchantResponse != null && merchantResponse.getMerchants().size() > 0) {
                    GoogleMapFragment.this.merchantResponse = merchantResponse;
                } else {
                    DialogUtils.showDialog(activity, getString(R.string.no_merchant_text));
                }
                if(mMap != null){
                    setupMarker(mMap, merchantResponse.getMerchants());
                }
            }

            @Override
            public void onError(Error msg) {
                activity.showToast(msg.getMsg());
            }
        });
    }

    private void setupMarker(GoogleMap googleMap, List<Merchant> merchants) {
        LatLng latLng = null;
        MarkerOptions options = null;
        Marker marker = null;
        for (Merchant merchant : merchants) {
            latLng = new LatLng(merchant.getCoordinates().getLat(), merchant.getCoordinates().getLng());
            options = new MarkerOptions();
            options.position(latLng)
                    .title(merchant.getShopName());
            options.snippet(getMarkerSnippetString(merchant));
            marker = googleMap.addMarker(options);
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));
        }

        if (latLng != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
    }

    private String getMarkerSnippetString(Merchant merchant) {
        StringBuilder builder = new StringBuilder();
        builder.append("Timing : " + merchant.getShopTiming().getFrom() + " to " + merchant.getShopTiming().getTo()+
                "\n"+ merchant.getClosingDays() + "/n" + merchant.getShopAddress());

        return builder.toString();
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
        setupMapMarkers();
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
        if (merchantResponse != null && googleMap != null) {
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

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

}


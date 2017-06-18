package e2b.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e2b.R;
import com.e2b.activity.BaseActivity;
import com.e2b.api.ApiCallback;
import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;
import com.e2b.fragments.BaseFragment;
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

import e2b.activity.ConsumerBaseActivity;
import e2b.activity.MapActivity;
import e2b.model.response.Merchant;
import e2b.model.response.MerchantResponse;
import retrofit2.Call;


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
    //    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;
    private LinearLayout rootLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.googlemap_fragment, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        init();
        mActivity = (BaseActivity) getActivity();
        ((ConsumerBaseActivity)mActivity).managebackIconVisiblity(false);
        ((ConsumerBaseActivity)mActivity).setHeaderText("General Store");
        mapWrapperLayout = (MapWrapperLayout) view.findViewById(R.id.map_relative_layout);
        return view;
    }

    public void setupMapMarkers() {

        mapWrapperLayout.init(mMap, getPixelsFromDp(activity, 39 + 20));

        // We want to reuse the info window for all the markers,
        // so let's create only one class member instance
        this.infoWindow = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.info_window, null);
        this.infoTitle = (TextView) infoWindow.findViewById(R.id.title);
        this.infoSnippet = (TextView) infoWindow.findViewById(R.id.snippet);
        this.rootLayout = (LinearLayout) infoWindow.findViewById(R.id.map_marker_rootlayout);
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Marker marker = (Marker) rootLayout.getTag();
                if (marker != null) {
                    ((MapActivity) activity).markerClick((Merchant) marker.getTag());
                }
                return false;
            }
        });

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
                rootLayout.setTag(marker);
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
        if (mMap != null) {
            mMap.clear();
        }
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
                if (mMap != null) {
                    mMap.clear();
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
            Log.d(TAG, "*********************************************");

            if (merchant.getCoordinates() != null) {
                Log.d(TAG, " Merchant Name : "+merchant.getShopName()+ " Merchant Coordinate :: "+merchant.getCoordinates().toString());
                latLng = new LatLng(merchant.getCoordinates().getLat(), merchant.getCoordinates().getLng());
                options = new MarkerOptions();
                options.position(latLng)
                        .title(merchant.getShopName());
                options.snippet(getMarkerSnippetString(merchant));
                marker = googleMap.addMarker(options);
                marker.setTag(merchant);
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));
            }
        }

        if (latLng != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d(TAG, "onMarkerClick : " + marker.toString());
                return false;
            }
        });
    }

    private String getMarkerSnippetString(Merchant merchant) {
        StringBuilder builder = new StringBuilder();
        builder.append("Timing : " + merchant.getShopTiming().getFrom() + " to " + merchant.getShopTiming().getTo() +
                "\n" + "Closing Days : " + getStringValue(merchant.getClosingDays()) + "\n" + "Address : " + merchant.getShopAddress());

        return builder.toString();
    }

    private String getStringValue(String[] closingDays) {
        StringBuilder builder = new StringBuilder();
        for (String day : closingDays) {
            if (builder.toString().length() > 0) {
                builder.append(", ");
            }
            builder.append(day);
        }
        return builder.toString();
    }

    private void init() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void currentLocation() {
//        if (mMap != null) {
//            double lat = Double.parseDouble(MerchantPreferenceKeeper.getInstance().getLat());
//            double longitute = Double.parseDouble(MerchantPreferenceKeeper.getInstance().getLong());
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
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMap != null) {
            mMap.clear();
        }
    }

}


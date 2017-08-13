package e2b.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.e2b.R;
import com.e2b.activity.BaseActivity;
import com.e2b.api.ApiCallback;
import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;
import com.e2b.fragments.BaseFragment;
import com.e2b.mapmarker.MapWrapperLayout;
import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.Error;
import com.e2b.utils.AppConstant;
import com.e2b.utils.DialogUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
    public static float ZOOM_LEVEL = (float) 12.50;
    private BaseActivity mActivity;
    public MerchantResponse merchantResponse;
    private MapWrapperLayout mapWrapperLayout;
    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private LinearLayout rootLayout;
//    private com.google.android.gms.location.LocationListener locationListener;
    private String[] locPermission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private LocationManager locationManager;

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
        ((ConsumerBaseActivity) mActivity).managebackIconVisiblity(false);
        ((ConsumerBaseActivity) mActivity).setHeaderText("General Store");
        mapWrapperLayout = (MapWrapperLayout) view.findViewById(R.id.map_relative_layout);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

//        locationListener = new com.google.android.gms.location.LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                //move map camera
//                Toast.makeText(getActivity(), " onLocationChanged " + location.getLatitude() + " : " + location.getLongitude(), Toast.LENGTH_LONG).show();
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));
//                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, locationListener);
//
//            }
//        };
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mMap != null) {
            mMap.clear();
        }
        requestLocation();
    }

    public void requestLocation() {
        Log.d(TAG, "************************* requestLocation");
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(activity, locPermission, AppConstant.REQUEST_CODE.LOCATION_PERMISSION);
            return;
        }

        if (!checkLocation())
            return;

        if(mMap != null)
            mMap.setMyLocationEnabled(true);

        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 5 * 1000, 10, locationListenerNetwork);
        } else if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5 * 1000, 10, locationListenerNetwork);
        } else {
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
                    Log.d(TAG, "**********************************" + latitudeNetwork + ":" + longitudeNetwork);
                    activity.showToast("location received");
                    LatLng latLng = new LatLng(latitudeNetwork, longitudeNetwork);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));
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
                Log.d(TAG, " Merchant Name : " + merchant.getShopName() + " Merchant Coordinate :: " + merchant.getCoordinates().toString());
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
        builder.append("Timing : " + getTime(merchant.getShopTiming().getFrom()) + " to " + getTime(merchant.getShopTiming().getTo()) +
                "\n" + "Closing Days : " + getStringValue(merchant.getClosingDays()) + "\n" + "Address : " + merchant.getShopAddress());

        return builder.toString();
    }

    private String getTime(int time) {
        return time / 60 + ":" + time % 60;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupMapMarkers();
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
        getMerchants();
   }



//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        mGoogleApiClient.connect();
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppConstant.REQUEST_CODE.LOCATION_PERMISSION:
                Log.d(TAG, "************** LOCATION_PERMISSION Callback");
                Toast.makeText(getActivity(), "LOCATION_PERMISSION Callback", Toast.LENGTH_SHORT).show();
                requestLocation();
                break;
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


//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        if (ContextCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationListener);
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
////        //stop location updates when Activity is no longer active
////        if (mGoogleApiClient != null) {
////            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, locationListener);
////        }
//    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(GoogleMapFragment.this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(GoogleMapFragment.this.getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(GoogleMapFragment.this.getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(GoogleMapFragment.this.getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(GoogleMapFragment.this.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }
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
}

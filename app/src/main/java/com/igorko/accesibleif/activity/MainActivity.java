package com.igorko.accesibleif.activity;

/**
 * Created by Igorko on 01.11.2016.
 */

import android.Manifest;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.igorko.accesibleif.R;
import com.igorko.accesibleif.fragments.AboutUsFagment;
import com.igorko.accesibleif.fragments.HowItsWorkFagment;
import com.igorko.accesibleif.fragments.SettingsFagment;
import com.igorko.accesibleif.manager.PreferencesManager;
import com.igorko.accesibleif.models.Data;
import com.igorko.accesibleif.models.Element;
import com.igorko.accesibleif.retrofit.ApiManager;
import com.igorko.accesibleif.retrofit.NetworkCallback;
import com.igorko.accesibleif.services.LocationService;
import com.igorko.accesibleif.utils.CameraUtils;
import com.igorko.accesibleif.utils.Const;
import com.igorko.accesibleif.utils.Extras;
import com.igorko.accesibleif.utils.LocationUtils;
import com.igorko.accesibleif.utils.MapUtils;
import com.igorko.accesibleif.utils.MarkerUtils;
import com.igorko.accesibleif.utils.NetworkUtils;
import com.igorko.accesibleif.utils.NumberUtils;
import com.mikepenz.materialdrawer.Drawer;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements OnMapReadyCallback, Const, Extras {

    private CoordinatorLayout mCoordinatorLayout;
    private Bundle mSavedInstanceState;
    private Drawer.Result mDrawer;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Toolbar mToolbar;
    private Intent mLocationServiceIntent;
    private Location mMyLocation;
    private int mSelectedMenuPosition;
    private ArrayList<MarkerOptions> mMarkerList;
    private boolean mMapIsTouched = false;
    private ArrayList<Element> mElementList;
    private boolean mIsIconsTiny;
    private float mZoomLevel;
    private float mPreviousZoomLevel;
    private CameraPosition mCameraPosition;
    private NetworkCallback<Data> mNetworkCallback;
    private boolean mIsActivityVisible = true;

    public void onStart() {
        initGoogleApiClient(MainActivity.this);
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSavedInstanceState = savedInstanceState;

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            setTheme(R.style.AppStyle);
        }

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        String appTitle;

        mNetworkCallback = new NetworkCallback<Data>() {
            @Override
            public void onSuccess(Data response) {
                if (mIsActivityVisible) {
                    onResponseSuccess(response);
                }
            }

            @Override
            public void onError(int errorMsgStringId) {
                if (mIsActivityVisible) {
                    showSnackbarMassage(getString(errorMsgStringId));
                    hideProgress();
                }
            }
        };

        if (savedInstanceState == null) {
            appTitle = getAppTitle(ALL_ACSSESIBLE_BUILDINGS);
            if (NetworkUtils.isOnline(this)) {
                showProgress();
                getAllBuildings();
            } else {
                Toast.makeText(this, getString(R.string.no_internet_connection),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            mElementList = savedInstanceState.getParcelableArrayList(ELEMENT_LIST_EXTRA);
            mSelectedMenuPosition = savedInstanceState.getInt(SELECTED_MENU_POSITION_EXTRA, 0);
            mMyLocation = savedInstanceState.getParcelable(LOCATION_EXTRA);
            mMapIsTouched = savedInstanceState.getBoolean(TOUCH_EXTRA, false);
            mZoomLevel = savedInstanceState.getFloat(ZOOM_LEVEL_EXTRA, 0);
            mIsIconsTiny = savedInstanceState.getBoolean(IS_ICONS_TINY, false);
            mMarkerList = MarkerUtils.getInstance().getAllMarkers(mElementList, mZoomLevel);

            if (mMyLocation != null) {
                displayLocation(mMyLocation);
            }
            appTitle = getAppTitle(mSelectedMenuPosition + 1);
        }
        initToolbar(appTitle);
        mDrawer = initLeftDrawer(appTitle, mSelectedMenuPosition);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findViewById(R.id.location_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LocationUtils.isLocationEnabled()) {
                    if (mMyLocation != null) {
                        moveToCurrentLocation(mMyLocation);
                    } else {
                        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            LocationUtils.getInstance().marshmallowGPSPremissionCheck(MainActivity.this);
                        } else {
                            if (LocationUtils.isLocationEnabled()) {
                                if (mMap != null && mMyLocation != null) {
                                    mMap.animateCamera(CameraUpdateFactory.newLatLng(
                                            new LatLng(mMyLocation.getLatitude(), mMyLocation.getLongitude())));
                                } else {
                                    Toast.makeText(MainActivity.this, getString(R.string.cant_detect_location), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, getString(R.string.gps_not_enabled_settings), Toast.LENGTH_SHORT).show();
                            }
                            if (mMyLocation != null) {
                                moveToCurrentLocation(mMyLocation);
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.gps_not_enabled_settings), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDrawerMenuItemSelected(int position) {
        if (LocationUtils.isLocationEnabled()) {
            initGoogleApiClient(MainActivity.this);
            onConnected(mSavedInstanceState);

            if (mMyLocation != null) {
                displayLocation(mMyLocation);
            }
        }

        if (position < SETTINGS) {
            hideInfo();
        }

        switch (position) {
            case ALL_ACSSESIBLE_BUILDINGS: {
                if (NetworkUtils.isOnline(this)) {
                    showProgress();
                    getAllBuildings();
                } else {
                    showSnackbarMassage(getString(R.string.no_internet_connection));
                }
                break;
            }
            case HOSPITAL_ACSSESIBLE_BUILDINGS: {
                if (NetworkUtils.isOnline(this)) {
                    showProgress();
                    getHospitalBuildings();
                } else {
                    showSnackbarMassage(getString(R.string.no_internet_connection));
                }
                break;
            }
            case PHARMACY_ACSSESIBLE_BUILDINGS: {
                if (NetworkUtils.isOnline(this)) {
                    showProgress();
                    getPharmacyBuildings();
                } else {
                    showSnackbarMassage(getString(R.string.no_internet_connection));
                }
                break;
            }
            case SHOP_BUILDINGS: {
                if (NetworkUtils.isOnline(this)) {
                    showProgress();
                    getShopBuildings();
                } else {
                    showSnackbarMassage(getString(R.string.no_internet_connection));
                }
                break;
            }
            case ATM_BUILDINGS: {
                if (NetworkUtils.isOnline(this)) {
                    showProgress();
                    getATMBuildings();
                } else {
                    showSnackbarMassage(getString(R.string.no_internet_connection));
                }
                break;
            }
            case SETTINGS: {
                hideProgress();
                showSettings();
                break;
            }
            case HOW_ITS_WORK:{
                hideProgress();
                showHowItsWorkInfo();
                break;
            }
            case ABOUT_US: {
                hideProgress();
                showAboutUsInfo();
                break;
            }
            default: {
                break;
            }
        }
        mSelectedMenuPosition = --position;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if (mSavedInstanceState == null) {
            if (LocationUtils.isLocationEnabled() && mMyLocation != null) {
                displayLocation(mMyLocation);
            } else {
                moveToCenterIf(googleMap, true);
            }
        } else {
            mMarkerList = MarkerUtils.getInstance().addMarkers(googleMap, mMarkerList);
        }

         /*For screen rotation before request call*/
        if (mElementList == null) {
            getAllBuildings();
            moveToCenterIf(mMap, true);
        }

        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mZoomLevel = cameraPosition.zoom;
                if (mPreviousZoomLevel != cameraPosition.zoom) {
                    ArrayList<MarkerOptions> markerList = MarkerUtils.getInstance().getAllMarkers(mElementList, mZoomLevel);
                    MarkerUtils.getInstance().addMarkers(googleMap, markerList);
                }

                if (!mMapIsTouched && PreferencesManager.isMapLimitSetted()) {
                    MapUtils.setCheckLimits(mMap, mCameraPosition, mMapIsTouched, mZoomLevel);
                }

                mPreviousZoomLevel = cameraPosition.zoom;
                mCameraPosition = cameraPosition;
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    public void initMarkerList(ArrayList<MarkerOptions> markerList) {
        mMarkerList = markerList;
    }

    void getAllBuildings() {
        ApiManager.getInstance().getApiProvider().getAllBuildings(mNetworkCallback);
    }

    void getPharmacyBuildings() {
        ApiManager.getInstance().getApiProvider().getPharmacyBuildings(mNetworkCallback);
    }

    void getHospitalBuildings() {
        ApiManager.getInstance().getApiProvider().getHospitalBuildings(mNetworkCallback);
    }

    void getShopBuildings() {
        ApiManager.getInstance().getApiProvider().getShopBuildings(mNetworkCallback);
    }

    void getATMBuildings() {
        ApiManager.getInstance().getApiProvider().getATMBuildings(mNetworkCallback);
    }

    private void onResponseSuccess(Data response) {
        mElementList = response.getElements();
        ArrayList<MarkerOptions> markerList = MarkerUtils.getInstance().getAllMarkers(mElementList, mZoomLevel);
        MarkerUtils.getInstance().addMarkers(mMap, markerList);

        if (mMyLocation != null) {
            displayLocation(mMyLocation);
        }
        hideProgress();
    }

    private void showSettings() {
        mToolbar.setTitle(getString(R.string.settings_title));
        Fragment fragment = getFragmentManager().findFragmentByTag(SETTINGS_FRAGMENT_TAG);
        getFragmentManager().beginTransaction()
                .replace(R.id.content, fragment == null ? SettingsFagment.newInstance() : (SettingsFagment) fragment, SETTINGS_FRAGMENT_TAG)
                .commit();
    }

    private void showAboutUsInfo() {
        mToolbar.setTitle(getString(R.string.about_title));
        Fragment fragment = getFragmentManager().findFragmentByTag(ABOUT_FRAGMENT_TAG);
        getFragmentManager().beginTransaction()
                .replace(R.id.content, fragment == null ? AboutUsFagment.newInstance() : (AboutUsFagment) fragment, ABOUT_FRAGMENT_TAG)
                .commit();
    }

    private void showHowItsWorkInfo() {
        mToolbar.setTitle(getString(R.string.how_its_work_title));
        Fragment fragment = getFragmentManager().findFragmentByTag(HOW_ITS_WORK_TAG);
        getFragmentManager().beginTransaction()
                .replace(R.id.content, fragment == null ? HowItsWorkFagment.newInstance() : (HowItsWorkFagment) fragment, HOW_ITS_WORK_TAG)
                .commit();
    }

    private void hideInfo() {
        mToolbar.setTitle(getString(R.string.app_name));

        Fragment aboutFragment = getFragmentManager().findFragmentByTag(ABOUT_FRAGMENT_TAG);
        if (aboutFragment != null) {
            getFragmentManager().beginTransaction().
                    remove((AboutUsFagment) aboutFragment).commit();
        }

        Fragment settingsFragment = getFragmentManager().findFragmentByTag(SETTINGS_FRAGMENT_TAG);
        if (settingsFragment != null) {
            getFragmentManager().beginTransaction().
                    remove((SettingsFagment) settingsFragment).commit();

            if (PreferencesManager.isMapLimitSetted() && mMap != null) {
                CameraUtils.moveToCenterIf(mMap, false, false);
            }
        }

        Fragment howItsFragment = getFragmentManager().findFragmentByTag(HOW_ITS_WORK_TAG);
        if (howItsFragment != null) {
            getFragmentManager().beginTransaction().
                    remove((HowItsWorkFagment) howItsFragment).commit();
        }
    }

    private void startFindMyLocationService() {
        Intent locationServiceIntent = new Intent(MainActivity.this, LocationService.class);
        startService(locationServiceIntent);
    }

    private void moveToCurrentLocation(Location location) {
        if (mMap != null && location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLng(
                    new LatLng(location.getLatitude(), location.getLongitude())));
            displayLocation(location);
        }
    }

    private void displayLocation(Location location) {
        if (mMap != null && location != null && LocationUtils.isLocationEnabled()) {
            MarkerUtils.getInstance().showCurrentLocation(mMap, location);
            mMyLocation = location;
        } else {
            Toast.makeText(this, getString(R.string.current_location_not_available), Toast.LENGTH_LONG);
        }
    }

    private void moveToCenterIf(GoogleMap googleMap, boolean zoom) {
        float ratushaLatitude = NumberUtils.getFloat(R.dimen.ratusha_latitude);
        float ratushaLongitude = NumberUtils.getFloat(R.dimen.ratusha_longitude);

        LatLng centerIFPosition = new LatLng(ratushaLatitude, ratushaLongitude);
        if (googleMap != null) {
            Log.d(TAG, Boolean.valueOf(mMapIsTouched).toString());
            if (!mMapIsTouched) {
                if (zoom) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerIFPosition, 18.0f));
                } else {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(centerIFPosition));
                }
            }
        }
    }

    protected String getAppTitle(int selectedMenuPosition) {
        String appTitle;
        switch (selectedMenuPosition) {
            case SETTINGS: {
                appTitle = getString(R.string.settings_title);
                break;
            }
            case ABOUT_US: {
                appTitle = getString(R.string.about_title);
                break;
            }
            default: {
                appTitle = getString(R.string.app_name);
                break;
            }
        }
        return appTitle;
    }

    private BroadcastReceiver mLocationDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String statusMessage = intent.getStringExtra(STATUS_EXTRA);
            Log.d(TAG, statusMessage);

            Location currentLocation = (Location) intent.getParcelableExtra(LOCATION_EXTRA);
            if (currentLocation != null) {
                if (currentLocation != null) {
                    Log.d(TAG, "MAIN last known location " + String.valueOf(currentLocation.getLatitude())
                            + ", " + String.valueOf(currentLocation.getLongitude()));
                    if (PreferencesManager.isFollowingLocation()) {
                        moveToCurrentLocation(currentLocation);
                    } else {
                        displayLocation(currentLocation);
                    }

                    mMyLocation = currentLocation;
                }
            }

            Location startLocation = (Location) intent.getParcelableExtra(LOCATION_START_EXTRA);
            if (startLocation != null) {
                moveToCurrentLocation(startLocation);
                mMyLocation = startLocation;
            }
        }
    };

    public GoogleMap getMap() {
        return mMap;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mMapIsTouched = true;
                break;

            case MotionEvent.ACTION_UP:
                mMapIsTouched = false;
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (ViewConfiguration.get(this).hasPermanentMenuKey()) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                if (mDrawer.isDrawerOpen()) {
                    mDrawer.closeDrawer();
                } else {
                    mDrawer.openDrawer();
                }
                return true;
            }
            return super.onKeyUp(keyCode, event);
        } else {
            return true;
        }
    }

    private void showSnackbarMassage(String massage) {
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, massage, Snackbar.LENGTH_LONG);
        ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if ((mElementList != null) && (!mElementList.isEmpty())) {
            savedInstanceState.putParcelableArrayList(ELEMENT_LIST_EXTRA, new ArrayList<Parcelable>(mElementList));
        }

        savedInstanceState.putInt(SELECTED_MENU_POSITION_EXTRA, mSelectedMenuPosition);
        savedInstanceState.putParcelable(LOCATION_EXTRA, mMyLocation);
        savedInstanceState.putBoolean(TOUCH_EXTRA, mMapIsTouched);
        savedInstanceState.putFloat(ZOOM_LEVEL_EXTRA, mZoomLevel);
        savedInstanceState.putBoolean(IS_ICONS_TINY, mIsIconsTiny);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mLocationDataReceiver, new IntentFilter(LOCATION_DATA_FILTER_NAME));
        mIsActivityVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mLocationDataReceiver);
        mIsActivityVisible = false;
        hideProgress();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Fragment aboutFragment = getFragmentManager().findFragmentByTag(ABOUT_FRAGMENT_TAG);
        Fragment settingsFragment = getFragmentManager().findFragmentByTag(SETTINGS_FRAGMENT_TAG);

        if (mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
        } else if (aboutFragment != null || settingsFragment != null && keyCode == KeyEvent.KEYCODE_BACK) {
            hideInfo();
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void initGoogleApiClient(MainActivity activity) {
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d(TAG, "onConnected");
        mLocationServiceIntent = new Intent(MainActivity.this, LocationService.class);
        startService(mLocationServiceIntent);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
        if (mLocationServiceIntent != null) {
            stopService(mLocationServiceIntent);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
        if (mLocationServiceIntent != null) {
            stopService(mLocationServiceIntent);
        }
    }

    public void onStop() {
        super.onStop();

        if (mLocationServiceIntent != null) {
            stopService(mLocationServiceIntent);
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startFindMyLocationService();
                } else {
                    showSnackbarMassage(getString(R.string.location_permissions_needed));
                }
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
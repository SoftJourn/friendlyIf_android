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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.igorko.accesibleif.R;
import com.igorko.accesibleif.fragments.AboutUsFagment;
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
    private Toolbar mToolbar;
    private MapUtils mMapUtilsInstanse;
    private Location mMyLocation;
    private int mSelectedMenuPosition;
    private ArrayList<MarkerOptions> mMarkerList;
    private boolean mMapIsTouched = false;
    private ArrayList<Element> mElementList;
    private boolean mIsIconsTiny;
    private float mZoomLevel;
    private float mPreviousZoomLevel;
    private LatLng mCameraTarget;
    private CameraPosition mCameraPosition;
    private MarkerUtils mMarkerUtils = MarkerUtils.getInstance();

    protected void onStart() {
        mMapUtilsInstanse.initGoogleApiClient(MainActivity.this);
        mMapUtilsInstanse.onStart();
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSavedInstanceState = savedInstanceState;

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            setTheme(R.style.AppStyle);
        }

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        String appTitle;
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
            mMarkerList = savedInstanceState.getParcelableArrayList(MARKER_LIST_EXTRA);
            mSelectedMenuPosition = savedInstanceState.getInt(SELECTED_MENU_POSITION_EXTRA, 0);
            mMyLocation = savedInstanceState.getParcelable(LOCATION_EXTRA);
            mMapIsTouched = savedInstanceState.getBoolean(TOUCH_EXTRA, false);
            mZoomLevel = savedInstanceState.getFloat(ZOOM_LEVEL_EXTRA, 0);
            mIsIconsTiny = savedInstanceState.getBoolean(IS_ICONS_TINY, false);

            if(mMyLocation != null) {
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
                } else{
                    Toast.makeText(getApplicationContext(), getString(R.string.gps_not_enabled_settings), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mMapUtilsInstanse = MapUtils.getInstance();
    }

    @Override
    protected void onDrawerMenuItemSelected(int position) {
        if (LocationUtils.isLocationEnabled()) {
            mMapUtilsInstanse = MapUtils.getInstance();
            mMapUtilsInstanse.initGoogleApiClient(this);
            mMapUtilsInstanse.onConnected(mSavedInstanceState);

            if(mMyLocation!= null) {
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
            case SETTINGS: {
                hideProgress();
                showSettings();
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if(mSavedInstanceState == null){
            if (LocationUtils.isLocationEnabled() && mMyLocation != null) {
                displayLocation(mMyLocation);
            }else{
                moveToCenterIf(googleMap, true);
            }
        } else {
            mMarkerList = MarkerUtils.getInstance().addMarkers(googleMap, mMarkerList);
        }

        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mCameraPosition = cameraPosition;
                mMarkerUtils.drawMarkers(MainActivity.this, mMap, mMarkerList,
                        mCameraPosition, mElementList, mPreviousZoomLevel, mMapIsTouched, true);
                mPreviousZoomLevel = mMarkerUtils.getPreveuosZoomLevel();
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
        ApiManager.getInstance().getApiProvider().getAllBuildings((new NetworkCallback<Data>() {
            @Override
            public void onSuccess(Data response) {
                onResponseSuccess(response, ALL_ACSSESIBLE_BUILDINGS);
            }

            @Override
            public void onError(int errorMsgStringId) {
                showSnackbarMassage(getString(errorMsgStringId));
                hideProgress();
            }
        }));
    }

    void getPharmacyBuildings() {
        ApiManager.getInstance().getApiProvider().getPharmacyBuildings(new NetworkCallback<Data>() {
            @Override
            public void onSuccess(Data response) {
                onResponseSuccess(response, PHARMACY_ACSSESIBLE_BUILDINGS);
            }

            @Override
            public void onError(int errorMsgStringId) {
                showSnackbarMassage(getString(errorMsgStringId));
                hideProgress();
            }
        });
    }

    void getHospitalBuildings() {
        ApiManager.getInstance().getApiProvider().getHospitalBuildings(new NetworkCallback<Data>() {
            @Override
            public void onSuccess(Data response) {
                onResponseSuccess(response, HOSPITAL_ACSSESIBLE_BUILDINGS);
            }

            @Override
            public void onError(int errorMsgStringId) {
                showSnackbarMassage(getString(errorMsgStringId));
                hideProgress();
            }
        });
    }

    void getShopBuildings() {
        ApiManager.getInstance().getApiProvider().getShopBuildings(new NetworkCallback<Data>() {
            @Override
            public void onSuccess(Data response) {
                onResponseSuccess(response, SHOP_BUILDINGS);
            }

            @Override
            public void onError(int errorMsgStringId) {
                showSnackbarMassage(getString(errorMsgStringId));
                hideProgress();
            }
        });
    }

    private void onResponseSuccess(Data response, int buildingsType) {
        mElementList = response.getElements();
        MarkerUtils markersInstance = MarkerUtils.getInstance();

        ArrayList<MarkerOptions> markerList = null;

        if (mElementList != null && !mElementList.isEmpty()) {
            switch (buildingsType) {
                case ALL_ACSSESIBLE_BUILDINGS: {
                    markerList = markersInstance.getAllMarkers(mElementList);
                    break;
                }
                case PHARMACY_ACSSESIBLE_BUILDINGS: {
                    markerList = markersInstance.getPharmacyMarkers(mElementList);
                    break;
                }
                case HOSPITAL_ACSSESIBLE_BUILDINGS: {
                    markerList = markersInstance.getHospitalMarkers(mElementList);
                    break;
                }
                case SHOP_BUILDINGS: {
                    markerList = markersInstance.getShopMarkers(mElementList);
                    break;
                }
            }
        }

        markersInstance.drawMarkers(MainActivity.this, mMap, markerList, mCameraPosition,
                mElementList, mPreviousZoomLevel, mMapIsTouched, false);
        mPreviousZoomLevel = markersInstance.getPreveuosZoomLevel();
        if(mMyLocation != null) {
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
    }

    private void startFindMyLocationService() {
        Intent locationServiceIntent = new Intent(MainActivity.this, LocationService.class);
        startService(locationServiceIntent);
    }

    private void moveToCurrentLocation(Location location){
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
                    if(PreferencesManager.isFollowingLocation()){
                        moveToCurrentLocation(currentLocation);
                    }else {
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

    private void showSnackbarMassage(String massage){
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, massage, Snackbar.LENGTH_LONG);
        ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        if((mElementList != null) && (!mElementList.isEmpty())){
            savedInstanceState.putParcelableArrayList(ELEMENT_LIST_EXTRA, new ArrayList<Parcelable>(mElementList));
        }
        if((mMarkerList != null) && (!mMarkerList.isEmpty())){
            savedInstanceState.putParcelableArrayList(MARKER_LIST_EXTRA, new ArrayList<Parcelable>(mMarkerList));
        }
        savedInstanceState.putInt(SELECTED_MENU_POSITION_EXTRA, mSelectedMenuPosition);
        savedInstanceState.putParcelable(LOCATION_EXTRA, mMyLocation);
        savedInstanceState.putBoolean(TOUCH_EXTRA, mMapIsTouched);
        savedInstanceState.putFloat(ZOOM_LEVEL_EXTRA, mZoomLevel);
        savedInstanceState.putBoolean(IS_ICONS_TINY, mIsIconsTiny);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mLocationDataReceiver, new IntentFilter(LOCATION_DATA_FILTER_NAME));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mLocationDataReceiver);
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

    @Override
    protected void onStop() {
        if (mMapUtilsInstanse != null) {
            mMapUtilsInstanse.onStop();
        }
        super.onStop();
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
}
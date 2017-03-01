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
import android.support.v4.content.ContextCompat;
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
import com.igorko.accesibleif.app.AppContext;
import com.igorko.accesibleif.fragments.AboutUsFagment;
import com.igorko.accesibleif.fragments.HowItsWorkFagment;
import com.igorko.accesibleif.fragments.SettingsFagment;
import com.igorko.accesibleif.manager.CityManager;
import com.igorko.accesibleif.manager.DataManager;
import com.igorko.accesibleif.manager.IDataManager;
import com.igorko.accesibleif.manager.PreferencesManager;
import com.igorko.accesibleif.models.City;
import com.igorko.accesibleif.models.Data;
import com.igorko.accesibleif.models.Element;
import com.igorko.accesibleif.services.LocationService;
import com.igorko.accesibleif.utils.CameraUtils;
import com.igorko.accesibleif.utils.Const;
import com.igorko.accesibleif.utils.DialogUtils;
import com.igorko.accesibleif.utils.Extras;
import com.igorko.accesibleif.utils.LocationUtils;
import com.igorko.accesibleif.utils.MapUtils;
import com.igorko.accesibleif.utils.MarkerUtils;
import com.igorko.accesibleif.utils.NetworkUtils;
import com.mikepenz.materialdrawer.Drawer;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements OnMapReadyCallback, Const, Extras, DataManager.IDataListener {

    private IDataManager mDataManager;
    private BuildingsType mSelectedType = BuildingsType.ALL;
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
    private long mOnRecentBackPressedTime;
    private int mSelectedCityID;

    public void onStart() {
        initGoogleApiClient();
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSavedInstanceState = savedInstanceState;
        mDataManager = new DataManager(this);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        String appTitle;

        if (savedInstanceState == null) {
            appTitle = getAppTitle(ALL_ACSSESIBLE_BUILDINGS);
        } else {
            mElementList = savedInstanceState.getParcelableArrayList(ELEMENT_LIST_EXTRA);
            mSelectedMenuPosition = savedInstanceState.getInt(SELECTED_MENU_POSITION_EXTRA, 0);
            mMyLocation = savedInstanceState.getParcelable(LOCATION_EXTRA);
            mMapIsTouched = savedInstanceState.getBoolean(TOUCH_EXTRA, false);
            mZoomLevel = savedInstanceState.getFloat(ZOOM_LEVEL_EXTRA, 0);
            mIsIconsTiny = savedInstanceState.getBoolean(IS_ICONS_TINY, false);
            mSelectedCityID = savedInstanceState.getInt(SELECTED_CITY_ID, 0);
            mMarkerList = MarkerUtils.getInstance().getAllMarkers(mElementList, mZoomLevel);

            if (mMyLocation != null) {
                displayLocation(mMyLocation);
            }
            appTitle = getAppTitle(mSelectedMenuPosition + 1);
        }

        if (PreferencesManager.getInstance().appGetFirstTimeRun() == APP_STARTED_FIRST_TIME) {
            DialogUtils.showSelectCityAlert(MainActivity.this, mSelectedCityID);
        }

        initToolbar(appTitle);
        mDrawer = initLeftDrawer(appTitle, mSelectedMenuPosition);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findViewById(R.id.location_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LocationUtils.isLocationEnabled()) {

                    //Check for permission
                    if ((android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                            && (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION)) {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.location_permissions_needed), Toast.LENGTH_SHORT).show();

                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_LOCATION);
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_LOCATION);
                        }
                        return;
                    } else {
                        startFindMyLocationService();
                    }

                    if (mMap != null && mMyLocation != null) {
                        moveToCurrentLocation(mMyLocation);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(
                                new LatLng(mMyLocation.getLatitude(), mMyLocation.getLongitude())));
                    } else {
                        showSnackbarMassage(getString(R.string.cant_detect_location));
                    }
                } else {
                    showSnackBarMassageWithButton(view, getString(R.string.gps_not_enabled_settings), getString(R.string.go));
                }
            }
        });
    }

    private void showSnackbarMassage(String massage) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), massage, Snackbar.LENGTH_LONG);
        TextView snackViewText = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        snackViewText.setTextColor(Color.WHITE);
        snackbar.show();
    }

    private void showSnackBarMassageWithButton(View view, String snackbarMessage, String buttonName) {
        Snackbar snackbar = Snackbar.make(view, snackbarMessage, Snackbar.LENGTH_LONG)
                .setAction(buttonName, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSettings();
                    }
                });
        snackbar.show();

        TextView snackViewText = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        snackViewText.setTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    protected void onDrawerMenuItemSelected(int position) {
        if (LocationUtils.isLocationEnabled()) {
            if (mMyLocation != null) {
                displayLocation(mMyLocation);
            }
        }

        if (position < SETTINGS) {
            hideInfo();
            if (CityManager.getInstance().isCityWasRecentlyChanged()) {
                CityManager.getInstance().setCityWasRecentlyChanged(false);
                moveToCenterCity(false);
            }
        }

        boolean isGetData = false;

        switch (position) {
            case ALL_ACSSESIBLE_BUILDINGS: {
                mSelectedType = BuildingsType.ALL;
                isGetData = true;
                break;
            }
            case HOSPITAL_ACSSESIBLE_BUILDINGS: {
                mSelectedType = BuildingsType.HOSPITALS;
                isGetData = true;
                break;
            }
            case PHARMACY_ACSSESIBLE_BUILDINGS: {
                mSelectedType = BuildingsType.PHARMACIES;
                isGetData = true;
                break;
            }
            case SHOP_BUILDINGS: {
                mSelectedType = BuildingsType.SHOPS;
                isGetData = true;
                break;
            }
            case ATM_BUILDINGS: {
                mSelectedType = BuildingsType.ATMs;
                isGetData = true;
                break;
            }
            case SETTINGS: {
                hideProgress();
                showSettings();
                isGetData = false;
                break;
            }
            case HOW_ITS_WORK: {
                hideProgress();
                isGetData = false;
                showHowItsWorkInfo();
                break;
            }
            case ABOUT_US: {
                hideProgress();
                isGetData = false;
                showAboutUsInfo();
                break;
            }
            default: {
                break;
            }
        }

        if (isGetData) {
            if (NetworkUtils.isOnline()) {
                showProgress();
                mDataManager.getData(mSelectedType);
            } else {
                showSnackbarMassage(getString(R.string.no_internet_connection));
            }
        }

        mSelectedMenuPosition = --position;
    }

    public void getData(BuildingsType type) {
        if (!NetworkUtils.isOnline()) {
            showSnackbarMassage(getString(R.string.no_internet_connection));
            return;
        }

        showProgress();
        mDataManager.getData(type);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if (mSavedInstanceState == null) {
            if (LocationUtils.isLocationEnabled() && mMyLocation != null) {
                displayLocation(mMyLocation);
            } else {
                moveToCenterCity(true);
            }
        } else {
            mMarkerList = MarkerUtils.getInstance().addMarkers(googleMap, mMarkerList);
        }

         /*For screen rotation before request call*/
        if (mElementList == null) {
            if (PreferencesManager.getInstance().appGetFirstTimeRun() == 1) {
                getData(mSelectedType);
            }
            moveToCenterCity(true);
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
                    CityManager cityManager = CityManager.getInstance();
                    if (!cityManager.isCityWasRecentlyChanged()) {
                        MapUtils.setCheckLimits(mMap, mCameraPosition, mMapIsTouched, mZoomLevel);
                    }
                    CityManager.getInstance().setCityWasRecentlyChanged(false);
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
                CameraUtils.moveToCenterCity(mMap, false, false);
            }
        }

        Fragment howItsFragment = getFragmentManager().findFragmentByTag(HOW_ITS_WORK_TAG);
        if (howItsFragment != null) {
            getFragmentManager().beginTransaction().
                    remove((HowItsWorkFagment) howItsFragment).commit();
        }
    }

    private void startFindMyLocationService() {
        if (!AppContext.isLocationServiseStarted) {
            Intent locationServiceIntent = new Intent(MainActivity.this, LocationService.class);
            startService(locationServiceIntent);
            AppContext.isLocationServiseStarted = true;
        }
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

    public void moveToCenterCity(boolean zoom) {
        CityManager cityManager = new CityManager();
        City currentCity = cityManager.getCurrentCity();
        LatLng cityCenterCoordinates = currentCity.getCityCenter();
        if (mMap != null) {
            Log.d(TAG, Boolean.valueOf(mMapIsTouched).toString());
            if (!mMapIsTouched) {
                if (zoom) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cityCenterCoordinates, 18.0f));
                } else {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(cityCenterCoordinates));
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
            case HOW_ITS_WORK: {
                appTitle = getString(R.string.how_its_work_title);
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
        savedInstanceState.putInt(SELECTED_CITY_ID, mSelectedCityID);
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
        hideProgress();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            mSelectedMenuPosition = 0;

            Fragment aboutFragment = getFragmentManager().findFragmentByTag(ABOUT_FRAGMENT_TAG);
            if (aboutFragment != null) {
                getFragmentManager().beginTransaction().
                        remove((AboutUsFagment) aboutFragment).commit();
                initToolbar(getString(R.string.app_name));
            }

            Fragment settingsFragment = getFragmentManager().findFragmentByTag(SETTINGS_FRAGMENT_TAG);
            if (settingsFragment != null) {
                getFragmentManager().beginTransaction().
                        remove((SettingsFagment) settingsFragment).commit();

                initToolbar(getString(R.string.app_name));
                if (PreferencesManager.isMapLimitSetted() && mMap != null) {
                    CameraUtils.moveToCenterCity(mMap, false, false);
                }
            }

            Fragment howItsFragment = getFragmentManager().findFragmentByTag(HOW_ITS_WORK_TAG);
            if (howItsFragment != null) {
                getFragmentManager().beginTransaction().
                        remove((HowItsWorkFagment) howItsFragment).commit();
                initToolbar(getString(R.string.app_name));
            }

            if (aboutFragment == null && settingsFragment == null && howItsFragment == null) {
                //user is on main screen now
                if (System.currentTimeMillis() - mOnRecentBackPressedTime > RECENT_BACK_PRESSED_TIME) {
                    mOnRecentBackPressedTime = System.currentTimeMillis();
                    Toast.makeText(this, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
            }
            return true;
        }
        return false;
    }

    public void initGoogleApiClient() {
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

    @Override
    public void onReceivedData(Data data, BuildingsType type) {
        hideProgress();
        onResponseSuccess(data);
    }

    @Override
    public void onDataError(int msgErrorId) {
        hideProgress();
        showSnackbarMassage(getString(msgErrorId));
    }

    public void saveSelectedCityId(int selectedCityId) {
        mSelectedCityID = selectedCityId;
    }
}
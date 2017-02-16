package com.igorko.accesibleif.fragments;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import com.igorko.accesibleif.R;
import com.igorko.accesibleif.manager.CityManager;
import com.igorko.accesibleif.manager.PreferencesManager;
import com.igorko.accesibleif.utils.DialogUtils;
import com.igorko.accesibleif.utils.LocationUtils;

/**
 * Created by Igorko on 21.10.2016.
 */

public class SettingsFagment extends PreferenceFragment {

    private CheckBoxPreference mMapLimitPreference;
    private CheckBoxPreference mLocationPreference;
    private CheckBoxPreference mEnableUpdateLocationPreference;
    private ListPreference mCityListPeference;

    public static SettingsFagment newInstance() {
        SettingsFagment settingsFagment = new SettingsFagment();
        return settingsFagment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.background_color));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_screen);

        mMapLimitPreference = (CheckBoxPreference) findPreference(getString(R.string.map_limit_preference_checkbox_id));
        mMapLimitPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                if(!PreferencesManager.isMapLimitSetted()){
                    PreferencesManager.setMapLimitSettings(true);
                }else {
                    PreferencesManager.setMapLimitSettings(false);
                }
                return true;
            }
        });

        mLocationPreference = (CheckBoxPreference) findPreference(getString(R.string.location_preference_checkbox_id));
        mLocationPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                DialogUtils.showGotoLocationSettingsAlert(getActivity(), mLocationPreference);
                return true;
            }
        });

        mEnableUpdateLocationPreference = (CheckBoxPreference) findPreference(getString(R.string.enable_update_current_location_checkbox_id));
        mEnableUpdateLocationPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                PreferencesManager.setLocationFollowingSettings(mEnableUpdateLocationPreference.isChecked());
                return true;
            }
        });

        mCityListPeference = (ListPreference) findPreference(getString(R.string.city_list_preference_id));
        mCityListPeference.setSummary(CityManager.getCurrentCity());

        mCityListPeference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                preference.setDefaultValue(CityManager.getCurrentCity());
                return true;
            }
        });

        mCityListPeference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String selectedCity = String.valueOf(newValue);
                CityManager.setCurrentCity(selectedCity);
                mCityListPeference.setSummary(selectedCity);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapLimitPreference.setChecked(PreferencesManager.isMapLimitSetted());
        mLocationPreference.setChecked(LocationUtils.getInstance().isLocationEnabled());
        mEnableUpdateLocationPreference.setChecked(PreferencesManager.isFollowingLocation());
        mCityListPeference.setSummary(getSavedCurrentCity());
    }

    private String getSavedCurrentCity() {
        String currentCity = CityManager.getCurrentCity();
        if(!currentCity.isEmpty()){
            mCityListPeference.setSummary(CityManager.getCurrentCity());
        }else{
            String[] cityesArray = getResources().getStringArray(R.array.cityesArray);
            currentCity = cityesArray[0];
            mCityListPeference.setSummary(currentCity);
        }
        return currentCity;
    }
}

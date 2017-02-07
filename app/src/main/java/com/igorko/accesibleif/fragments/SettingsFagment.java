package com.igorko.accesibleif.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import com.igorko.accesibleif.R;
import com.igorko.accesibleif.manager.PreferencesManager;
import com.igorko.accesibleif.utils.DialogUtils;
import com.igorko.accesibleif.utils.LocationUtils;

/**
 * Created by Igorko on 21.10.2016.
 */

public class SettingsFagment extends PreferenceFragment {

    private Activity mActivity;
    private CheckBoxPreference mMapLimitPreference;
    private CheckBoxPreference mLocationPreference;
    private CheckBoxPreference mEnableUpdateLocationPreference;

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

        mActivity = getActivity();
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
                DialogUtils.showGotoLocationSettings(mActivity, mLocationPreference);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapLimitPreference.setChecked(PreferencesManager.isMapLimitSetted());
        mLocationPreference.setChecked(LocationUtils.getInstance().isLocationEnabled());
        mEnableUpdateLocationPreference.setChecked(PreferencesManager.isFollowingLocation());
    }
}

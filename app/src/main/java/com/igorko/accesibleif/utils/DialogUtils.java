package com.igorko.accesibleif.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.preference.CheckBoxPreference;
import com.igorko.accesibleif.R;
import com.igorko.accesibleif.activity.MainActivity;
import com.igorko.accesibleif.manager.CityManager;
import com.igorko.accesibleif.manager.CityStorage;
import com.igorko.accesibleif.manager.PreferencesManager;
import com.igorko.accesibleif.models.City;

/**
 * Created by Igorko on 24.10.2016.
 */

public class DialogUtils {

    public static void showSelectCityAlert(final Activity activity, final int selectedCityID){
        CityManager cityManager = new CityManager();
        String[] citiesNames = cityManager.getCitiesNames();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(activity.getString(R.string.select_current_city))
                .setSingleChoiceItems(citiesNames, selectedCityID, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedCityId) {
                        dialog.dismiss();
                        ((MainActivity)activity).saveSelectedCityId(selectedCityId);

                        CityStorage cityStorage = CityStorage.getInstance();
                        City selectedCity = cityStorage.getCityById(selectedCityId);
                        CityManager cityManager = new CityManager();
                        cityManager.setCurrentCity(selectedCity);

                        PreferencesManager.getInstance().setAppNotFirstTime();

                        ((MainActivity)activity).moveToCenterCity(true);
                        ((MainActivity)activity).getData(Const.BuildingsType.ALL);
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
    }

    public static void showGotoOpenstreetmapSiteAlert(final Activity activity){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(activity.getLayoutInflater().inflate(R.layout.goto_site_dialog_layout, null));
        alertDialogBuilder
                .setPositiveButton(activity.getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Navigaton.goToOpenstreetmapSite(activity);
                    }
                })
                .setNegativeButton(activity.getString(R.string.btn_no), null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void showGotoLocationSettingsAlert(final Activity activity, final CheckBoxPreference locationPreference){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(activity.getLayoutInflater().inflate(R.layout.goto_location_settings_dialog_layout, null));
        alertDialogBuilder
                .setPositiveButton(activity.getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Navigaton.goToLocationSettings(activity);
                    }
                })
                .setNegativeButton(activity.getString(R.string.btn_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationPreference.setChecked(LocationUtils.getInstance().isLocationEnabled());
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        locationPreference.setChecked(LocationUtils.getInstance().isLocationEnabled());
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

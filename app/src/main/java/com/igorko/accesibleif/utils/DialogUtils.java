package com.igorko.accesibleif.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.preference.CheckBoxPreference;
import com.igorko.accesibleif.R;

/**
 * Created by Igorko on 24.10.2016.
 */

public class DialogUtils {

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
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

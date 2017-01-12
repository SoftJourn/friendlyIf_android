package com.igorko.accesibleif.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
}

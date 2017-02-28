package com.igorko.accesibleif.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Igorko on 12.10.2016.
 */

public class AppContext extends Application {

    private static AppContext mInstance;
    public static boolean isLocationServiseStarted = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static AppContext getInstance(){
        return mInstance;
    }

    /**
     * Retrieve version number of installed application based on value defined in Manifest.xml or
     * build.gradle file.
     *
     * @return string that represents a version number.
     */
    public static String getAppVersion(Context context) {
            String versionName = "";
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                versionName = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        return versionName;
    }
}
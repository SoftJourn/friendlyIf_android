package com.igorko.accesibleif.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import com.igorko.accesibleif.app.AppContext;

/**
 * Created by Igorko on 24.11.2016.
 */

public class ScreenUtils {

    public static int getScreenWidth(Activity activity){
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static boolean isTablet() {
        boolean xlarge = ((AppContext.getInstance().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean large = ((AppContext.getInstance().getInstance().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
}

package com.igorko.accesibleif.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.igorko.accesibleif.app.AppContext;

/**
 * Created by Igorko on 09.11.2016.
 */

public class NetworkUtils {

    public static boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                AppContext.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}

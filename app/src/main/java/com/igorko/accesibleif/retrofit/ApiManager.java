package com.igorko.accesibleif.retrofit;

/**
 * Created by Igorko on 10.10.2016.
 */

public class ApiManager {

    private static ApiManager mInstance;
    private static RetrofitApiProvider mApiProvider;

    private ApiManager() {
        mApiProvider = new RetrofitApiProvider();
    }

    public static ApiManager getInstance() {
        if (mInstance == null) {
            mInstance = new ApiManager();
        }
        return mInstance;
    }

    public RetrofitApiProvider getApiProvider() {
        return mApiProvider;
    }

}

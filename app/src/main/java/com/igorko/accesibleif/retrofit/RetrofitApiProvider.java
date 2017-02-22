package com.igorko.accesibleif.retrofit;

import com.igorko.accesibleif.R;
import com.igorko.accesibleif.manager.URLManager;
import com.igorko.accesibleif.models.Data;
import com.igorko.accesibleif.utils.Const;
import com.igorko.accesibleif.utils.ErrorCodes;
import com.igorko.accesibleif.utils.NetworkUtils;
import java.net.UnknownHostException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Igorko on 10.10.2016.
 */

public class RetrofitApiProvider implements Const, ErrorCodes {

    private ApiService mApiService;

    public RetrofitApiProvider() {

        mApiService = new Retrofit.Builder()
                .baseUrl(URLManager.getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }

    public void getBuildingsByType(final BuildingsType buildingsType, final NetworkCallback<Data, BuildingsType> callback) {
        Callback<Data> retrofitCallBack =  new Callback<Data>() {
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (callback != null) {
                        callback.onSuccess(response.body(), buildingsType);
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                handleError(callback, t);
            }
        };
        String url = URLManager.getDataQuery(buildingsType);
        mApiService.getData(url).enqueue(retrofitCallBack);
    }

    private void handleError(NetworkCallback<Data, BuildingsType> callback, Throwable t){
        if(!NetworkUtils.isOnline()){
            onError(NO_INTERNET_CONNECTION, callback);
        }else{
            if (t instanceof UnknownHostException){
                onError(NO_SERVER_CONNECTION, callback);
            }else{
                onError(ERROR, callback);
            }
        }
    }

    private void onError(int codeError, NetworkCallback callback){
        int msgStringId;
        switch (codeError){
            case NO_INTERNET_CONNECTION:
                msgStringId = R.string.no_internet_connection;
                break;

            case NO_SERVER_CONNECTION:
                msgStringId = R.string.no_server_connection;
                break;

            default:
                msgStringId = R.string.default_error;
                break;
        }
        if (callback != null) {
            callback.onError(msgStringId);
        }
    }
}

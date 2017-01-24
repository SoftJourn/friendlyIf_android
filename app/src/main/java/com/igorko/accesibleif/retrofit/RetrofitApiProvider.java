package com.igorko.accesibleif.retrofit;

import com.igorko.accesibleif.R;
import com.igorko.accesibleif.app.AppContext;
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
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }

    public void getAllBuildings(final NetworkCallback<Data> callback) {
        mApiService.getAllBuildings().enqueue(new Callback<Data>() {
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null){
                    if (callback != null){
                        callback.onSuccess(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                handleError(callback, t);
            }
        });
    }

    public void getPharmacyBuildings(final NetworkCallback<Data> callback) {
        mApiService.getPharmacyBuildings().enqueue(new Callback<Data>() {
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null){
                    if (callback != null){
                        callback.onSuccess(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                handleError(callback, t);
            }
        });
    }

    public void getHospitalBuildings(final NetworkCallback<Data> callback) {
        mApiService.getHospitalBuildings().enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null){
                    if (callback != null){
                        callback.onSuccess(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                handleError(callback, t);
            }
        });
    }

    public void getShopBuildings(final NetworkCallback<Data> callback) {
        mApiService.getShopBuildings().enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null){
                    if (callback != null){
                        callback.onSuccess(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                handleError(callback, t);
            }
        });
    }

    private void handleError(NetworkCallback<Data> callback, Throwable t){
        if(!NetworkUtils.isOnline(AppContext.getInstance())){
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

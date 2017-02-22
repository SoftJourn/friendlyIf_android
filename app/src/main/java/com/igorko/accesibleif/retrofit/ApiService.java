package com.igorko.accesibleif.retrofit;

/**
 * Created by Igorko on 10.10.2016.
 */

import com.igorko.accesibleif.models.Data;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {


    @GET("/api/interpreter")
    Call<Data> getData(@Query("data") String data);
}
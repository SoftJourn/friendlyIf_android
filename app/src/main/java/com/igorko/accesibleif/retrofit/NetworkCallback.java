package com.igorko.accesibleif.retrofit;

/**
 * Created by Igorko on 12.10.2016.
 */

public interface NetworkCallback<T> {

    void onSuccess(T response, int type);

    void onError(int errorMsgId);
}

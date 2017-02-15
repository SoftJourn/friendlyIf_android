package com.igorko.accesibleif.retrofit;

/**
 * Created by Igorko on 12.10.2016.
 */

public interface NetworkCallback<T, V> {

    void onSuccess(T response, V type);

    void onError(int errorMsgId);
}

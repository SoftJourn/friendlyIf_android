package com.igorko.accesibleif.manager;

import com.igorko.accesibleif.models.Data;
import com.igorko.accesibleif.retrofit.ApiManager;
import com.igorko.accesibleif.retrofit.NetworkCallback;
import com.igorko.accesibleif.utils.Const;
import com.igorko.accesibleif.utils.NetworkUtils;

/**
 * Created by Igorko on 13.02.2017.
 */

public class DataManager implements IDataManager, Const{

    private IDataListener mListener;
    private ApiManager mApiManager = ApiManager.getInstance();
// private LocalStorageMamager mLocalStorageManager = LocalStorageManager.getInstance();

    public interface IDataListener {
        void onReceivedData (Data data, BuildingsType type);
        void onDataError(int msgErrorId);
    }

   private NetworkCallback<Data, BuildingsType> mNetworkCallback = new NetworkCallback<Data, BuildingsType>() {
        @Override
        public void onSuccess(Data response, BuildingsType type) {
            if (mListener != null){
                mListener.onReceivedData(response, type);
            }
        }

        @Override
        public void onError(int errorMsgStringId) {
            if (mListener != null){
                mListener.onDataError(errorMsgStringId);
            }
        }
    };

    public DataManager(IDataListener listener) {
        this.mListener = listener;
    }

    @Override
    public void getData(BuildingsType type) {
        if(NetworkUtils.isOnline()){
            getDataFromNetwork(type);
        } else {
            getDataFromLocalStorage(type);
        }
    }


    private void getDataFromNetwork(BuildingsType type){
        mApiManager.getApiProvider().getBuildingsByType(type, mNetworkCallback);
    }


    private void getDataFromLocalStorage(BuildingsType type){
        // from storage todo
    }
}

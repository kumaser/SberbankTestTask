package com.sberbank.model.request;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sberbank.model.entities.Currency;
import com.sberbank.utils.NetworkUtils;

import java.util.List;


public class DataService extends Service {

    private DataBinder mDataBinder = new DataBinder();
    private NetworkStrategy mNetworkStrategy;
    private CacheStrategy mCacheStrategy;
    private ResponseListener mListener;

    @Override
    public void onCreate() {
        super.onCreate();
        mNetworkStrategy = new NetworkStrategy(this) {
            @Override
            void onCurrenciesReceived(List<Currency> currencies) {
                mListener.onCurrencyReceived(currencies);
                mCacheStrategy.saveDataInCache(currencies);
            }
            @Override
            void onTimeoutException() {
                mCacheStrategy.requestData();
            }
        };
        mCacheStrategy = new CacheStrategy(this) {
            @Override
            void onCurrenciesReceived(List<Currency> currencies) {
                mListener.onCurrencyReceived(currencies);
            }
        };
    }

    public void requestCurrencies(ResponseListener listener) {
        mListener = listener;
        DataRequestStrategy dataRequestStrategy;
        if (NetworkUtils.isNetworkAvailable(this)) {
            dataRequestStrategy = mNetworkStrategy;
        } else {
            dataRequestStrategy = mCacheStrategy;
        }
        dataRequestStrategy.requestData();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mDataBinder;
    }

    public class DataBinder extends Binder {
        public DataService getDataService() {
            return DataService.this;
        }
    }
}

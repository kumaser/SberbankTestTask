package com.sberbank.model;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;

import com.sberbank.model.entities.Currency;
import com.sberbank.model.request.DataService;
import com.sberbank.model.request.ResponseListener;

import java.util.List;

public abstract class ModelImpl implements Model {

    private ServiceConnection mServiceConnection;

    protected ModelImpl() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DataService mDataService = ((DataService.DataBinder) service).getDataService();
                mDataService.requestCurrencies(mResponseListener);
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
    }

    @Override
    public void requestCurrencies() {
        Intent intent = new Intent(getContext(), DataService.class);
        getContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @NonNull
    private final ResponseListener mResponseListener = new ResponseListener() {
        @Override
        public void onCurrencyReceived(List<Currency> currencies) {
            getContext().unbindService(mServiceConnection);
            onCurrenciesReceived(currencies);
        }
    };

    public abstract Context getContext();
    public abstract void onCurrenciesReceived(List<Currency> currencies);
}

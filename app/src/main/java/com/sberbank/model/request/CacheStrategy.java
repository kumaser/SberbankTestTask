package com.sberbank.model.request;


import android.content.Context;
import android.os.Handler;

import com.sberbank.model.database.DBHelper;
import com.sberbank.model.entities.Currency;

import java.util.Collections;
import java.util.List;

abstract class CacheStrategy implements DataRequestStrategy {

    private final Context mContext;
    private final DBHelper mDbHelper;

    CacheStrategy(Context context) {
        mContext = context;
        mDbHelper = new DBHelper(context, Collections.<Class>singletonList(Currency.class));
    }

    @Override
    public void requestData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Currency> currencies = mDbHelper.getAll(Currency.class);
                notifyDataReceived(currencies);
            }
        }).start();
    }

    private void notifyDataReceived(final List<Currency> currencies) {
        Handler mainHandler = new Handler(mContext.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                onCurrenciesReceived(currencies);
            }
        };
        mainHandler.post(myRunnable);
    }

    void saveDataInCache(List<Currency> currencies) {
        mDbHelper.clearTable(Currency.class);
        mDbHelper.updateTable(currencies, Currency.class);
    }

    abstract void onCurrenciesReceived(List<Currency> currencies);
}

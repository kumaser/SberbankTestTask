package com.sberbank.model.request;

import android.content.Context;
import android.os.Handler;

import com.sberbank.model.entities.Currencies;
import com.sberbank.model.entities.Currency;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

abstract class NetworkStrategy implements DataRequestStrategy {

    private static final String CURRENCIES_URL = "http://www.cbr.ru/scripts/XML_daily.asp";

    private final Context mContext;

    NetworkStrategy(Context context) {
        mContext = context;
    }

    public void requestData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(CURRENCIES_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(15 * 1000);
                    parseXml(urlConnection.getInputStream());
                } catch (SocketTimeoutException e) {
                    onTimeoutException();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void parseXml(InputStream inputStream) {
        try {
            Serializer serializer = new Persister();
            Currencies currencies = serializer.read(Currencies.class, inputStream);
            notifyDataReceived(currencies.getCurrencies());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    abstract void onCurrenciesReceived(List<Currency> currencies);
    abstract void onTimeoutException();
}

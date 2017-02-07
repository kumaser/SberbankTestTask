package com.sberbank.presenter;


import android.content.Context;

import com.sberbank.model.Model;
import com.sberbank.model.ModelImpl;
import com.sberbank.model.entities.Currency;
import com.sberbank.view.CurrenciesView;

import java.util.List;

public class PresenterImpl implements Presenter {

    private Model mModel;
    private CurrenciesView mCurrenciesView;

    public PresenterImpl(final CurrenciesView currenciesView) {
        mCurrenciesView = currenciesView;
        mModel = new ModelImpl() {
            @Override
            public Context getContext() {
                return currenciesView.getContextPresenter();
            }
            @Override
            public void onCurrenciesReceived(List<Currency> currencies) {
                currenciesView.showCurrencies(currencies);
            }
        };
    }

    @Override
    public void onResultButtonClick(double startSum, Currency startCurrency, Currency endCurrency) {
        double startValueOneUnit = startCurrency.getValueDouble() / startCurrency.getNominal();
        double endValueOneUnit = endCurrency.getValueDouble() / endCurrency.getNominal();
        double result = startSum * (startValueOneUnit / endValueOneUnit);
        mCurrenciesView.onEndSumCalculated(startSum, result, startCurrency.getName(), endCurrency.getName());
    }

    @Override
    public void onViewCreated() {
        mModel.requestCurrencies();
    }

}

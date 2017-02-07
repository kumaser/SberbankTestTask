package com.sberbank.view;


import android.content.Context;

import com.sberbank.model.entities.Currency;

import java.util.List;

public interface CurrenciesView {
    void showCurrencies(final List<Currency> currencies);
    void onEndSumCalculated(double startSum, double endSum, String startName, String endName);
    Context getContextPresenter();
}

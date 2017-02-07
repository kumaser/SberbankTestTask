package com.sberbank.presenter;

import com.sberbank.model.entities.Currency;

public interface Presenter {
    void onResultButtonClick(double startSum, Currency startCurrency, Currency endCurrency);
    void onViewCreated();
}

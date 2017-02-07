package com.sberbank.model.request;


import com.sberbank.model.entities.Currency;

import java.util.List;

public interface ResponseListener {
    void onCurrencyReceived(List<Currency> currencies);
}

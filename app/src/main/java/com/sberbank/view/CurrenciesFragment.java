package com.sberbank.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.sberbank.R;
import com.sberbank.view.dialogs.ErrorDialog;
import com.sberbank.view.dialogs.ResultDialog;
import com.sberbank.model.entities.Currency;
import com.sberbank.presenter.Presenter;
import com.sberbank.presenter.PresenterImpl;
import com.sberbank.view.adapters.CurrenciesAdapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrenciesFragment extends Fragment implements CurrenciesView {

    private static final Pattern CURRENCY_PATTERN = Pattern.compile("\\d+(\\.\\d+)?");

    private Presenter mPresenter;
    private EditText mStartSumEditText;
    private CurrenciesAdapter mStartCurrenciesAdapter;
    private CurrenciesAdapter mEndCurrenciesAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mPresenter = new PresenterImpl(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_currencies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            initAdapters();
            mPresenter.onViewCreated();
        }
        initViews(view);
    }

    private void initAdapters() {
        mStartCurrenciesAdapter = new CurrenciesAdapter(getContext());
        mEndCurrenciesAdapter = new CurrenciesAdapter(getContext());
    }

    private void initViews(View view) {
        Spinner startCurrencySpinner = (Spinner) view.findViewById(R.id.start_currency_spinner);
        startCurrencySpinner.setOnItemSelectedListener(onItemSelectedListener);
        startCurrencySpinner.setAdapter(mStartCurrenciesAdapter);
        Spinner endCurrencySpinner = (Spinner) view.findViewById(R.id.end_currency_spinner);
        endCurrencySpinner.setOnItemSelectedListener(onItemSelectedListener);
        endCurrencySpinner.setAdapter(mEndCurrenciesAdapter);
        Button resultButton = (Button) view.findViewById(R.id.result_button);
        resultButton.setOnClickListener(onResultButtonClickListener);
        mStartSumEditText = (EditText) view.findViewById(R.id.start_value_edit_text);
    }

    @NonNull
    private final AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getId() == R.id.start_currency_spinner) {
                mStartCurrenciesAdapter.updateSelection(position);
            } else if (parent.getId() == R.id.end_currency_spinner) {
                mEndCurrenciesAdapter.updateSelection(position);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    @NonNull
    private final View.OnClickListener onResultButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String inputText = mStartSumEditText.getText().toString();
            if (TextUtils.isEmpty(inputText)) {
                showErrorDialog(ErrorDialog.ERROR_EMPTY_INPUT);
                return;
            }
            Matcher matcher = CURRENCY_PATTERN.matcher(inputText);
            if (!matcher.matches()) {
                showErrorDialog(ErrorDialog.ERROR_INCORRECT_INPUT);
                return;
            }
            if (mStartCurrenciesAdapter.isEmpty() || mEndCurrenciesAdapter.isEmpty()) {
                showErrorDialog(ErrorDialog.ERROR_CURRENCIES_NOT_RECEIVED);
                return;
            }
            double startSum = Double.parseDouble(inputText);
            Currency startCurrency = mStartCurrenciesAdapter.getSelectedCurrency();
            Currency endCurrency = mEndCurrenciesAdapter.getSelectedCurrency();
            mPresenter.onResultButtonClick(startSum, startCurrency, endCurrency);
        }
    };

    private void showErrorDialog(int errorCode) {
        Bundle bundle = new Bundle();
        bundle.putInt(ErrorDialog.ERROR_CODE_KEY, errorCode);
        ErrorDialog errorDialog = ErrorDialog.newInstance(bundle);
        errorDialog.show(getFragmentManager(), "error_dialog");
    }

    @Override
    public void showCurrencies(final List<Currency> currencies) {
        mStartCurrenciesAdapter.addAll(currencies);
        mEndCurrenciesAdapter.addAll(currencies);
    }

    @Override
    public void onEndSumCalculated(double startSum, double endSum, String startName, String endName) {
        Bundle bundle = new Bundle();
        bundle.putString(ResultDialog.START_CURRENCY_NAME, startName);
        bundle.putString(ResultDialog.END_CURRENCY_NAME, endName);
        bundle.putDouble(ResultDialog.START_SUM, startSum);
        bundle.putDouble(ResultDialog.END_SUM, endSum);
        ResultDialog resultDialog = ResultDialog.newInstance(bundle);
        resultDialog.show(getFragmentManager(), "result_dialog");
    }

    @Override
    public Context getContextPresenter() {
        return getContext();
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }
}

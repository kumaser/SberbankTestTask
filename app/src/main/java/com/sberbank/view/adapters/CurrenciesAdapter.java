package com.sberbank.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.sberbank.model.entities.Currency;

public class CurrenciesAdapter extends ArrayAdapter<Currency> {

    private int mCurrentCurrencyPosition;

    public CurrenciesAdapter(Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final View view;
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        Currency currency = getItem(position);
        holder.bind(currency);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {

        final View view;
        final DropDownViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(android.R.layout.simple_list_item_single_choice, parent, false);
            holder = new DropDownViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (DropDownViewHolder) view.getTag();
        }
        Currency currency = getItem(position);
        boolean checked = mCurrentCurrencyPosition == position;
        holder.bind(currency, checked);
        return view;
    }

    public void updateSelection(int position) {
        mCurrentCurrencyPosition = position;
        notifyDataSetChanged();
    }

    public Currency getSelectedCurrency() {
        return getItem(mCurrentCurrencyPosition);
    }

    private static class ViewHolder {
        private TextView mTitle;

        ViewHolder(View rootView) {
            mTitle = (TextView) rootView.findViewById(android.R.id.text1);
        }

        void bind(Currency currency) {
            mTitle.setText(currency.getName());
        }
    }

    private static class DropDownViewHolder {

        private CheckedTextView mTitle;

        DropDownViewHolder(View rootView) {
            mTitle = (CheckedTextView) rootView.findViewById(android.R.id.text1);
        }

        void bind(Currency currency, boolean checked) {
            mTitle.setText(currency.getName());
            mTitle.setChecked(checked);
        }
    }
}

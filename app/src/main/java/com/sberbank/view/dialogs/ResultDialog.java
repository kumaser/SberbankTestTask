package com.sberbank.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.sberbank.R;


public class ResultDialog extends DialogFragment {

    public static final String START_SUM = "start_sum";
    public static final String END_SUM = "end_sum";
    public static final String START_CURRENCY_NAME = "start_currency_name";
    public static final String END_CURRENCY_NAME = "end_currency_name";

    public static ResultDialog newInstance(Bundle bundle) {
        ResultDialog resultDialog = new ResultDialog();
        resultDialog.setArguments(bundle);
        return resultDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.convert_result_title);
        builder.setMessage(getString(R.string.convert_result_message,
                args.getString(START_CURRENCY_NAME),
                args.getString(END_CURRENCY_NAME),
                args.getDouble(START_SUM),
                args.getDouble(END_SUM)));
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.button_caption_ok, null);
        return builder.create();
    }
}

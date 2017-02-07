package com.sberbank.view.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.sberbank.R;

public class ErrorDialog extends DialogFragment {

    public static final String ERROR_CODE_KEY = "error_code_key";

    public static final int ERROR_EMPTY_INPUT = 0;
    public static final int ERROR_INCORRECT_INPUT = 1;
    public static final int ERROR_CURRENCIES_NOT_RECEIVED = 2;

    public static ErrorDialog newInstance(Bundle bundle) {
        ErrorDialog errorDialog = new ErrorDialog();
        errorDialog.setArguments(bundle);
        return errorDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.error);
        builder.setMessage(getMessage());
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.button_caption_ok, null);
        return builder.create();
    }

    private int getMessage() {
        Bundle arguments = getArguments();
        int errorCode = arguments.getInt(ERROR_CODE_KEY);
        if (errorCode == ERROR_EMPTY_INPUT) {
            return R.string.error_start_sum_empty;
        } else if (errorCode == ERROR_INCORRECT_INPUT) {
            return R.string.error_start_sum_incorrect;
        } else if (errorCode == ERROR_CURRENCIES_NOT_RECEIVED) {
            return R.string.error_currencies_not_received;
        } else {
            return R.string.error_unknown;
        }
    }
}

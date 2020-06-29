package com.example.myapplication2;

import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

public class MessageDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        /**
         * Called when the dialog is dismissed.
         */
        void onMessageDialogDismissed();
    }

    private static final String ARG_MESSAGE = "message";

    /**
     * Creates a new instance of {@link MessageDialogFragment}.
     *
     * @param message The message to be shown on the dialog.
     * @return A newly created dialog fragment.
     */
    public static MessageDialogFragment newInstance(String message) {
        final MessageDialogFragment fragment = new MessageDialogFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setMessage(getArguments().getString(ARG_MESSAGE))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Listener) getActivity()).onMessageDialogDismissed();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        ((Listener) getActivity()).onMessageDialogDismissed();
                    }
                })
                .create();
    }

}

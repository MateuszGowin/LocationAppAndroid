package com.example.myapplication.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class EnabledUserDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Musisz najpierw aktywować konto, kliknij w otrzymany drogą mailową link.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("dialogValue",true);
                        getParentFragmentManager().setFragmentResult("enabledUserDialog",bundle);
                    }
                });
        return builder.create();

    }
}

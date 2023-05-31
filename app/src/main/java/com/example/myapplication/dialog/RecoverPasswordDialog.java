package com.example.myapplication.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class RecoverPasswordDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Na podany adres email wysłano wiadomość z linkiem resetującym hasło")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("dialogValue",true);
                        getParentFragmentManager().setFragmentResult("recoverPasswordDialog",bundle);
                    }
                });
        return builder.create();

    }
}

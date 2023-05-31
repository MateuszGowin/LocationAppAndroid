package com.example.myapplication.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.dialog.RecoverPasswordDialog;
import com.example.myapplication.retrofit.ApiService;
import com.example.myapplication.retrofit.ServiceGenerator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecoverPasswordFragment extends Fragment implements View.OnClickListener, FragmentResultListener {
    private FloatingActionButton recoverPassword;
    private NavController navController;
    private ApiService apiService;
    private EditText email;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recover_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        recoverPassword = view.findViewById(R.id.recoverPassword);
        email = view.findViewById(R.id.email);
        recoverPassword.setOnClickListener(this);
        progressBar = view.findViewById(R.id.progressBar);
        getChildFragmentManager().setFragmentResultListener("recoverPasswordDialog",getViewLifecycleOwner(),this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.recoverPassword) {
            recoverPassword();
        }
    }
    private void recoverPassword() {
        showDialog();
        apiService = ServiceGenerator.createService(ApiService.class,getActivity());
        apiService.recoverPassword(email.getText().toString()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()) {
                    if(response.code() == 404) {
                        hideDialog();
                        email.setError("Nieprawidłowy adres email");
                    }
                } else {
                    hideDialog();
                    RecoverPasswordDialog recoverPasswordDialog = new RecoverPasswordDialog();
                    recoverPasswordDialog.show(getChildFragmentManager(),"Recover password dialog");
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

    }

    @Override
    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        if(requestKey.equals("recoverPasswordDialog")) {
            if(result.containsKey("dialogValue")) {
                navController.navigateUp();
            }
        }
    }
    private void showDialog() {
        progressBar.setVisibility(View.VISIBLE);
    }
    private void hideDialog() {
        if(progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}

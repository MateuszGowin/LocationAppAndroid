package com.example.myapplication.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.dialog.EnabledUserDialog;
import com.example.myapplication.model.JwtResponse;
import com.example.myapplication.model.LoginRequest;
import com.example.myapplication.R;
import com.example.myapplication.retrofit.ServiceGenerator;
import com.example.myapplication.retrofit.ApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements View.OnClickListener, FragmentResultListener, TextWatcher {
    private FloatingActionButton btn_login;
    private NavController navController;
    private ApiService apiService;
    private Button btn_register;
    private TextView forgotPassword;

    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputEditText emailInputEditText;
    private TextInputEditText passwordInputEditText;
    private TextView loginError;
    private LinearProgressIndicator linearProgressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*SharedPreferences sharedPreferences = getContext().getSharedPreferences(getContext().getResources().getString(R.string.ACCESSTOKEN),Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        String auth = sharedPreferences.getString("accessToken","");*/
        navController = Navigation.findNavController(view);

        btn_login = view.findViewById(R.id.btn_login);
        btn_register = view.findViewById(R.id.btn_register);
        linearProgressBar = view.findViewById(R.id.linearProgressBar);
        forgotPassword = view.findViewById(R.id.forgotPassword);
        emailInputEditText = view.findViewById(R.id.emailInputEditText);
        passwordInputEditText = view.findViewById(R.id.passwordInputEditText);
        emailInputLayout = view.findViewById(R.id.emailInputLayout);
        passwordInputLayout = view.findViewById(R.id.passwordInputLayout);
        loginError = view.findViewById(R.id.loginError);
        forgotPassword.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        emailInputEditText.addTextChangedListener(this);
        passwordInputEditText.addTextChangedListener(this);
    }
    private void showDialog() {
        linearProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideDialog() {
        if(linearProgressBar.getVisibility() == View.VISIBLE) {
            linearProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_login) {
            if(validateLoginForm()) {
                signIn();
            }
        } else if(id == R.id.btn_register) {
            navController.navigate(R.id.action_loginFragment_to_registerFragment);
        } else if(id == R.id.forgotPassword) {
            navController.navigate(R.id.action_loginFragment_to_recoverPasswordFragment);
        }
    }

    private boolean validateLoginForm() {
        String email = emailInputEditText.getText().toString();
        String password = passwordInputEditText.getText().toString();
        boolean isValid = true;
        if(TextUtils.isEmpty(email)) {
            emailInputLayout.setError(getString(R.string.error_field_required));
            isValid = false;
        }
        if(TextUtils.isEmpty(password)) {
            passwordInputLayout.setError(getString(R.string.error_field_required));
            isValid = false;
        }
        return isValid;
    }

    private void signIn() {
        showDialog();
        apiService = ServiceGenerator.createService(ApiService.class,getActivity());
        apiService.authenticateUser(new LoginRequest(emailInputEditText.getText().toString(),passwordInputEditText.getText().toString())).enqueue(new Callback<JwtResponse>() {
            @Override
            public void onResponse(@NonNull Call<JwtResponse> call, @NonNull Response<JwtResponse> response) {
                hideDialog();
                if(response.isSuccessful() && response.body() != null) {

                    if(!response.body().isEnabled()){
                        EnabledUserDialog enabledUserDialog = new EnabledUserDialog();
                        enabledUserDialog.show(getChildFragmentManager(),"User not enabled dialog");
                    } else {
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getContext().getResources().getString(R.string.ACCESSTOKEN),Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token",response.body().getAccessToken());
                        editor.putLong("userId",response.body().getId());
                        editor.putString("userFirstName",response.body().getFirstName());
                        editor.putString("userLastName",response.body().getLastName());
                        editor.apply();
                        navController.navigateUp();
                    }
                } else {
                    loginError.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(@NonNull Call<JwtResponse> call,@NonNull Throwable t) {
                Log.d("Error",t.getMessage());
                hideDialog();
            }
        });
    }

    @Override
    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        if(requestKey.equals("enabledUserDialog")) {
            if(result.containsKey("dialogValue")) {
                navController.navigateUp();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String email = emailInputEditText.getText().toString();
        String password = passwordInputEditText.getText().toString();
        if(editable.toString().equals(email)) {
            emailInputLayout.setError(null);
        }
        if(editable.toString().equals(password)) {
            passwordInputLayout.setError(null);
        }
    }
}

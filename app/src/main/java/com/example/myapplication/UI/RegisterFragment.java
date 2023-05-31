package com.example.myapplication.UI;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.model.RegisterRequest;
import com.example.myapplication.retrofit.ServiceGenerator;
import com.example.myapplication.model.Opinion;
import com.example.myapplication.retrofit.ApiService;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment implements View.OnClickListener, TextWatcher {
    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputLayout firstNameInputLayout;
    private TextInputLayout lastNameInputLayout;
    private TextInputLayout confirmPasswordInputLayout;
    private TextInputEditText emailInputEditText;
    private TextInputEditText passwordInputEditText;
    private TextInputEditText confirmPasswordInputEditText;
    private TextInputEditText firstNameInputEditText;
    private TextInputEditText lastNameInputEditText;
    private NavController navController;
    private ApiService apiService;
    private Button register;
    private LinearProgressIndicator linearProgressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailInputEditText = view.findViewById(R.id.emailInputEditText);
        passwordInputEditText = view.findViewById(R.id.passwordInputEditText);
        confirmPasswordInputEditText = view.findViewById(R.id.confirmPasswordInputEditText);
        firstNameInputEditText = view.findViewById(R.id.firstNameInputEditText);
        lastNameInputEditText = view.findViewById(R.id.lastNameInputEditText);
        emailInputLayout = view.findViewById(R.id.emailInputLayout);
        passwordInputLayout = view.findViewById(R.id.passwordInputLayout);
        confirmPasswordInputLayout = view.findViewById(R.id.confirmPasswordInputLayout);
        firstNameInputLayout = view.findViewById(R.id.firstNameInputLayout);
        lastNameInputLayout = view.findViewById(R.id.lastNameInputLayout);
        emailInputEditText.addTextChangedListener(this);
        passwordInputEditText.addTextChangedListener(this);
        confirmPasswordInputEditText.addTextChangedListener(this);
        firstNameInputEditText.addTextChangedListener(this);
        lastNameInputEditText.addTextChangedListener(this);

        navController = Navigation.findNavController(view);
        apiService = ServiceGenerator.createService(ApiService.class,getActivity());
        register = view.findViewById(R.id.btn_register);
        linearProgressBar = view.findViewById(R.id.linearProgressBar);
        register.setOnClickListener(this);
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
        if(id == R.id.btn_register) {
            if(validateRegisterForm()) {
                registerNewUser();
            }
        }
    }

    private boolean validateRegisterForm() {
        boolean isValid = true;
        if(!validateEmail())
            isValid = false;
        if(!validatePassword())
            isValid = false;
        if(!validateFirstName())
            isValid = false;
        if(!validateLastName())
            isValid = false;
        return isValid;

    }

    private void registerNewUser() {
        showDialog();
        RegisterRequest registerRequest =
                new RegisterRequest(emailInputEditText.getText().toString(),
                        passwordInputEditText.getText().toString(),
                        firstNameInputEditText.getText().toString(),
                        lastNameInputEditText.getText().toString());
        apiService.createUser(registerRequest).enqueue(new Callback<Opinion>() {
            @Override
            public void onResponse(@NonNull Call<Opinion> call, @NonNull Response<Opinion> response) {
                if(response.isSuccessful()) {
                    hideDialog();
                    Toast.makeText(getActivity(),"Pomyślnie zarejestrowano",Toast.LENGTH_SHORT).show();
                    navController.navigateUp();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Opinion> call, @NonNull Throwable t) {
                hideDialog();
            }
        });
    }
    private boolean validatePassword() {
        String password = passwordInputEditText.getText().toString();
        String confirmPassword = confirmPasswordInputEditText.getText().toString();
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        if(TextUtils.isEmpty(password)) {
            passwordInputLayout.setError(getString(R.string.error_field_required));
            return false;
        } else if(!password.matches(PASSWORD_PATTERN)) {
            passwordInputLayout.setError("Hasło musi składać się z:\n" +
                    "- co najmniej 1 małej litery\n" +
                    "- co najmniej 1 dużej litery\n" +
                    "- co najmniej 1 cyfry\n" +
                    "- oraz być dłuższe niż 8 znaków\n");
            return false;
        } else if(!password.equals(confirmPassword)) {
            confirmPasswordInputLayout.setError("Hasła muszą być identyczne");
            return false;
        }
        return true;
    }
    private boolean validateEmail() {
        String email = emailInputEditText.getText().toString();
        if(TextUtils.isEmpty(email)) {
            emailInputLayout.setError(getString(R.string.error_field_required));
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.setError(getString(R.string.error_invalid_email));
            return false;
        }
        return true;
    }
    private boolean validateFirstName() {
        String firstName = firstNameInputEditText.getText().toString();
        if(TextUtils.isEmpty(firstName)) {
            firstNameInputLayout.setError(getString(R.string.error_field_required));
            return false;
        }
        return true;
    }
    private boolean validateLastName() {
        String lastName = lastNameInputEditText.getText().toString();
        if(TextUtils.isEmpty(lastName)) {
            lastNameInputLayout.setError(getString(R.string.error_field_required));
            return false;
        }
        return true;
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
        String confirmPassword = confirmPasswordInputEditText.getText().toString();
        String firstName = firstNameInputEditText.getText().toString();
        String lastName = lastNameInputEditText.getText().toString();
        if(editable.toString().equals(email)) {
            emailInputLayout.setError(null);
        }
        if(editable.toString().equals(password)) {
            passwordInputLayout.setError(null);
        }
        if(editable.toString().equals(confirmPassword)) {
            confirmPasswordInputLayout.setError(null);
        }
        if(editable.toString().equals(firstName)) {
            firstNameInputLayout.setError(null);
        }
        if(editable.toString().equals(lastName)) {
            lastNameInputLayout.setError(null);
        }
    }
}

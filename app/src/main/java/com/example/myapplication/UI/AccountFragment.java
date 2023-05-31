package com.example.myapplication.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.retrofit.ServiceGenerator;
import com.example.myapplication.retrofit.ApiService;

public class AccountFragment extends Fragment implements View.OnClickListener {
    private TextView user_name;
    private Button login;
    private NavController navController;
    private LinearLayout after_login;
    private LinearLayout befor_login;
    private ImageButton logout;
    private ApiService apiService;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        apiService = ServiceGenerator.createService(ApiService.class,getActivity());
        user_name = view.findViewById(R.id.user_name);
        login = view.findViewById(R.id.login);
        after_login = view.findViewById(R.id.after_login);
        befor_login = view.findViewById(R.id.befor_login);
        logout = view.findViewById(R.id.logout);
        progressBar = view.findViewById(R.id.progressBar);
        sharedPreferences = getActivity().getSharedPreferences(getContext().getResources().getString(R.string.ACCESSTOKEN), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");
        if(!token.equals("")) {
            String name = sharedPreferences.getString("userFirstName","") + " " + sharedPreferences.getString("userLastName","");
            user_name.setText(name);
            setAfterLoginVisible();
        } else {
            setBeforLoginVisible();
        }
        login.setOnClickListener(this);
        logout.setOnClickListener(this);
    }
    public void setAfterLoginVisible(){
        after_login.setVisibility(View.VISIBLE);
        befor_login.setVisibility(View.GONE);
    }
    public void setBeforLoginVisible() {
        after_login.setVisibility(View.GONE);
        befor_login.setVisibility(View.VISIBLE);
    }
    private void showDialog() {
        progressBar.setVisibility(View.VISIBLE);
    }
    private void hideDialog() {
        if(progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.logout) {
            showDialog();
            sharedPreferences.edit().clear().apply();
            setBeforLoginVisible();
            hideDialog();
        } else if(id == R.id.login) {
            navController.navigate(R.id.action_accountFragment_to_loginFragment);
        }
    }
}

package com.example.myapplication.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.dialog.LoginDialog;
import com.example.myapplication.R;
import com.example.myapplication.adapter.OpinionsAdapter;
import com.example.myapplication.model.Opinion;
import com.example.myapplication.model.Place;
import com.example.myapplication.model.PlaceViewModel;

import java.util.List;

public class OpinionsFragment extends Fragment implements View.OnClickListener,FragmentResultListener{
    private RecyclerView recyclerView;
    private PlaceViewModel placeViewModel;
    private Button btn_add_opinion;
    private NavController navController;
    private TextView noOpinion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_opinions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        noOpinion = view.findViewById(R.id.noOpinion);
        btn_add_opinion = view.findViewById(R.id.btn_add_opinion);

        getChildFragmentManager().setFragmentResultListener("requestKey", getViewLifecycleOwner(),this);
        btn_add_opinion.setOnClickListener(this);

        placeViewModel = new ViewModelProvider(requireActivity()).get(PlaceViewModel.class);
        recyclerView = view.findViewById(R.id.recyclerview_opinions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        placeViewModel.getSelectedPlace().observe(getViewLifecycleOwner(), new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                Place p = placeViewModel.getSelectedPlace().getValue();
                List<Opinion> opinions = placeViewModel.getSelectedPlace().getValue().getOpinions();
                if(opinions.isEmpty())
                    noOpinion.setVisibility(View.VISIBLE);
                else
                    noOpinion.setVisibility(View.GONE);

                recyclerView.setAdapter(new OpinionsAdapter(opinions));
            }
        });
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_add_opinion) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getContext().getResources().getString(R.string.ACCESSTOKEN), Context.MODE_PRIVATE);

            String authToken = sharedPreferences.getString("token","");
            if(authToken.equals("")) {
                LoginDialog loginDialog = new LoginDialog();
                loginDialog.show(getChildFragmentManager(),"Login Dialog");
            } else {
                navController.navigate(R.id.action_opinionsFragment_to_addOpinionFragment);
            }
        }
    }

    @Override
    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
        boolean result = bundle.getBoolean("Key");
        if (result)
            navController.navigate(R.id.action_opinionsFragment_to_loginFragment);
    }
}

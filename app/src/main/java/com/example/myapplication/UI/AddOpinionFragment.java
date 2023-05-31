package com.example.myapplication.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.model.AddOpinionRequest;
import com.example.myapplication.R;
import com.example.myapplication.retrofit.ServiceGenerator;
import com.example.myapplication.model.Opinion;
import com.example.myapplication.model.Place;
import com.example.myapplication.model.PlaceViewModel;
import com.example.myapplication.retrofit.ApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOpinionFragment extends Fragment {
    private NavController navController;
    private PlaceViewModel placeViewModel;
    private ApiService apiService;
    private TextInputEditText editTextComment;
    private FloatingActionButton btn_send_opinion;
    private RatingBar ratingBar_opinion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_opinion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        btn_send_opinion = view.findViewById(R.id.btn_send_opinion);
        ratingBar_opinion = view.findViewById(R.id.ratingBar_opinion);
        editTextComment = view.findViewById(R.id.editText_comment);
        apiService = ServiceGenerator.createService(ApiService.class,getActivity());
        placeViewModel = new ViewModelProvider(requireActivity()).get(PlaceViewModel.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getContext().getResources().getString(R.string.ACCESSTOKEN), Context.MODE_PRIVATE);
        Long userId = sharedPreferences.getLong("userId", 0);
        Place place = placeViewModel.getSelectedPlace().getValue();

        btn_send_opinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rate = (int) ratingBar_opinion.getRating();
                String comment = String.valueOf(editTextComment.getText());
                AddOpinionRequest opinion = new AddOpinionRequest(rate,comment,new Date());
                apiService.addOpinion(place.getId(),userId,opinion).enqueue(new Callback<Opinion>() {
                    @Override
                    public void onResponse(Call<Opinion> call, Response<Opinion> response) {
                        if(response.isSuccessful()) {
                            updatePlace(place.getId());
                            navController.navigateUp();
                            System.out.println("Dodano opinie");
                        }
                    }

                    @Override
                    public void onFailure(Call<Opinion> call, Throwable t) {

                    }
                });

            }
        });
    }
    private void updatePlace(Long id) {
        apiService.getPlaceById(id).enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                if(response.isSuccessful()) {
                    placeViewModel.setData(response.body());
                }
            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {

            }
        });
    }
}

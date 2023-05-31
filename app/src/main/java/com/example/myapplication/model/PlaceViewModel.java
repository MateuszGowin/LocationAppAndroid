package com.example.myapplication.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlaceViewModel extends ViewModel {
    private final MutableLiveData<Place> selectedPlace = new MutableLiveData<Place>();

    public void setData(Place place) {
        selectedPlace.setValue(place);
    }

    public LiveData<Place> getSelectedPlace() {
        return selectedPlace;
    }
}

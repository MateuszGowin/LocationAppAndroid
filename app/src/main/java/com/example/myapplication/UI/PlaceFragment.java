package com.example.myapplication.UI;

import android.content.Context;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.dialog.PlaceDialog;
import com.example.myapplication.model.AddPlaceRequest;
import com.example.myapplication.model.Address;
import com.example.myapplication.model.Place;
import com.example.myapplication.model.Type;
import com.example.myapplication.retrofit.ApiService;
import com.example.myapplication.retrofit.ServiceGenerator;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceFragment extends Fragment implements
        View.OnClickListener,
        View.OnFocusChangeListener,
        TextWatcher,
        FragmentResultListener {
    private LinearLayout addGeopoint;
    private NavController navController;
    private ApiService apiService;
    private EditText name;
    private EditText city;
    private EditText street;
    private EditText number;
    private TextView geoPoint;
    private Button addNewPlace;
    private String[] items;
    private AutoCompleteTextView typ;
    private TextView address;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        name = view.findViewById(R.id.name);
        city = view.findViewById(R.id.city);
        street = view.findViewById(R.id.street);
        number = view.findViewById(R.id.number);
        geoPoint = view.findViewById(R.id.geopoint);
        addNewPlace = view.findViewById(R.id.addNewPlace);
        addNewPlace.setOnClickListener(this);
        typ = view.findViewById(R.id.dropdown_menu);
        addGeopoint = view.findViewById(R.id.addGeopoint);
        addGeopoint.setOnClickListener(this);
        address = view.findViewById(R.id.address);
        items = new String[Type.values().length];
        for (int i = 0; i < Type.values().length; i++) {
            items[i] = mapTypeToString(Type.values()[i]);
        }

        name.setOnFocusChangeListener(this);
        city.setOnFocusChangeListener(this);
        street.setOnFocusChangeListener(this);
        number.setOnFocusChangeListener(this);
        typ.addTextChangedListener(this);

        getParentFragmentManager().setFragmentResultListener("marker_click",getViewLifecycleOwner(),this);
        getChildFragmentManager().setFragmentResultListener("placeDialog",getViewLifecycleOwner(),this);


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.addGeopoint) {
            Toast.makeText(getActivity(),"Dodaj wspolrzedne",Toast.LENGTH_SHORT).show();
            navController.navigate(R.id.action_placeFragment_to_getCoordinatesFragment);
        } else if (id == R.id.addNewPlace) {
            addNewPlace();
        }
    }

    private void addNewPlace() {
        if(validateForm(new EditText[]{name, city, street, number},typ,geoPoint)) {
            AddPlaceRequest addPlaceRequest = getPlaceFromLayout();
            apiService = ServiceGenerator.createService(ApiService.class,getActivity());
            apiService.createPlace(addPlaceRequest).enqueue(new Callback<Place>() {
                @Override
                public void onResponse(Call<Place> call, Response<Place> response) {
                    if(response.isSuccessful()) {
                        PlaceDialog placeDialog = new PlaceDialog();
                        placeDialog.show(getChildFragmentManager(),"Place dialog");
                    }
                }

                @Override
                public void onFailure(Call<Place> call, Throwable t) {

                }
            });
        }
    }
    private AddPlaceRequest getPlaceFromLayout() {
        String name = this.name.getText().toString().trim();
        String city = this.city.getText().toString().trim();
        String street = this.street.getText().toString().trim();
        int number = Integer.parseInt(this.number.getText().toString().trim());
        Address address = new Address(street,number,city);
        Type type = mapStringToType(String.valueOf(typ.getText()));
        String geoPoint = this.geoPoint.getText().toString();
        String[] parts= geoPoint.substring(geoPoint.indexOf("(")+1,geoPoint.indexOf(")")).split(",");
        BigDecimal lat = new BigDecimal(parts[0]);
        BigDecimal lng = new BigDecimal(parts[1]);

        return new AddPlaceRequest(name,address,lng,lat,type);
    }

    private String mapTypeToString(Type type) {
        switch(type) {
            case CINEMA:
                return "Kino";
            case RESTAURANT:
                return "Restauracja";
            case POOL:
                return "Basen";
            case THEATER:
                return "Teatr";
            case GYM:
                return "Siłownia";
            case MUSEUM:
                return "Muzeum";
            case PARK:
                return "Park";
            case NIGHT_LIFE:
                return "Życie nocne";
            case TOURIST_ATTRACTION:
                return "Atrakcja turystyczna";
            case ZOO:
                return "Zoo";
            default:
                throw new IllegalArgumentException("Nieobsługiwany typ: " + type);
        }
    }
    private Type mapStringToType(String string) {
        switch(string) {
            case "Kino":
                return Type.CINEMA;
            case "Restauracja":
                return Type.RESTAURANT;
            case "Basen":
                return Type.POOL;
            case "Teatr":
                return Type.THEATER;
            case "Siłownia":
                return Type.GYM;
            case "Muzeum":
                return Type.MUSEUM;
            case "Park":
                return Type.PARK;
            case "Życie nocne":
                return Type.NIGHT_LIFE;
            case "Atrakcja turystyczna":
                return Type.TOURIST_ATTRACTION;
            case "Zoo":
                return Type.ZOO;
            default:
                throw new IllegalArgumentException("Brak typu dla " + string);
        }
    }

    private boolean validateForm(EditText[] editTexts, AutoCompleteTextView autoCompleteTextView, TextView textView) {
        boolean isValid = true;
        for(EditText editText: editTexts) {
            String text = editText.getText().toString().trim();
            if(text.isEmpty()) {
                editText.setError("To pole nie może być puste");
                isValid = false;
            }
        }
        if (!Arrays.asList(items).contains(autoCompleteTextView.getText().toString())) {
            autoCompleteTextView.setError("Nie wybrano żadnego typu");
            isValid = false;
        }
        if (textView.getText().toString().equals("(lokalizacja)")) {
            textView.setError("Nie wybrano lokalizacji");
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onFocusChange(View view, boolean hasfocus) {
        if(!hasfocus) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        if(editable.toString().equals(typ.getText().toString())) {
            typ.setError(null);
        }
    }

    @Override
    public void onResume() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.item_dropdown_type,items);
        typ.setAdapter(arrayAdapter);
        super.onResume();
    }

    @Override
    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        if(requestKey.equals("marker_click")) {
            if(result.containsKey("position")) {
                LatLng postion = result.getParcelable("position");
                String address = getAddressFromLatLng(postion);
                String latlan = String.format("(%.6f %.6f)",postion.latitude,postion.longitude);
                latlan = latlan.replace(",",".");
                latlan = latlan.replace(" ",",");
                geoPoint.setText(latlan);
                this.address.setText(address);
            }
        } else if(requestKey.equals("placeDialog")) {
            if(result.containsKey("dialogValue")) {
                navController.navigateUp();
            }
        }
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder =new Geocoder(getContext(), Locale.getDefault());
        try {
            List<android.location.Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude,1);
            for(android.location.Address address: addressList) {
                System.out.println("Adres: " + address.getAddressLine(0));
                return address.getAddressLine(0);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}

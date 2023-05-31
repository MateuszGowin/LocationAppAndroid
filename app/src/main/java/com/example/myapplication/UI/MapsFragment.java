package com.example.myapplication.UI;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.retrofit.ServiceGenerator;
import com.example.myapplication.model.Opinion;
import com.example.myapplication.model.Place;
import com.example.myapplication.model.PlaceViewModel;
import com.example.myapplication.model.Type;
import com.example.myapplication.retrofit.ApiService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapsFragment extends Fragment implements
        OnMapReadyCallback,
        View.OnClickListener ,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener {
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Location myLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private ImageButton btn_nearby_search;
    private ImageButton btn_radius;
    private ImageButton btn_filtr;
    private ImageButton btn_follow;
    private int radius;
    private List<Type> filterList;
    private ApiService apiService;
    private boolean follow;
    private Map<Marker,Place> markers;
    private LinearLayout bottomsheetLayout;
    private BottomSheetBehavior<View> sheetBehaviorMarker;
    private TextView tv_name;
    private TextView tv_type;
    private TextView tv_address;
    private TextView tv_latlang;
    private TextView tv_opinionsCount;
    private LinearLayout bottomsheetLayoutFiltr;
    private BottomSheetBehavior<View> sheetBehaviorFiltr;
    private LinearLayout bottomsheetLayoutRadius;
    private BottomSheetBehavior<View> sheetBehaviorRadius;
    private ChipGroup group_chip_filter;
    private RadioGroup radio_group_radius;

    private Button btn_filtr_submit;
    private Button btn_radius_submit;
    private ImageButton btn_navigation;
    private TextView tv_radius;
    private TextView rate_score;
    private NavController navController;

    private PlaceViewModel placeViewModel;
    private LinearLayout linearlayout_opinions;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_nearby_search = view.findViewById(R.id.btn_nearby_search);
        btn_radius = view.findViewById(R.id.btn_radius);
        btn_filtr = view.findViewById(R.id.btn_filtr);
        btn_follow = view.findViewById(R.id.btn_follow);
        btn_nearby_search.setOnClickListener(this);
        btn_radius.setOnClickListener(this);
        btn_filtr.setOnClickListener(this);
        btn_follow.setOnClickListener(this);
        radius = 500;
        follow = false;


        bottomsheetLayout = view.findViewById(R.id.bottom_sheet_layout);
        sheetBehaviorMarker = BottomSheetBehavior.from(bottomsheetLayout);

        sheetBehaviorMarker.setState(BottomSheetBehavior.STATE_HIDDEN);
        tv_name = view.findViewById(R.id.tv_name);
        tv_type = view.findViewById(R.id.tv_type);
        tv_address = view.findViewById(R.id.tv_address);
        tv_latlang = view.findViewById(R.id.tv_latlang);
        tv_opinionsCount = view.findViewById(R.id.tv_opinionsCount);

        bottomsheetLayoutFiltr = view.findViewById(R.id.bottom_sheet_layout_filtr);
        sheetBehaviorFiltr = BottomSheetBehavior.from(bottomsheetLayoutFiltr);

        bottomsheetLayoutRadius = view.findViewById(R.id.bottom_sheet_layout_radius);
        sheetBehaviorRadius = BottomSheetBehavior.from(bottomsheetLayoutRadius);

        group_chip_filter = view.findViewById(R.id.group_chip_filter);
        radio_group_radius = view.findViewById(R.id.radio_group_radius);


        btn_filtr_submit = view.findViewById(R.id.btn_filtr_submit);
        btn_filtr_submit.setOnClickListener(this);

        btn_radius_submit = view.findViewById(R.id.btn_radius_submit);
        btn_radius_submit.setOnClickListener(this);

        btn_navigation = view.findViewById(R.id.btn_navigation);
        btn_navigation.setOnClickListener(this);

        tv_radius = view.findViewById(R.id.tv_radius);
        rate_score = view.findViewById(R.id.rate_score);

        linearlayout_opinions = view.findViewById(R.id.linearlayout_opinions);
        linearlayout_opinions.setOnClickListener(this);
        navController = Navigation.findNavController(view);

        placeViewModel = new ViewModelProvider(requireActivity()).get(PlaceViewModel.class);

        placeViewModel.getSelectedPlace().observe(getViewLifecycleOwner(), place->{
            tv_name.setText(place.getName());
            tv_latlang.setText(place.getLatitude()+","+place.getLongitude());
            tv_address.setText(place.getAddress().getCity() +" "+place.getAddress().getStreet() + " " + place.getAddress().getNumber());
            tv_type.setText(place.getType().toString());
            Double avg2 = place.getOpinions().stream().mapToDouble(Opinion::getRate).average().orElse(0.0);
            rate_score.setText(String.format("%.1f",avg2));
            tv_opinionsCount.setText("("+place.getOpinions().size()+")");
        });


        apiService = ServiceGenerator.createService(ApiService.class,getActivity());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        getCurrentLocation();

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .setWaitForAccurateLocation(true)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                myLocation = locationResult.getLastLocation();
                if(follow) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(),myLocation.getLongitude()),16);
                    mMap.animateCamera(cameraUpdate);
                }
                for (Location location : locationResult.getLocations()) {
                    //myLocation = new Location(location);
                    System.out.println("User location:(" + location.getLatitude() + "," + location.getLongitude() + ")");
                }
            }
        };

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            mMap.setMyLocationEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        uiSettings.setMyLocationButtonEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_radius) {
            showBottomSheetRadius();
        } else if (id == R.id.btn_nearby_search) {
            getNearbyPlaces(radius,null);
        } else if (id == R.id.btn_filtr) {
            getCurrentLocation();
            showBottomSheetFilter();
        } else if (id == R.id.btn_follow) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(),myLocation.getLongitude()),16);
            mMap.animateCamera(cameraUpdate);
            follow = !follow;
            if(follow)
                Toast.makeText(getActivity(),"Sledze",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(),"Nie sledze",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.btn_filtr_submit) {
            List<Type> filterList = new ArrayList<>();
            for(int i = 0; i < group_chip_filter.getChildCount(); i++){
                Chip chip = (Chip)group_chip_filter.getChildAt(i);
                if(chip.isChecked()) {
                    filterList.add(Type.valueOf(chip.getTag().toString()));
                }
            }
            System.out.println("Szukam miejsc po filtrach");
            if(!filterList.isEmpty())
                getNearbyPlaces(radius, filterList);
            else
                getNearbyPlaces(radius,null);
        } else if (id == R.id.btn_radius_submit) {
            boolean anyChecked = false;
            for(int i = 0; i < radio_group_radius.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) radio_group_radius.getChildAt(i);
                if(radioButton.isChecked()) {
                    anyChecked = true;
                    radius = Integer.parseInt(radioButton.getTag().toString());
                    tv_radius.setText(radius + " m");
                    sheetBehaviorRadius.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
            if(!anyChecked)
                Toast.makeText(getActivity(),"Nie wybrano obszaru",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.linearlayout_opinions) {
            Bundle bundle = new Bundle();
            List<Opinion> opinions = markers.get(lMarker).getOpinions();
            bundle.putParcelableArrayList("opinions", (ArrayList<? extends Parcelable>) opinions);
            navController.navigate(R.id.navigationToOpinionsFragment,bundle);
        } else if (id == R.id.btn_navigation) {
            BigDecimal latitude = markers.get(lMarker).getLatitude();
            BigDecimal longitude = markers.get(lMarker).getLongitude();
            Uri mapIntentUri = Uri.parse("google.navigation:q="+latitude+","+longitude);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW,mapIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if(mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        }
    }

    private void showBottomSheetRadius() {
        sheetBehaviorMarker.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehaviorFiltr.setState(BottomSheetBehavior.STATE_HIDDEN);
        if(sheetBehaviorRadius.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehaviorRadius.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehaviorRadius.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    private void showBottomSheetFilter() {
        sheetBehaviorMarker.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehaviorRadius.setState(BottomSheetBehavior.STATE_HIDDEN);
        if(sheetBehaviorFiltr.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehaviorFiltr.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehaviorFiltr.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    private void getNearbyPlaces(int radius, List<Type> filterList) {
        if (mMap != null) {
            resetMap();
            drawCircle();
            Callback<List<Place>> callback = new Callback<List<Place>>() {
                @Override
                public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                    if (response.isSuccessful()) {
                        List<Place> nearbyPlaces = response.body();
                        if(nearbyPlaces == null) {
                            Toast.makeText(getActivity(),"Brak miejsc w pobliżu",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        markers = new HashMap<>();
                        for (Place p : nearbyPlaces) {
                            if(p.isIs_accepted()) {
                                System.out.println(p);
                                LatLng latLng = new LatLng(p.getLatitude().doubleValue(), p.getLongitude().doubleValue());
                                MarkerOptions m = new MarkerOptions();
                                m.position(latLng);
                                m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                                Marker marker = mMap.addMarker(m);
                                markers.put(marker,p);
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Place>> call, Throwable t) {
                    System.out.println("ERROR: " + t.getMessage());
                }
            };
            if(filterList == null || filterList.isEmpty())
                apiService.getAllPlaces(myLocation.getLatitude(), myLocation.getLongitude(), radius).enqueue(callback);
            else
                apiService.getAllPlaces(myLocation.getLatitude(), myLocation.getLongitude(), radius, filterList.stream().map(Type::toString).collect(Collectors.joining(","))).enqueue(callback);



        }
    }

    public void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null && myLocation == null) {
                    myLocation = location;
                    System.out.println("User location:(" + location.getLatitude() + "," + location.getLongitude() + ")");
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()),16));
                }
            }
        });
    }
    private void resetMap() {
        if (mMap != null) {
            mMap.clear();
        }
    }
    private void drawCircle() {
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(myLocation.getLatitude(),myLocation.getLongitude()))
                .radius(radius)
                .strokeColor(Color.RED)
                .strokeWidth(4));
    }
    private Marker lMarker;
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        apiService.getPlaceById(markers.get(marker).getId()).enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                if(response.isSuccessful()){
                    Place place = response.body();

                    placeViewModel.setData(place);
                }
            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {

            }
        });

        sheetBehaviorFiltr.setState(BottomSheetBehavior.STATE_HIDDEN);
        if(sheetBehaviorMarker.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehaviorMarker.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (marker.equals(lMarker) && sheetBehaviorMarker.getState() == BottomSheetBehavior.STATE_EXPANDED){
            sheetBehaviorMarker.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

        lMarker = marker;

        return true;
    }


    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        sheetBehaviorMarker.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehaviorFiltr.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehaviorRadius.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
}

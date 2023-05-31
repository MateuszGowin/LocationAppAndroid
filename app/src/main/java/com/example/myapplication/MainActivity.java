package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;

import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        Toolbar.OnMenuItemClickListener,
        NavController.OnDestinationChangedListener {

    private NavController navController;
    private Toolbar toolbar;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private boolean locationPermissionGranted = false;

    private MenuItem item_account;
    private MenuItem item_addPlace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        navController = navHostFragment.getNavController();


        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        toolbar = findViewById(R.id.toolbar);
        NavigationUI.setupWithNavController(toolbar,navController, appBarConfiguration);

        item_account = toolbar.getMenu().findItem(R.id.menu_item_account);
        item_addPlace = toolbar.getMenu().findItem(R.id.menu_item_addPlace);


        toolbar.setOnMenuItemClickListener(this);
        navController.addOnDestinationChangedListener(this);
        checkLocationPermission();

    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_account) {
            navController.navigate(R.id.action_mapsFragment_to_accountFragment);
            Toast.makeText(this,"Account",Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.menu_item_addPlace) {
            navController.navigate(R.id.action_mapsFragment_to_placeFragment);
        }
        return true;
    }

    @Override
    public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {

        if(navDestination.getId() != R.id.mapsFragment) {
            item_account.setVisible(false);
            item_addPlace.setVisible(false);
        } else {
            item_account.setVisible(true);
            item_addPlace.setVisible(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        locationPermissionGranted = true;
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }



}
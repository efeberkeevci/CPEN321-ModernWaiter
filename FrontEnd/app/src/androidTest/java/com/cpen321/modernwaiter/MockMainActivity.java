package com.cpen321.modernwaiter;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.cpen321.modernwaiter.application.MainActivity;
import com.cpen321.modernwaiter.application.NotificationService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MockMainActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tableSession = new MockTableSession();

        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        NotificationService.sendToken();

    }
}

package com.cpen321.modernwaiter.application;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cpen321.modernwaiter.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    // Shopping kart
    public static SessionInterface tableSession;

    //Backend stuff
    public static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        requestQueue = Volley.newRequestQueue(this);
        tableSession = new TableSession(requestQueue, this);

        setContentView(R.layout.activity_main);

        Picasso.get().setIndicatorsEnabled(true);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }
}

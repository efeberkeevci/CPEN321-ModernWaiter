package com.cpen321.modernwaiter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cpen321.modernwaiter.ui.MenuItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // Shopping kart
    public TableSession tableSession;

    // Deserialized restaurant's menu in the from of ID, MenuItem
    private HashMap<Integer, MenuItem> menuMap;

    //Backend stuff
    private static final int ID = 1;
    public static RequestQueue requestQueue;

    BottomNavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tableSession = new TableSession();

        setContentView(R.layout.activity_main);

        findViewById(R.id.inspectSession).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tableSession.checkout();
                    }
                }
        );

        requestQueue = Volley.newRequestQueue(this);
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_menu, R.id.navigation_pay)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TOKEN FAIL", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.d("Success", token);
                        try{
                            URL url = new URL("http://52.188.158.129:3000/registerToken");
                            HttpURLConnection client = null;
                            client = (HttpURLConnection) url.openConnection();
                            client.setRequestProperty("Content-Type", "application/json; utf-8");
                            client.setRequestProperty("Accept", "application/json");
                            client.setDoOutput(true);
                            //FIX JSON MESSAGE
                            String message = "{ \"token\" : " + "\"" + token +"\"";
                            try(OutputStream os = client.getOutputStream()) {
                                byte[] input = message.getBytes("utf-8");
                                os.write(input, 0, input.length);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }                    }
                });


    }
    public static int getID(){
        return ID;
    }

    public void openDetailItemView(int id) {

    }

}
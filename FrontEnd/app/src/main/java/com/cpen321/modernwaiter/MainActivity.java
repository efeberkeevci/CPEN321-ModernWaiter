package com.cpen321.modernwaiter;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cpen321.modernwaiter.ui.MenuItem;
import com.cpen321.modernwaiter.ui.menu.MenuFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Shopping kart
    public TableSession tableSession;

    // Deserialized restaurant's menu in the from of ID, MenuItem
    private HashMap<Integer, MenuItem> menuMap;

    //Backend stuff
    private static final int ID = 1;
    public static RequestQueue requestQueue;
    public static ArrayList<MenuItem> menu_items = new ArrayList<MenuItem>();

    BottomNavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveMenuItems();

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
        tableSession = new TableSession(requestQueue);

        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_menu, R.id.navigation_pay)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        MyFirebaseMessagingService.sendToken();


    }
    public static int getID(){
        return ID;
    }

    public void openDetailItemView(int id) {

    }
    private void retrieveMenuItems(){
        Log.i("NOTE:","retrieving menu items");

        RequestQueue queue = Volley.newRequestQueue(this);
        LinkedList<MenuItem> response_menu_items = new LinkedList<MenuItem>();
        String url ="http://52.188.158.129:3000/items/1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response:",response);

                        //Toast.makeText(TimeActivity.this,"Date: "+(response),Toast.LENGTH_LONG).show();
                        //time_text = findViewById(R.id.time_text);
                        //time_text.setText("Date: "+(response));
                        tableSession.addMenuItems(menuItemParser(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("asd",error.toString());
                //Toast.makeText(TimeActivity.this,("Error"),Toast.LENGTH_LONG);
            }
        });
        queue.add(stringRequest);
    }
    private ArrayList<MenuItem> menuItemParser(String json){
        Log.i("NOTE","İN menuItemParser FUNC");

            // convert JSON array to Java List
            Log.i("NOTE","parsing");
            return new Gson().fromJson(json, new TypeToken<List<MenuItem>>() {}.getType());
    }
}

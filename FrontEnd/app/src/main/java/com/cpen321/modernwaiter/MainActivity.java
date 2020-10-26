package com.cpen321.modernwaiter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cpen321.modernwaiter.ui.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

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
                        HashMap<String,String>  post_message_body= new HashMap<> ();
                        post_message_body.put("registrationToken", token);
                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try  {
                                    performPostCall("http://52.188.158.129:3000/registrationToken", post_message_body);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        thread.start();

                    }
                });


    }
    public static int getID(){
        return ID;
    }

    public void openDetailItemView(int id) {

    }
    public String  performPostCall(String requestURL,
                                   HashMap<String, String> postDataParams){

        URL url;
        String response="";
        try{
            url=new URL(requestURL);

            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os=conn.getOutputStream();
            BufferedWriter writer=new BufferedWriter(
                    new OutputStreamWriter(os,"UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if(responseCode== HttpsURLConnection.HTTP_OK){
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while((line=br.readLine())!=null){
                    response+=line;
                }
            }
            else{
                response="";

            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return response;
    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}

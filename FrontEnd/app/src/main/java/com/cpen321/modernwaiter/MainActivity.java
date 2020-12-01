package com.cpen321.modernwaiter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cpen321.modernwaiter.barcode.BarcodeActivity;
import com.cpen321.modernwaiter.customer.application.ApiUtil;
import com.cpen321.modernwaiter.customer.application.CustomerActivity;
import com.cpen321.modernwaiter.customer.application.TableSession;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public final static int GOOGLE_SIGN_IN_ACTIVITY_CODE = 1;
    public final static int BARCODE_ACTIVITY_CODE = 2;

    private GoogleSignInAccount account;
    private int userId;

    private RequestQueue requestQueue;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            signInGoogle();
        } else {
            account = GoogleSignIn.getLastSignedInAccount(this);
        }

        fetchUserId();
    }

    private void signInGoogle() {
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1031633982947-6dqn2p8mginechhj7ekn3n231tcsif9e.apps.googleusercontent.com")
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_ACTIVITY_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_ACTIVITY_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else if (requestCode == BARCODE_ACTIVITY_CODE) {
            Intent customerActivityIntent = new Intent(this, CustomerActivity.class);
            Bundle bundle = new Bundle();

            bundle.putInt("userId", userId);
            bundle.putString("restaurantId", data.getStringExtra("restaurantId"));
            bundle.putString("tableId", data.getStringExtra("tableId"));

            customerActivityIntent.putExtras(bundle);

            startActivity(customerActivityIntent);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account == null) {
                signInGoogle();
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Customer Activity", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void fetchUserId() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, ApiUtil.usersGoogle + account.getId(),
                response -> {
                    if (response.equals("")) {
                        postUserId();
                    } else {
                        TableSession.UserResponse userResponse = new Gson().fromJson(response, new TypeToken<TableSession.UserResponse>() {}.getType());
                        userId = userResponse.id;
                        setupButtons();
                    }
                }, error -> Log.i("Fetch user Id", error.toString()));

        requestQueue.add(stringRequest);
    }

    private void postUserId() {
        final Map<String, String> bodyFields = new HashMap<>();
        bodyFields.put("username", account.getDisplayName());
        bodyFields.put("email", account.getEmail());
        bodyFields.put("googleId", account.getId());
        bodyFields.put("preferences", "");

        final String bodyJSON = new Gson().toJson(bodyFields);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, ApiUtil.users,
                response -> fetchUserId(),

                error -> Log.i("Post user", error.toString())
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return bodyJSON.getBytes();
            }
        };

        requestQueue.add(stringRequest);
    }

    private void setupButtons() {
        Button startDemoButton = findViewById(R.id.demo_button);
        startDemoButton.setVisibility(View.VISIBLE);
        startDemoButton.setOnClickListener(a -> {
            Intent customerActivityIntent = new Intent(this, CustomerActivity.class);
            Bundle bundle = new Bundle();

            bundle.putInt("userId", userId);
            bundle.putString("restaurantId", ApiUtil.RESTAURANT_ID);
            bundle.putString("tableId", ApiUtil.TABLE_ID);

            customerActivityIntent.putExtras(bundle);

            startActivity(customerActivityIntent);
        });

        Button startBarcodeButton = findViewById(R.id.barcode_button);
        startBarcodeButton.setVisibility(View.VISIBLE);
        startBarcodeButton.setOnClickListener(a -> {
            Intent barcodeActivityIntent = new Intent(this, BarcodeActivity.class);
            startActivityForResult(barcodeActivityIntent, BARCODE_ACTIVITY_CODE);
        });

        TextView loadingTextview = findViewById(R.id.loadingTextView);
        loadingTextview.setVisibility(View.INVISIBLE);
    }
}
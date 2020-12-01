package com.cpen321.modernwaiter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.cpen321.modernwaiter.barcode.BarcodeActivity;
import com.cpen321.modernwaiter.customer.application.ApiUtil;
import com.cpen321.modernwaiter.customer.application.CustomerActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    public final static int GOOGLE_SIGN_IN_ACTIVITY_CODE = 1;
    public final static int BARCODE_ACTIVITY_CODE = 2;

    GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            signInGoogle();
        } else {
            account = GoogleSignIn.getLastSignedInAccount(this);
        }

        Button startDemoButton = findViewById(R.id.demo_button);
        startDemoButton.setOnClickListener(a -> {
            Intent customerActivityIntent = new Intent(this, CustomerActivity.class);
            Bundle bundle = new Bundle();

            bundle.putString("googleId", account.getId());
            bundle.putString("googleEmail", account.getEmail());
            bundle.putString("googleName", account.getDisplayName());
            bundle.putString("restaurantId", ApiUtil.RESTAURANT_ID);
            bundle.putString("tableId", ApiUtil.TABLE_ID);

            customerActivityIntent.putExtras(bundle);

            startActivity(customerActivityIntent);
        });

        Button startBarcodeButton = findViewById(R.id.barcode_button);
        startBarcodeButton.setOnClickListener(a -> {
            Intent barcodeActivityIntent = new Intent(this, BarcodeActivity.class);
            startActivity(barcodeActivityIntent);
        });
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

            bundle.putString("googleId", account.getId());
            bundle.putString("googleEmail", account.getEmail());
            bundle.putString("googleName", account.getDisplayName());
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
}
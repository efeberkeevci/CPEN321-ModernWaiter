package com.cpen321.modernwaiter.barcode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeActivity extends AppCompatActivity {

    //In order to use barcode scan using webcam of pc is recommended because it is not possible with the android emulator mock camera
    //In order to do this go to Tools>AVD Manager>Edit your device of choice>show advanced settings>camera>front->webcam0 and back->webcam0

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        Log.d("MESSAGE:", "In barcode activity");
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //TODO: Extract restaurant_Id from result.getContents() and pass this to the activity which calls the menu and switch to menu view
                //TODO: Extract table_Id from result.getContents() and pass this to the activity which creates an active session for that user in that table_id
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setTableId(int tableId) {
        //TODO: SET THE TABLE ID
    }

    public void setRestaurantId(int restaurantId) {
        //TODO: SET THE RESTAURANT ID
    }

}

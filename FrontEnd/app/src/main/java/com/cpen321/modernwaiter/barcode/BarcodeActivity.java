package com.cpen321.modernwaiter.barcode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cpen321.modernwaiter.MainActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static java.lang.Integer.parseInt;

public class BarcodeActivity extends AppCompatActivity {

    //In order to use barcode scan using webcam of pc is recommended because it is not possible with the android emulator mock camera
    //In order to do this go to Tools>AVD Manager>Edit your device of choice>show advanced settings>camera>front->webcam0 and back->webcam0

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        Log.d("MESSAGE:", "In barcode activity");
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode");
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(integrator.QR_CODE);
        integrator.initiateScan();    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                //Extract restaurant_Id from result.getContents() and pass this to the activity which calls the menu and switch to menu view
                setRestaurantId(parseInt((result.getContents().split(",",0)[0]),10));
                //Extract table_Id from result.getContents() and pass this to the activity which creates an active session for that user in that table_id
                setTableId(parseInt((result.getContents().split(",",0)[1]),10));

                Intent intent = new Intent();
                intent.putExtra("restaurantId", result.getContents().split(",",0)[0]);
                intent.putExtra("tableId", result.getContents().split(",",0)[1]);

                setResult(MainActivity.BARCODE_ACTIVITY_CODE, intent);
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setTableId(int tableId) {
        //TODO: SET THE TABLE ID
            Log.d("Table_Id", Integer.toString(tableId));
    }

    public void setRestaurantId(int restaurantId) {
        //TODO: SET THE RESTAURANT ID
        Log.d("Restaurant_Id", Integer.toString(restaurantId));

    }

}

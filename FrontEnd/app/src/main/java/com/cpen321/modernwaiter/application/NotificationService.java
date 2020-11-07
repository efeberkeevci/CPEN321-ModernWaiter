package com.cpen321.modernwaiter.application;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cpen321.modernwaiter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static com.cpen321.modernwaiter.application.MainActivity.tableSession;

public class NotificationService extends FirebaseMessagingService {

    public static String token;

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String notification_title = remoteMessage.getNotification().getTitle();

        if ("Item Claimed!".equals(notification_title)){
            tableSession.fetchOrderList();
        } else if ("Order Received!".equals(notification_title)) {
            notifyUser(remoteMessage);
            tableSession.fetchBill();
            tableSession.fetchOrderList();
        } else if ("Payment Completed!".equals(notification_title)) {
            notifyUser(remoteMessage);
        }
    }

    private void notifyUser(RemoteMessage remoteMessage) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // create channel in new versions of android
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("CHANNEL1", "MODERN_WAITER_NOTIFICATION_CHANNEL", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Log.d("MESSAGE: ","NEW PUSH NOTIFICATION");
        String notification_title = remoteMessage.getNotification().getTitle();
        String notification_body = remoteMessage.getNotification().getTitle();
        Notification notification = new NotificationCompat.Builder(this,"CHANNEL1")
                .setContentTitle(notification_title)
                .setContentText(notification_body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // or NotificationCompat.PRIORITY_MAX
                .build();
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(123, notification);
    }

    public static void sendToken(String orderId) {
        FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (!task.isSuccessful()) {
                        Log.w("TOKEN FAIL", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    token = task.getResult();

                    // Log and toast
                    Log.d("Success", token);
                    HashMap<String, String> post_message_body = new HashMap<>();
                    post_message_body.put("registrationToken", token);
                    post_message_body.put("orderId", orderId);

                    final String bodyJSON = new Gson().toJson(post_message_body);
                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST, ApiUtil.registration,
                            response -> {
                                Log.i("MSG:",response);

                            }, error -> Log.i("Posting token:", error.toString())
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

                    tableSession.add(stringRequest);
                }
            });
    }

    public static void unsubscribe(String orderId) {
        HashMap<String, String> post_message_body = new HashMap<>();
        post_message_body.put("registrationToken", token);
        post_message_body.put("orderId", orderId);

        final String bodyJSON = new Gson().toJson(post_message_body);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, ApiUtil.unsubscribe,
                response -> {
                    Log.i("MSG:",response);

                }, error -> Log.i("Unsubscribe token:", error.toString())
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

        tableSession.add(stringRequest);
    }
}

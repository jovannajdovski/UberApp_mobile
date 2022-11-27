package com.example.uberapp_tim12.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.activities.DriverMainActivity;
import com.example.uberapp_tim12.activities.PassengerMainActivity;

public class SyncMessagesReceiver extends BroadcastReceiver {

    private static int NOTIFICATION_ID = 1;
    private static String DRIVER_CHANNEL_ID = "Driver notification channel";
    private static String PASSENGER_CHANNEL_ID = "Passenger notification channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("REZ", "onReceive");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder driverNCBuilder = new NotificationCompat.Builder(context, DRIVER_CHANNEL_ID);
        NotificationCompat.Builder passengerNCBuilder = new NotificationCompat.Builder(context, PASSENGER_CHANNEL_ID);


        if(intent.getAction().equals(DriverMainActivity.SYNC_DRIVER_MESSAGES)){
            String message = intent.getExtras().getString("Message");
            Bitmap bm;

            bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_baseline_message_24);
            driverNCBuilder.setSmallIcon(R.drawable.ic_baseline_message_24);
            driverNCBuilder.setContentTitle(context.getString(R.string.notification_driver_title));
            driverNCBuilder.setContentText(message);
            driverNCBuilder.setLargeIcon(bm);
            notificationManager.notify(NOTIFICATION_ID, driverNCBuilder.build());

        } else if(intent.getAction().equals(PassengerMainActivity.SYNC_PASSENGER_MESSAGES)){
            String message = intent.getExtras().getString("Message");
            Bitmap bm;

            bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_baseline_message_24);
            passengerNCBuilder.setSmallIcon(R.drawable.ic_baseline_message_24);
            passengerNCBuilder.setContentTitle(context.getString(R.string.notification_passenger_title));
            passengerNCBuilder.setContentText(message);
            passengerNCBuilder.setLargeIcon(bm);
            notificationManager.notify(NOTIFICATION_ID, passengerNCBuilder.build());
        }
    }
}
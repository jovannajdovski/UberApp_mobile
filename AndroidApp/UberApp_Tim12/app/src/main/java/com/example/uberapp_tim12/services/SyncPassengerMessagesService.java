package com.example.uberapp_tim12.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.example.uberapp_tim12.activities.PassengerMainActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyncPassengerMessagesService extends Service {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("REZ", "SyncPassengerService onStartCommand");

        String generatedMessage = "I am a passenger";

        executor.execute(() -> {
            //Background work here
            try {
                //Thread.sleep(1000);
                Log.i("REZ", generatedMessage);
                //         Poruke u Inbox da se stave !!!!!!!!!!!!!!!!!!!!
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.post(() -> {
                //UI Thread work here
                Intent ints = new Intent(PassengerMainActivity.SYNC_PASSENGER_MESSAGES);
                String message = generatedMessage;
                ints.putExtra("Message", message);
                getApplicationContext().sendBroadcast(ints);
            });
        });

        stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
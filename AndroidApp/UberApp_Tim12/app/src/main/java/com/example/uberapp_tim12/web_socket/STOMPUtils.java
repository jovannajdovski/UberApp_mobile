package com.example.uberapp_tim12.web_socket;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class STOMPUtils {
//    public static final String SERVICE_API_PATH = "192.168.0.11:8080/";
//public static final String SERVICE_API_PATH = "192.168.3.6:8080/";
//public static final String SERVICE_API_PATH = "172.29.240.1:8080/";
public static final String SERVICE_API_PATH = "192.168.3.2:8080/";


    private String TAG = "STOMP";
    public StompClient stompClient;

    private CompositeDisposable compositeDisposable;

    public void connectStomp() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + SERVICE_API_PATH
                 + "api/socket/websocket");

        resetSubscriptions();

        Disposable dispLifecycle = stompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.d(TAG,"Stomp connection opened");
                            break;
                        case ERROR:
                            Log.d(TAG, "Stomp connection error", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.d(TAG, "Stomp connection closed");
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Log.d(TAG,"Stomp failed server heartbeat");
                            break;
                    }
                });

        compositeDisposable.add(dispLifecycle);

        stompClient.connect();
    }

    public void disconnectStomp() {
        stompClient.disconnect();
    }

    public void subscribeOn(String topic){
        Disposable dispTopic = stompClient.topic("api/socket-publisher/"+topic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG, "Received " + topicMessage.getPayload());
                    // logika ovde moze se koristiti odgovar kao stoj npr topicMessage
                }, throwable -> {
                    Log.e(TAG, "Error on subscribe topic", throwable);
                });

        compositeDisposable.add(dispTopic);
    }

    public void sendEchoViaStomp(String websocketControllerTopic) {
//        if (!mStompClient.isConnected()) return;
        compositeDisposable.add(stompClient.send("api/socket-subscriber/"+websocketControllerTopic, "Echo STOMP ")
                .compose(applySchedulers())
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully");
                }, throwable -> {
                    Log.e(TAG, "Error send STOMP echo", throwable);
                }));
    }

    public CompletableTransformer applySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }
}

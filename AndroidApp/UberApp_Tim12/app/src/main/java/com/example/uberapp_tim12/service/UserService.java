package com.example.uberapp_tim12.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.dto.LoginUserDTO;
import com.example.uberapp_tim12.dto.MessageDTO;
import com.example.uberapp_tim12.dto.MessageListDTO;
import com.example.uberapp_tim12.dto.RideFullDTO;
import com.example.uberapp_tim12.dto.SendingMessageDTO;
import com.example.uberapp_tim12.dto.UserTokenDTO;
import com.example.uberapp_tim12.security.LoggedUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService extends Service {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        //dobavljanje podataka iz intenta
        String method=intent.getStringExtra("endpoint");

        executor.execute(new Runnable() {
            @Override
            public void run() {

                Log.d("PASSS", "Metoda ".concat(method));
                if(method.equals("loginUser"))
                {
                    final UserTokenDTO[] userTokenDTO=new UserTokenDTO[1];
                    LoginUserDTO loginUserDTO= (LoginUserDTO) intent.getSerializableExtra("user");
                    Log.d("PASSS", "ista metoda");
                    Call<UserTokenDTO> call = ControllerUtils.userController.loginUser(loginUserDTO);
                    call.enqueue(new Callback<UserTokenDTO>() {
                        @Override
                        public void onResponse(Call<UserTokenDTO> call, Response<UserTokenDTO> response) {
                            if (response.code() == 200){
                                Log.d("PASSS", "200");
                                userTokenDTO[0]=response.body();
                                Log.d("PASSS", userTokenDTO[0].toString());
                                Intent ints = new Intent ("logged");
                                Log.d("PASSS", userTokenDTO[0].toString());
                                ints.putExtra("userLoginDTO", userTokenDTO[0]);
                                ints.putExtra("found", "true");
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            } else if (response.code() == 400){
                                Intent ints = new Intent ("logged");
                                ints.putExtra("found", "false");
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }
                            else{
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<UserTokenDTO> call, Throwable t) {
                            Log.d("REZZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
                else if(method.equals("getMessages"))
                {
                    final MessageListDTO[] messageListDTO = new MessageListDTO[1];
                    int id=intent.getIntExtra("id", 0);
                    Call<MessageListDTO> call = ControllerUtils.userController.getUserMessages(id, "Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<MessageListDTO>() {
                        @Override
                        public void onResponse(Call<MessageListDTO> call, Response<MessageListDTO> response) {
                            if (response.code() == 200) {
                                Log.d("PASSS", "200");
                                messageListDTO[0] = response.body();
                                Log.d("PASSS", messageListDTO[0].toString());
                                Intent ints = new Intent("userMessages");
                                Log.d("PASSS", messageListDTO[0].toString());
                                ints.putExtra("messageListDTO", messageListDTO[0]);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }
                            else{
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageListDTO> call, Throwable t) {
                            Log.d("REZZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
                else if(method.equals("sendMessage"))
                {
                    final MessageDTO[] messageDTO = new MessageDTO[1];
                    int id=intent.getIntExtra("receiverId", 0);
                    SendingMessageDTO sendingMessageDTO=intent.getParcelableExtra("messageDTO");
                    Call<MessageDTO> call = ControllerUtils.userController.sendMessage(id, sendingMessageDTO, "Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<MessageDTO>() {
                        @Override
                        public void onResponse(Call<MessageDTO> call, Response<MessageDTO> response) {
                            Intent ints = new Intent ("sendMessage");
                            if (response.code() == 200){
                                Log.d("PASSS", "200");
                                messageDTO[0] =response.body();
                                Log.d("PASSS", messageDTO[0].toString());
                                ints.putExtra("messageDTO", messageDTO[0]);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            } else if (response.code() == 400){
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }
                            else{
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageDTO> call, Throwable t) {
                            Log.d("REZZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
                else{
                    stopSelf();
                }
            }
        });

        stopSelf();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


package com.example.uberapp_tim12.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.dto.LoginUserDTO;
import com.example.uberapp_tim12.dto.NewPasswordDTO;
import com.example.uberapp_tim12.dto.PassengerDetailsDTO;
import com.example.uberapp_tim12.dto.RideFullDTO;
import com.example.uberapp_tim12.dto.UserEmailDTO;
import com.example.uberapp_tim12.dto.MessageDTO;
import com.example.uberapp_tim12.dto.MessageListDTO;
import com.example.uberapp_tim12.dto.MultipleSendingMessageDTO;
import com.example.uberapp_tim12.dto.RideFullDTO;
import com.example.uberapp_tim12.dto.SendingMessageDTO;
import com.example.uberapp_tim12.dto.UserTokenDTO;
import com.example.uberapp_tim12.security.LoggedUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
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
                }else if(method.equals("sendResetCodeToEmail"))
                {
                    Intent ints = new Intent ("resetPassword");

                    Log.d("PASSS", "zove bek ");
                    Call<ResponseBody> call = ControllerUtils.userController.sendResetCodeToEmail(intent.getIntExtra("id",0));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 204) {
                                ints.putExtra("sent", "true");
                            } else if (response.code() == 404){
                                ints.putExtra("sent", "false");
                            }else {
                                ints.putExtra("responseMessage", response.message());
                                Log.d("PASSS", "Meesage recieved: " + response.code());
                            }
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("PASSS", t.getMessage() != null ? t.getMessage() : "error");
                        }
                    });
                } else if(method.equals("changePassword"))
                {
                    Intent ints = new Intent ("passwordChanged");
                    Integer userId = LoggedUser.getUserId();
                    String oldPassword = intent.getStringExtra("oldPassword");
                    String newPassword = intent.getStringExtra("newPassword");
                    Call<ResponseBody> call = ControllerUtils.userController.changePassword(userId, new NewPasswordDTO(oldPassword,newPassword), "Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 204) {
                                ints.putExtra("changed", "true");
                            } else {
                                ints.putExtra("changed", "false");
                            }
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("PASSS", t.getMessage() != null ? t.getMessage() : "error");
                        }
                    });
                }
                else if(method.equals("getMessages"))
                {
                    Log.d("PASSS", "Usao u else if");
                    final MessageListDTO[] messageListDTO = new MessageListDTO[1];
                    Call<MessageListDTO> call = ControllerUtils.userController.getUserMessages(LoggedUser.getUserId(), "Bearer "+ LoggedUser.getToken());
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
                else if(method.equals("getRideMessages"))
                {
                    Log.d("PASSSS", "usao u servisu");
                    int rideId=intent.getIntExtra("rideId", 0);
                    final MessageListDTO[] messageListDTO = new MessageListDTO[1];
                    Call<MessageListDTO> call = ControllerUtils.userController.getUserMessagesForSpecificRide(LoggedUser.getUserId(), rideId, "Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<MessageListDTO>() {
                        @Override
                        public void onResponse(Call<MessageListDTO> call, Response<MessageListDTO> response) {
                            if (response.code() == 200) {
                                Log.d("PASSS", "status 200");
                                messageListDTO[0] = response.body();
                                Log.d("PASSS", messageListDTO[0].toString());
                                Intent ints = new Intent("rideChat");
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
                                messageDTO[0] =response.body();
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
                else if(method.equals("multipleSendMessage"))
                {
                    MultipleSendingMessageDTO multipleSendingMessageDTO=intent.getParcelableExtra("multipleMessageDTO");
                    Log.d("PASSS", String.valueOf(multipleSendingMessageDTO.getUserIds().size()));
                    Log.d("PASSS", multipleSendingMessageDTO.getMessage().getMessage());
                    Call<MessageDTO> call = ControllerUtils.userController.sendMultipleMessages(multipleSendingMessageDTO, "Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<MessageDTO>() {
                        @Override
                        public void onResponse(Call<MessageDTO> call, Response<MessageDTO> response) {

                            if (response.code() == 200){
                                Log.d("PASSSSSSSSSSSSSS","USPESNO!!!!!");
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


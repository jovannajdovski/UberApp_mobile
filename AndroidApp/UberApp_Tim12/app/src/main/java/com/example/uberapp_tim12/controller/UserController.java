package com.example.uberapp_tim12.controller;

import com.example.uberapp_tim12.dto.LoginUserDTO;
import com.example.uberapp_tim12.dto.MessageDTO;
import com.example.uberapp_tim12.dto.MessageListDTO;
import com.example.uberapp_tim12.dto.MultipleSendingMessageDTO;
import com.example.uberapp_tim12.dto.PanicDTO;
import com.example.uberapp_tim12.dto.ReasonDTO;
import com.example.uberapp_tim12.dto.SendingMessageDTO;
import com.example.uberapp_tim12.dto.UserTokenDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserController {

    @POST("user/login")
    Call<UserTokenDTO> loginUser(@Body LoginUserDTO user);

    @GET("user/{id}/message")
    Call<MessageListDTO> getUserMessages(@Path("id") int id, @Header("Authorization") String token);

    @GET("user/{id}/message/{ride_id}")
    Call<MessageListDTO> getUserMessagesForSpecificRide(@Path("id") int id, @Path("ride_id") int ride_id, @Header("Authorization") String token);

    @POST("user/{id}/message")
    Call<MessageDTO> sendMessage(@Path("id") int receiverId, @Body SendingMessageDTO messageDTO, @Header("Authorization") String token);

    @POST("user/send-messages")
    Call<MessageDTO> sendMultipleMessages(@Body MultipleSendingMessageDTO messageDTO, @Header("Authorization") String token);

}

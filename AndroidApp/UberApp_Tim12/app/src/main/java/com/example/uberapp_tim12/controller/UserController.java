package com.example.uberapp_tim12.controller;

import com.example.uberapp_tim12.dto.LoginUserDTO;
import com.example.uberapp_tim12.dto.PanicDTO;
import com.example.uberapp_tim12.dto.ReasonDTO;
import com.example.uberapp_tim12.dto.UserTokenDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserController {

    @POST("user/login")
    Call<UserTokenDTO> loginUser(@Body LoginUserDTO user);
}

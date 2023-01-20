package com.example.uberapp_tim12.controller;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerUtils {
    public static final String SERVICE_API_PATH = "http://192.168.0.13:8080/api/";

    public static OkHttpClient test(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        return client;
    }

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(test())
            .build();

    public static RideController rideController = retrofit.create(RideController.class);
    public static DriverController driverController=retrofit.create(DriverController.class);
    public static PassengerController passengerController=retrofit.create(PassengerController.class);

    public static PanicController panicController = retrofit.create(PanicController.class);

    public static UserController userController = retrofit.create(UserController.class);
}

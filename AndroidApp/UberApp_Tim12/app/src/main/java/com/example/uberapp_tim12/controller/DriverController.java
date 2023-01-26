package com.example.uberapp_tim12.controller;

import com.example.uberapp_tim12.dto.RidePageList;
import com.example.uberapp_tim12.dto.RidesListDTO;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.TimeSpanDTO;
import com.example.uberapp_tim12.dto.UserDTO;
import com.example.uberapp_tim12.model.DriverStatistics;
import com.example.uberapp_tim12.model.RideCountStatistics;
import com.example.uberapp_tim12.model.RideDistanceStatistics;
import com.example.uberapp_tim12.model.User;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import java.time.LocalDateTime;
import com.example.uberapp_tim12.dto.ActiveDriverListDTO;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.EndTimeDTO;
import com.example.uberapp_tim12.dto.StartTimeDTO;
import com.example.uberapp_tim12.dto.WorkHoursDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DriverController {

    @GET("driver/{id}")
    Call<DriverDetailsDTO> getDriverDetails(@Path("id") Integer id,
                                            @Header("Authorization") String token);

    @PUT("driver/{id}")
    Call<User> updateDriver(@Path("id") Integer id,
                            @Header("Authorization") String token,
                            @Body UserDTO userDTO);

    @GET("driver/{id}/stats")
    Call<DriverStatistics> getDriverStatistics(@Path("id") Integer id,
                                               @Header("Authorization") String token,
                                               @Query("from") LocalDateTime from,
                                               @Query("to") LocalDateTime to);

    @GET("driver/{id}/ride-count")
    Call<RideCountStatistics> getRideCountStatistics(@Path("id") Integer id,
                                                     @Header("Authorization") String token,
                                                     @Query("from") LocalDateTime from,
                                                     @Query("to") LocalDateTime to);

    @GET("driver/{id}/distance")
    Call<RideDistanceStatistics> getRideDistanceStatistics(@Path("id") Integer id,
                                                           @Header("Authorization") String token,
                                                           @Query("from") LocalDateTime from,
                                                           @Query("to") LocalDateTime to);

    @GET("driver/{id}/ride")
    Call<RidePageList> getDriverRides(@Path("id") Integer id, @Query("page") Integer page, @Query("size") Integer size, @Query("sort") String sort, @Query("from") String from, @Query("to") String to, @Header("Authorization") String token);

    @GET("driver/active-drivers")
    Call<ActiveDriverListDTO> getActiveDrivers(@Header("Authorization") String token);

    @POST("driver/{driver-id}/working-hour")
    Call<WorkHoursDTO> startShift(@Path("driver-id") Integer id, @Body StartTimeDTO time, @Header("Authorization") String token);

    @PUT("driver/working-hour/{work-hour-id}")
    Call<WorkHoursDTO> endShift(@Path("work-hour-id") Integer id, @Body EndTimeDTO time, @Header("Authorization") String token);
}

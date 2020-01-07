package com.baskerville.toilocate.service;

import com.baskerville.toilocate.dto.ResponseDTO;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ToiletService {

    @GET("api/get-all")
    Call<ResponseDTO> getAllToilets();

    @POST("api/near")
    @FormUrlEncoded
    Call<ResponseDTO> getNearbyToilets(@Field("long") double longitude,
                                       @Field("latt") double latitude, @Field("maxDist") double maxDist);


}

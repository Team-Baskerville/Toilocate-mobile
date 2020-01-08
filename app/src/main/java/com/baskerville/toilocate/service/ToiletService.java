package com.baskerville.toilocate.service;

import com.baskerville.toilocate.dto.ImageResponseDTO;
import com.baskerville.toilocate.dto.ResponseDTO;
import com.baskerville.toilocate.dto.ToiletDTO;
import com.baskerville.toilocate.dto.ToiletSaveResDTO;
import com.baskerville.toilocate.dto.ToiletSearchDTO;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ToiletService {

    @GET("get-all")
    Call<ResponseDTO> getAllToilets();

//    @POST("api/near")
//    @FormUrlEncoded
//    Call<ResponseDTO> getNearbyToilets(@Field("long") double longitude,
//                                       @Field("latt") double latitude, @Field("maxDist") double maxDist);

    @POST("near")
    Call<ResponseDTO> getNearbyToilets(@Body ToiletSearchDTO toiletSearchDTO);

    @POST("image")
    @Multipart
    Call<ImageResponseDTO> uploadImage(@Part MultipartBody.Part image, @Query("name") String name);

    @POST("add")
    Call<ToiletSaveResDTO> saveToilet(@Body ToiletDTO toiletDTO);



}

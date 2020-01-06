package com.baskerville.toilocate.service;

import com.baskerville.toilocate.dto.ResponseDTO;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ToiletService {

    @GET("api/get-all")
    Call<ResponseDTO> getAllToilets();


}

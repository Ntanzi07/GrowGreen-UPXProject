package com.example.growgreen.api;

import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import okhttp3.MultipartBody;

public interface PlantApiService {

    @Multipart
    @POST("predict") // Tente "predict" que é comum em APIs de ML
    Call<ApiResponse> uploadImage(@Part MultipartBody.Part file);

    // Ou se não funcionar, tente sem endpoint:
    // @POST("/")
    // Call<ApiResponse> uploadImage(@Part MultipartBody.Part file);
}
package com.aulmrd.mystory.data.api

import com.aulmrd.mystory.data.response.LoginResponse
import com.aulmrd.mystory.data.response.RegisterResponse
import com.aulmrd.mystory.data.response.StoriesResponse
import com.aulmrd.mystory.data.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiServices {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password")password :String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET ("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("location") location : String?
    ): StoriesResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody?
    ): UploadResponse
}
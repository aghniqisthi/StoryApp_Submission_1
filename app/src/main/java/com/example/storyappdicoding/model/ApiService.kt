package com.example.storyappdicoding.model

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseRegister>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseLogin>

    @GET("stories")
    fun getAllStories(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int?,
        @Header("Authorization") authHeader: String?
    ): Call<ResponseListStories>

    @GET("stories/{id}")
    fun getDetailStory(
        @Path("id") id: String,
        @Header("Authorization") authHeader: String?
    ): Call<ResponseDetailStory>

    @Multipart
    @POST("stories")
    fun addStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") authHeader: String?
    ): Call<ResponseAddStory>

}

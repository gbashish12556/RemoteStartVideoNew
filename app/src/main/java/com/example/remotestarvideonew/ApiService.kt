package com.example.remotestarvideonew

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("trending/all/day")
    fun getVideos(@Query("api_key") apiKeys:String,@Header("Cache-Control") cacheControl:String?) : Call<VideoResponse>

}
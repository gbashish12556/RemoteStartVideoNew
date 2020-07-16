package com.example.remotestarvideonew

import android.app.Application
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRepository(private val applicationContext:Application){

    private val videos = MutableLiveData<VideoResponse>()
    private val status = MutableLiveData<Boolean>()

    val allVideos: MutableLiveData<VideoResponse>
        get() {
            fetchVideos(null)
            return videos
        }

    val videoApiStatus: MutableLiveData<Boolean>
        get() {
            return status
        }


    fun fetchVideos(cacheControl:String?) {

        val call1 = RetrofitClient.getInstance(applicationContext).api.getVideos(Constants.API_KEY,cacheControl)
        call1.enqueue(object : Callback<VideoResponse> {

            override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                if (response.code() == 200) {
                    val reponse = response.body()
                    if (reponse.results!!.size > 0) {
                        videos.postValue(reponse)
                    } else {
                        status!!.postValue(false)
                    }
                } else {
                    status!!.postValue(false)
                }
            }

            override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                status!!.postValue(false)
            }
        })
    }



}
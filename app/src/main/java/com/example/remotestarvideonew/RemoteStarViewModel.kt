package com.example.remotestarvideonew

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RemoteStarViewModel : ViewModel {

    constructor(apiService:ApiService) {
        apiRepository = ApiRepository(apiService)
    }

    val apiRepository: ApiRepository

    val videos: MutableLiveData<VideoResponse>
        get() = apiRepository.allVideos

    val apiStatus: MutableLiveData<Boolean>
        get() = apiRepository.videoApiStatus

    fun fetchVideos(cacheControl:String?){
        apiRepository.fetchVideos(cacheControl)
    }

}
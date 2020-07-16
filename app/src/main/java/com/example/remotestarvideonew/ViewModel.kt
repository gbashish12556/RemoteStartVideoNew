package com.example.remotestarvideonew

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ViewModel(application: Application) : AndroidViewModel(application) {

    private val apiRepository: ApiRepository


    val videos: MutableLiveData<VideoResponse>
        get() = apiRepository.allVideos

    val apiStatus: MutableLiveData<Boolean>
        get() = apiRepository.videoApiStatus

    fun fetchVideos(cacheControl:String?){
        apiRepository.fetchVideos(cacheControl)
    }

    init {
        apiRepository = ApiRepository(application)
    }

}
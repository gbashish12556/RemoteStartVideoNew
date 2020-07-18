package com.example.remotestarvideonew

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RemoteStarViewModeFactory:ViewModelProvider.Factory {

//    var newApplication:Application? = null
    var newApiService:ApiService? = null
    constructor(apiService: ApiService){
//        this.newApplication = application
        this.newApiService = apiService
    }
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RemoteStarViewModel(this.newApiService!!) as T
    }

}
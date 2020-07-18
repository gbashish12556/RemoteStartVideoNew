package com.example.remotestarvideonew

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RemoteStarViewModeFactory:ViewModelProvider.Factory {

    var newApiService:ApiService? = null
    constructor(apiService: ApiService){
        this.newApiService = apiService
    }
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RemoteStarViewModel(this.newApiService!!) as T
    }

}
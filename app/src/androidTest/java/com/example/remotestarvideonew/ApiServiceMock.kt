package com.example.remotestarvideonew

import android.util.Log
import com.google.gson.Gson
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiServiceMock : ApiService {
    override fun getVideos(apiKeys: String, cacheControl: String?): Call<VideoResponse> {
        return object: Call<VideoResponse> {
            override fun enqueue(callback: Callback<VideoResponse>?) {

            }

            override fun isExecuted(): Boolean {
                return false
            }

            override fun clone(): Call<VideoResponse> {
                Log.d("ApiServiceMock","clone")
                return this
            }

            override fun isCanceled(): Boolean {
                return false
            }

            override fun cancel() {
            }

            override fun request(): Request {
                return Request.Builder().build()
            }

            override fun execute(): Response<VideoResponse> {
                // Create your mock data in here
                val response = TestHelper.getJson(Constants.DUMMY_JSON)
                return Response.success(Gson().fromJson(response, VideoResponse::class.java))
            }

        }
    }
}
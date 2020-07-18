package com.example.remotestarvideonew

import android.app.Application
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient private constructor(context:Application) {
    private val retrofit: Retrofit

    val api: ApiService
        get() = retrofit.create(ApiService::class.java)

    init {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val myCache = Cache(context.cacheDir, Constants.CACHE_SIZE)

        var client = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
//            .cache(myCache)
            .addInterceptor(logging)
            .build()

        retrofit = Retrofit.Builder()
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()

        var errorConverter = retrofit.responseBodyConverter<ErrorBody>(
            ErrorBody::class.java,
            arrayOfNulls<Annotation>(0)
        )


    }

    companion object {

        private var mRetrofitInstance: RetrofitClient? = null

        fun getInstance(context: Application):RetrofitClient{

            if (mRetrofitInstance == null) {
                synchronized(this) {
                    if (mRetrofitInstance == null) {
                        mRetrofitInstance = RetrofitClient(context)
                    }

                }
            }
            return mRetrofitInstance as RetrofitClient
        }

    }
}
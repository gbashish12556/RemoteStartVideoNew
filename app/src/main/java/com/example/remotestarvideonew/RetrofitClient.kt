package com.example.remotestarvideonew

import android.app.Application
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.CacheControl
import okhttp3.Interceptor
import retrofit2.Response
import java.io.IOException


class RetrofitClient private constructor(context:Application) {
    private val retrofit: Retrofit

    val api: ApiService
        get() = retrofit.create(ApiService::class.java)

    init {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val myCache = Cache(context.cacheDir, Constants.CACHE_SIZE)

        var client = OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS)
            .cache(myCache)
            .addInterceptor(logging)
            .addNetworkInterceptor(provideCacheInterceptor())
            .addInterceptor(provideOfflineCacheInterceptor())
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

    private fun provideCacheInterceptor(): Interceptor {

        return Interceptor { chain ->
            var request = chain.request()
            val originalResponse = chain.proceed(request)
            val cacheControl = originalResponse.header("Cache-Control")

            if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache")
            ) {


                val cc = CacheControl.Builder()
                    .maxStale(2, TimeUnit.HOURS)
                    .build()



                request = request.newBuilder()
                    .cacheControl(cc)
                    .build()

                chain.proceed(request)

            } else {
                originalResponse
            }
        }

    }


    private fun provideOfflineCacheInterceptor(): Interceptor {

        return Interceptor { chain ->
            try {
                return@Interceptor chain.proceed(chain.request())
            } catch (e: Exception) {


                val cacheControl = CacheControl.Builder()
                    .onlyIfCached()
                    .maxStale(1, TimeUnit.DAYS)
                    .build()

                val offlineRequest = chain.request().newBuilder()
                    .cacheControl(cacheControl)
                    .build()
                return@Interceptor chain.proceed(offlineRequest)
            }
        }
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
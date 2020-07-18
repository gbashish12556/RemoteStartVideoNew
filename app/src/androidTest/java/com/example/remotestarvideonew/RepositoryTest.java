package com.example.remotestarvideonew;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import java.net.HttpURLConnection;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@RunWith(AndroidJUnit4.class)
public class RepositoryTest {


    private MockWebServer mockWebServer = new MockWebServer();
    private ApiService apiService;


    @Before
    public void setup() {
        try {
            mockWebServer.start();
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            apiService = new Retrofit.Builder()
                    .baseUrl(mockWebServer.url("/"))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                    .create(ApiService.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @After
    public void teardown() {
        try {
            mockWebServer.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void  testRetrofitCall_CheckForValidResponse() {
        // Assign
        MockResponse response = (new MockResponse())
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(TestHelper.getJson(Constants.DUMMY_JSON));
        response.throttleBody(1024, 1, TimeUnit.SECONDS);
        mockWebServer.enqueue(response);
        // Act
        Response<VideoResponse> videoResponse = null;
        try {
            videoResponse = apiService.getVideos(Constants.API_KEY, Constants.NO_CACHHE).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert(videoResponse.body().getResults().size() > 0);

    }

}

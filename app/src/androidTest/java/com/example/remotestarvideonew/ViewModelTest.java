package com.example.remotestarvideonew;


import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class ViewModelTest {

//    @Rule
//    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    Call<VideoResponse> callResponse;

    @Mock
    ApiService apiService;

    @Mock
    Observer<VideoResponse> dataObserver;

    @Mock
    Observer<Boolean> apiStatusObserver;

    private RemoteStarViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        viewModel = new RemoteStarViewModel(apiService);
    }

    @Test
    public void testFetchVideoData_whenReturnData(){
        //Assemble

        Call<VideoResponse> mockedCall = (new ApiServiceMock()).getVideos(Constants.API_KEY,null);

        doReturn(callResponse).when(apiService).getVideos(Constants.API_KEY,"no-cache");

        Mockito.doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Callback<VideoResponse> callback = invocation.getArgument(0);
                String response = TestHelper.getJson(Constants.DUMMY_JSON);
                callback.onResponse(mockedCall, Response.success(new Gson().fromJson(response, VideoResponse.class)));
                return null;
            }
        }).when(callResponse).enqueue(any(Callback.class));

        //Act
        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            @Override
            public void run() {
                viewModel.getVideos().observeForever(dataObserver);
            }
        });
        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            @Override
            public void run() {
                viewModel.getApiStatus().observeForever(apiStatusObserver);
            }
        });

        viewModel.fetchVideos("no-cache");

        //Verify
        VideoResponse finalData = (new Gson()).fromJson(TestHelper.getJson(Constants.DUMMY_JSON),VideoResponse.class);
        verify(apiStatusObserver).onChanged(true);
        verify(dataObserver).onChanged(finalData);

    }

    @Test
    public void testFetchVideoData_whenReturnFailed(){
        //Assemble

        Call<VideoResponse> mockedCall = (new ApiServiceMock()).getVideos(Constants.API_KEY,null);

        doReturn(callResponse).when(apiService).getVideos(Constants.API_KEY,"no-cache");

        Mockito.doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Callback<VideoResponse> callback = invocation.getArgument(0);
                callback.onFailure(mockedCall, new Exception("Fetch Failed"));
                return null;
            }
        }).when(callResponse).enqueue(any(Callback.class));

        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            @Override
            public void run() {
                viewModel.getApiStatus().observeForever(apiStatusObserver);
            }
        });

        viewModel.fetchVideos("no-cache");

        verify(apiStatusObserver).onChanged(false);

    }



}


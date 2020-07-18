package com.example.remotestarvideonew;

import androidx.test.platform.app.InstrumentationRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class TestHelper {

    /**
     * Source: https://stackoverflow.com/questions/29378552/in-espresso-how-to-choose-one-the-view-who-have-same-id-to-avoid-ambiguousviewm
     */


    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    public static String getJson(String path) {

        InputStreamReader stream = null;

        try {
            stream = new InputStreamReader(InstrumentationRegistry.getInstrumentation().getContext().getResources().getAssets().open(path));
            BufferedReader streamReader = new BufferedReader(stream);
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            return responseStrBuilder.toString();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return "";

    }

}
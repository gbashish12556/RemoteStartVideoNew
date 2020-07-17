package com.example.remotestarvideonew;

import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

public class TestHelper {

    /**
     * Source: https://stackoverflow.com/questions/29378552/in-espresso-how-to-choose-one-the-view-who-have-same-id-to-avoid-ambiguousviewm
     */


    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }


}
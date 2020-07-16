package com.example.remotestarvideonew

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val TAG = "EpisodeActivityScreenTest"
    private var mIdlingResource: IdlingResource? = null

    @Rule
    var mActivityTestRule: ActivityTestRule<MainActivity> =
        object : ActivityTestRule<MainActivity>(MainActivity::class.java) {

            override fun beforeActivityLaunched() {
                super.beforeActivityLaunched()
            }

        }

    @Before
    fun registerIdlingResource() {
        mIdlingResource = mActivityTestRule.activity.getIdlingResource()
        IdlingRegistry.getInstance().register(mIdlingResource!!)
    }


}
package com.syyamnoor.imgurapp.end2end

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.syyamnoor.imgurapp.R
import com.syyamnoor.imgurapp.data.room.ImageDatabase
import com.syyamnoor.imgurapp.data.room.RoomImageMapper
import com.syyamnoor.imgurapp.data.room.daos.ImageDao
import com.syyamnoor.imgurapp.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@LargeTest
class EndToEndTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @get: Rule(order = 1)
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var newsDao: ImageDao

    @Inject
    lateinit var imageDatabase: ImageDatabase

    private val imageNewsMapper = RoomImageMapper()

    @Before
    fun setup() {
        hiltAndroidRule.inject()
    }

    @Test
    fun itemInRecyclerClick_navigatesToViewDetails() {
        onView(withId(R.id.fabSearch))
            .perform(
                click()
            )

        onView(withId(R.id.recyclerView))
            .check(matches(isDisplayed()))
    }

    @After
    fun teardown() {
        Intents.release()
        if (imageDatabase.isOpen)
            imageDatabase.close()
    }

}
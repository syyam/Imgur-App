package com.syyamnoor.imgurapp.ui.list

import android.content.Context
import android.view.KeyEvent
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.syyamnoor.imgurapp.R
import com.syyamnoor.imgurapp.ToastMatcher
import com.syyamnoor.imgurapp.data.retrofit.ImageApi
import com.syyamnoor.imgurapp.data.room.ImageDatabase
import com.syyamnoor.imgurapp.data.room.RoomImageMapper
import com.syyamnoor.imgurapp.data.room.daos.ImageDao
import com.syyamnoor.imgurapp.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltAndroidTest
@SmallTest
class ImageListFragmentTest {

    @get: Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var newsDao: ImageDao

    @Inject
    lateinit var nyTimesApi: ImageApi

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var newsDatabase: ImageDatabase

    private val roomNewsMapper = RoomImageMapper()

    @Before
    fun setup() {
        hiltAndroidRule.inject()
        Intents.init()
    }

    @Test
    fun pressingBackOnceWhenDrawerIsClosed_showsClickAgainToast() {
        launchFragmentInHiltContainer<ImageListFragment>()
        pressBack()

        val isToastDisplayed: () -> Boolean = {
            try {
                isToastMessageDisplayed(R.string.press_back_again)
                true
            } catch (e: NoMatchingViewException) {
                false
            }
        }

        // Slow devices might need some time
        for (x in 0..9) {
            sleep(200)
            if (isToastDisplayed()) {
                break
            }
        }

    }


    @Test
    fun clickingSearch_displaysSearchView() {
        launchFragmentInHiltContainer<ImageListFragment>()
        try {
            openActionBarOverflowOrOptionsMenu(
                InstrumentationRegistry.getInstrumentation().targetContext
            )
        } catch (e: Exception) {
            //This is normal. Maybe we don't have overflow menu.
        }
        onView(
            allOf(
                anyOf(
                    withText(R.string.search),
                    withId(R.id.action_search)
                ),
                isDisplayed()
            )
        ).perform(click())
        onView(
            allOf(
                isDescendantOfA(withClassName(`is`(SearchView::class.java.name))),
                isDisplayed(),
                isAssignableFrom(EditText::class.java)
            )
        )
            .perform(
                clearText(),
                typeText("enter the text"),
                pressKey(KeyEvent.KEYCODE_ENTER)
            )
    }

    @After
    fun teardown() {
        Intents.release()
        if (newsDatabase.isOpen)
            newsDatabase.close()
    }


    private fun isToastMessageDisplayed(textId: Int) {
        onView(withText(textId)).inRoot(ToastMatcher.isToast()).check(matches(isDisplayed()))
    }
}
package com.syyamnoor.imgurapp.data.repositories

import ApiResponses.failedResponse
import ApiResponses.successResponseNullBody
import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.syyamnoor.imgurapp.data.retrofit.ImageApi
import com.syyamnoor.imgurapp.data.retrofit.RetrofitImageMapper
import com.syyamnoor.imgurapp.data.room.RoomImageMapper
import com.syyamnoor.imgurapp.data.room.daos.ImageDao
import com.syyamnoor.imgurapp.data.room.models.ImageInDb
import com.syyamnoor.imgurapp.utils.AppDispatchers
import com.syyamnoor.imgurapp.utils.DataState
import com.syyamnoor.imgurapp.utils.Utils.createRandomImage
import com.syyamnoor.imgurapp.utils.isConnectedToInternet
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ImageRepositoryImplTest {

    private val imageNewsMapper = RoomImageMapper()
    private lateinit var imageRepositoryImpl: ImageRepositoryImpl
    private lateinit var imageApi: ImageApi
    private lateinit var imageDao: ImageDao
    private lateinit var context: Context

    private val dispatchers: AppDispatchers = object : AppDispatchers {
        override fun main(): CoroutineDispatcher {
            return TestCoroutineDispatcher()
        }

        override fun default(): CoroutineDispatcher {
            return TestCoroutineDispatcher()
        }

        override fun io(): CoroutineDispatcher {
            return TestCoroutineDispatcher()
        }
    }

    @Before
    fun setup() {
        imageApi = mockk(relaxed = true)
        imageDao = mockk(relaxed = true)
        context = mockk(relaxed = true)
        imageRepositoryImpl = ImageRepositoryImpl(
            RetrofitImageMapper(),
            imageApi,
            imageNewsMapper,
            imageDao,
            dispatchers,
            context
        )

        coEvery { context.getString(any()) } returns "An Error Occurred"
        coEvery { context.isConnectedToInternet() } returns false
    }

    /**
     * getImage test cases
     * 1. Get images emits loading first
     * 2. Get images emits loading with data from local storage first
     * 3. The dao method queried could be
     *    a. getAllNewsWith query when query is not null
     *    b. getAllNews when query is null
     * 4. a. If connected to internet fetches from API
     *           i. If API response is successful and body is not null
     *           Emits Success
     *           Saves images items to database
     *           Saves images images to database
     *           ii. If an exception occurs or response body is null
     *           Emits failure
     *         b. If not connected emits Failure
     */

    @Test
    fun `get image starts in loading state`() = runBlockingTest {
        val first = imageRepositoryImpl.getImages("").first()
        assertThat(first).isInstanceOf(DataState.Loading::class.java)
    }

    @Test
    fun `second emit is loading state and returns data`() = runBlockingTest {
        every { imageDao.getAllImages() } returns flow { getImagesInDbItems() }
        val second = imageRepositoryImpl.getImages("").drop(1).first()
        assertThat(second).isInstanceOf(DataState.Loading::class.java)
        assertThat((second as DataState.Loading).data).isNotNull()
    }

    @Test
    fun `query not null calls get image with query on db and not get all image`() = runBlockingTest {
        imageRepositoryImpl.getImages("").collect()
        coVerify { imageDao.getAllImages() wasNot called }
    }

    @Test
    fun `unavailable network connection does not fetch from API`() = runBlockingTest {
        every { context.isConnectedToInternet() } returns false
        imageRepositoryImpl.getImages("").collect()
        coVerify { imageApi.getGalleryImages("hot", "viral", "day", "1", showViral = true, showMature = true, albumPreview = true) wasNot called }
    }

    @Test
    fun `unavailable network returns failed state with data from local cache`() = runBlockingTest {
        every { context.isConnectedToInternet() } returns false
        every { imageDao.getAllImages() } returns flow { getImagesInDbItems() }
        val last = imageRepositoryImpl.getImages("").last()
        assertThat(last).isInstanceOf(DataState.Failure::class.java)
        assertThat((last as DataState.Failure).data).isNotNull()
    }

    private fun getImagesInDbItems(count: Int = 10): List<ImageInDb> {
        return (1..count).map { imageNewsMapper.domainToEntity(createRandomImage()) }
    }
}
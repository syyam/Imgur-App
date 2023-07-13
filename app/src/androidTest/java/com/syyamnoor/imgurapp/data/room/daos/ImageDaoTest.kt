package com.syyamnoor.imgurapp.data.room.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.syyamnoor.imgurapp.data.room.ImageDatabase
import com.syyamnoor.imgurapp.data.room.RoomImageMapper
import com.syyamnoor.imgurapp.utils.Utils.createRandomImage
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@SmallTest
class ImageDaoTest {

    @get: Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    @get: Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var imageDao: ImageDao

    @Inject
    lateinit var roomImageMapper: RoomImageMapper

    @Inject
    lateinit var imageDatabase: ImageDatabase

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    @Test
    fun queryGetNews_returnsEmptyList() = runBlockingTest {
        insertRandomNewsToDb()
        val job = launch {
            val last = imageDao.getAllImages().last()
            assertThat(last.size).isEqualTo(0)
        }
        job.cancel()
    }

    @Test
    fun deleteNews_returnsEmptyList() = runBlockingTest {
        insertRandomNewsToDb()
        val job = launch {
            imageDao.deleteAllImages()
            val last = imageDao.getAllImages().last()
            assertThat(last.size).isEqualTo(0)
        }
        job.cancel()
    }

    @Test
    fun getNewsWithWrongId_returnsNull() = runBlockingTest {
        val imageId = "12"
        val insertedNewsInDb =
            roomImageMapper.domainToEntity(createRandomImage().copy(id = imageId))
        val job = launch {
            imageDao.insert(insertedNewsInDb)
            val last = imageDao.getAllImagesWithQuery("12").last()
            val fetchedNews = last
            assertThat(fetchedNews).isNull()
        }
        job.cancel()
    }

    private fun insertRandomNewsToDb(count: Int = 50) {
        (1..count).map { createRandomImage() }
            .forEach { imageDao.insert(roomImageMapper.domainToEntity(it)) }
    }

    @After
    fun teardown() {
        if (imageDatabase.isOpen)
            imageDatabase.close()
    }

}
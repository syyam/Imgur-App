package com.syyamnoor.imgurapp.domain.usecases

import com.syyamnoor.imgurapp.data.repositories.ImageRepositoryImpl
import com.syyamnoor.imgurapp.utils.DataState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetNewsTest {

    private lateinit var getImage: GetImagesUseCase
    private lateinit var imageRepositoryImpl: ImageRepositoryImpl

    @Before
    fun setup() {
        imageRepositoryImpl = mockk(relaxed = true)
        getImage = GetImagesUseCase(imageRepositoryImpl)

        coEvery {
            imageRepositoryImpl.getImages(
                any(),
            )
        } returns flow { emit(DataState.Loading()) }
    }

    @Test
    fun `calls repository get news with same query`() = runBlockingTest {
        val query = null
        getImage(query).last()
        coVerify { imageRepositoryImpl.getImages(query) }
    }
}
package com.syyamnoor.imgurapp.ui.list

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.syyamnoor.imgurapp.domain.usecases.GetImagesUseCase
import getOrAwaitValue
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@FlowPreview
class ItemListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var getImage: GetImagesUseCase
    private lateinit var imageListViewModel: ImageListViewModel

    @Before
    fun setup() {
        getImage = mockk(relaxed = true)
        imageListViewModel = ImageListViewModel(getImage)
    }

    @Test
    fun `initializing item list viewmodel fetches data`() = runBlockingTest {
        verify(exactly = 1) { getImage(any()) }
    }

    @Test
    fun `search with blank query changes query in state to null`() = runBlockingTest {
        imageListViewModel.performEvent(ImageListViewModel.ListUiEvent.Search("      "))
        val value = imageListViewModel.listUiState.getOrAwaitValue()
        assertThat(value.query).isNull()
    }

    @Test
    fun `search with new query changes query in state to that value`() = runBlockingTest {
        val newQuery = "some query"
        imageListViewModel.performEvent(ImageListViewModel.ListUiEvent.Search(newQuery))
        val value = imageListViewModel.listUiState.getOrAwaitValue()
        assertThat(value.query).isEqualTo(newQuery)
    }


}

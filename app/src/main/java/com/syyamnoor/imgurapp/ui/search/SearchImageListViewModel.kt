package com.syyamnoor.imgurapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syyamnoor.imgurapp.domain.models.Image
import com.syyamnoor.imgurapp.domain.usecases.GetSearchImagesUseCase
import com.syyamnoor.imgurapp.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class SearchImageListViewModel @Inject constructor(
    private val getImages: GetSearchImagesUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData(ListUiState())
    val listUiState: LiveData<ListUiState>
        get() = _uiState


    private var job: Job? = null

    init {
        performEvent(ListUiEvent.Search(""))
    }

    fun performEvent(listUiEvent: ListUiEvent) {
        val uiStateValue = _uiState.value ?: ListUiState()
        when (listUiEvent) {
            is ListUiEvent.Search -> {
                val newQuery = if (listUiEvent.query == null || listUiEvent.query.isBlank()) ""
                else listUiEvent.query.trim()
                // Nothing has changed
                if (newQuery == uiStateValue.query)
                    return

                _uiState.value = uiStateValue.copy(query = newQuery)
                performQuery()
            }

            is ListUiEvent.Refresh -> {
                performQuery()
            }

        }
    }


    private fun performQuery() {
        // Stop previous emissions of data
        job?.cancel()

        val uiStateValue = _uiState.value ?: ListUiState()
        job = viewModelScope.launch {
            getImages(uiStateValue.query)
                // Wait half a second in case user is still typing
                .debounce(600)
                .collectLatest {
                    when (it) {
                        // Watch for database changes of the results
                        is DataState.Success -> {
                            _uiState.value = uiStateValue.copy(result = it)

                        }
                        // Watch for database changes of the results if they are available
                        is DataState.Failure -> {
                            if (it.data != null) {

                                _uiState.value = uiStateValue.copy(
                                    result =
                                    it
                                )

                            } else {
                                _uiState.value = uiStateValue.copy(
                                    result = DataState.Failure(
                                        it.throwable,
                                        null
                                    )
                                )
                            }
                        }
                        // Watch for database changes of results if they are available
                        is DataState.Loading -> {
                            if (it.data != null) {

                                _uiState.value =
                                    uiStateValue.copy(result = DataState.Loading(it.data))

                            } else {
                                _uiState.value = uiStateValue.copy(result = DataState.Loading())
                            }
                        }
                    }
                }
        }
    }

    sealed class ListUiEvent {
        data class Search(val query: String?) : ListUiEvent()
        object Refresh : ListUiEvent()
    }

    data class ListUiState(
        val result: DataState<List<Image>> = DataState.Loading(),
        val query: String? = null,
    )
}
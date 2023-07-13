package com.syyamnoor.imgurapp.domain.repositories

import com.syyamnoor.imgurapp.domain.models.Image
import com.syyamnoor.imgurapp.utils.DataState
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    fun getImages(query: String?): Flow<DataState<Flow<List<Image>>>>

    fun getSearchImages(query: String?): Flow<DataState<List<Image>>>

}
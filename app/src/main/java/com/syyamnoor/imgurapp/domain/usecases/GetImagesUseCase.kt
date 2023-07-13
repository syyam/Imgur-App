package com.syyamnoor.imgurapp.domain.usecases

import com.syyamnoor.imgurapp.domain.models.Image
import com.syyamnoor.imgurapp.domain.repositories.ImageRepository
import com.syyamnoor.imgurapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetImagesUseCase @Inject constructor(val imageRepository: ImageRepository) {

    operator fun invoke(query: String?): Flow<DataState<Flow<List<Image>>>> {
        return imageRepository.getImages(query)
    }

}
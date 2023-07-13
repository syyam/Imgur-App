package com.syyamnoor.imgurapp.data.repositories

import android.content.Context
import com.syyamnoor.imgurapp.R
import com.syyamnoor.imgurapp.data.retrofit.ImageApi
import com.syyamnoor.imgurapp.data.retrofit.RetrofitImageMapper
import com.syyamnoor.imgurapp.data.room.RoomImageMapper
import com.syyamnoor.imgurapp.data.room.daos.ImageDao
import com.syyamnoor.imgurapp.data.room.models.ImageInDb
import com.syyamnoor.imgurapp.domain.models.Image
import com.syyamnoor.imgurapp.domain.repositories.ImageRepository
import com.syyamnoor.imgurapp.utils.AppDispatchers
import com.syyamnoor.imgurapp.utils.DataState
import com.syyamnoor.imgurapp.utils.isConnectedToInternet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

@ExperimentalCoroutinesApi
class ImageRepositoryImpl @Inject constructor(
    private val retrofitImageMapper: RetrofitImageMapper,
    private val imageApi: ImageApi,
    private val roomImageMapper: RoomImageMapper,
    private val imageDao: ImageDao,
    private val dispatchers: AppDispatchers,
    private val context: Context
) : ImageRepository {

    override fun getImages(query: String?): Flow<DataState<Flow<List<Image>>>> =
        flow { // Display loading with no items
            emit(DataState.Loading())

            val dbFlow: Flow<List<ImageInDb>> = when {
                (query != null) -> imageDao.getAllImagesWithQuery("%$query%").distinctUntilChanged()
                else -> imageDao.getAllImages().distinctUntilChanged()
            }

            val imageItems = dbFlow.mapLatest {
                roomImageMapper.entityListToDomainList(it)
            }


            // Display loading with current items
            emit(DataState.Loading(imageItems))

            var isSuccessful: Boolean
            var throwable: Throwable? = null
            val genericException = Exception(context.getString(R.string.geeric_error))
            val internetException = Exception(context.getString(R.string.internet_connection_error))
            if (context.isConnectedToInternet()) {
                try {
                    imageDao.deleteAllImages()
                    val viewedNews = imageApi.getGalleryImages(
                        "hot",
                        "viral",
                        "day",
                        "1",
                        showViral = true,
                        showMature = true,
                        albumPreview = true
                    )
                    val imageResponse = viewedNews.body() // API call was successful
                    if (viewedNews.isSuccessful && imageResponse != null) {
                        isSuccessful = true
                        val domainModels =
                            retrofitImageMapper.entityListToDomainList(imageResponse.data)
                        val roomModels = roomImageMapper.domainListToEntityList(domainModels)

                        // Create lists of the items and images to insert at once
                        val imageModelItems = mutableListOf<ImageInDb>()
                        roomModels.forEach {
                            imageModelItems.add(it)
                        } // Cache the data in local storage
                        imageDao.upsert(imageModelItems)
                    } else if (viewedNews.code() == 429) {
                        isSuccessful = false
                        throwable = Exception(context.getString(R.string.too_many_requests))
                    } else if (viewedNews.errorBody() != null) {
                        isSuccessful = false
                        throwable = Exception(viewedNews.errorBody().toString())
                    } else {
                        isSuccessful = false
                        throwable = genericException
                    }
                }catch (e: SSLHandshakeException) {
                    isSuccessful = false
                    throwable = internetException
                } catch (e: Exception) {
                    isSuccessful = false
                    throwable = e
                }

            } else {
                isSuccessful = false
                throwable = internetException
            }

            // Emit result with current value in DB
            emit(
                if (isSuccessful) DataState.Success(imageItems)
                else DataState.Failure(
                    throwable ?: genericException, imageItems
                )
            )
        }.flowOn(dispatchers.io())

    override fun getSearchImages(query: String?): Flow<DataState<List<Image>>> =
        flow { // Display loading with no items
            emit(DataState.Loading())

            // Display loading with current items
            var throwable: Throwable? = null
            val genericException = Exception(context.getString(R.string.geeric_error))
            val internetException = Exception(context.getString(R.string.internet_connection_error))

            if (context.isConnectedToInternet()) {
                try {
                    val viewedNews = imageApi.getSearchGalleryImages(
                        search = query!!
                    )
                    val imageResponse = viewedNews.body() // API call was successful
                    if (viewedNews.isSuccessful && imageResponse != null) {
                        val domainModels =
                            retrofitImageMapper.entityListToDomainList(imageResponse.data)

                        emit(DataState.Success(domainModels))

                    } else if (viewedNews.code() == 429) {
                        throwable = Exception(context.getString(R.string.too_many_requests))
                        emit(
                            DataState.Failure(
                                throwable
                            )
                        )
                    } else if (viewedNews.errorBody() != null) {
                        throwable = Exception(viewedNews.errorBody().toString())
                        emit(
                            DataState.Failure(
                                throwable
                            )
                        )
                    } else {
                        throwable = genericException
                        emit(
                            DataState.Failure(
                                throwable
                            )
                        )
                    }
                }catch (e: SSLHandshakeException) {
                    throwable = internetException

                    emit(
                         DataState.Failure(
                            throwable
                        )
                    )
                } catch (e: Exception) {
                    emit(
                        DataState.Failure(
                            e
                        )
                    )
                }

            } else {
                emit(
                    DataState.Failure(
                        internetException
                    )
                )
            }

        }.flowOn(dispatchers.io())


}
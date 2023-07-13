package com.syyamnoor.imgurapp.di

import android.content.Context
import com.syyamnoor.imgurapp.data.repositories.ImageRepositoryImpl
import com.syyamnoor.imgurapp.data.retrofit.ImageApi
import com.syyamnoor.imgurapp.data.retrofit.RetrofitImageMapper
import com.syyamnoor.imgurapp.data.room.RoomImageMapper
import com.syyamnoor.imgurapp.data.room.daos.ImageDao
import com.syyamnoor.imgurapp.domain.repositories.ImageRepository
import com.syyamnoor.imgurapp.utils.AppDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideDispatchers(): AppDispatchers {
        return object : AppDispatchers {
            override fun main(): CoroutineDispatcher {
                return Dispatchers.Main
            }

            override fun default(): CoroutineDispatcher {
                return Dispatchers.Default
            }

            override fun io(): CoroutineDispatcher {
                return Dispatchers.IO
            }

        }
    }

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideImageRepository(
        retrofitImageMapper: RetrofitImageMapper, imageApi: ImageApi,
        roomImageMapper: RoomImageMapper,
        imageDao: ImageDao,
        appDispatchers: AppDispatchers, @ApplicationContext context: Context
    ): ImageRepository
    {
        return ImageRepositoryImpl(
            retrofitImageMapper,
            imageApi,
            roomImageMapper,
            imageDao,
            appDispatchers,
            context
        )
    }
}
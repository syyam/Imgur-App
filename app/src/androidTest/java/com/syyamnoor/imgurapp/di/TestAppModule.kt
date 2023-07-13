package com.syyamnoor.imgurapp.di

import android.content.Context
import com.syyamnoor.imgurapp.data.repositories.ImageRepositoryImpl
import com.syyamnoor.imgurapp.data.retrofit.ImageApi
import com.syyamnoor.imgurapp.data.retrofit.RetrofitImageMapper
import com.syyamnoor.imgurapp.data.room.RoomImageMapper
import com.syyamnoor.imgurapp.data.room.daos.ImageDao
import com.syyamnoor.imgurapp.domain.repositories.ImageRepository
import com.syyamnoor.imgurapp.utils.AppDispatchers
import com.syyamnoor.imgurapp.utils.isConnectedToInternet
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
@TestInstallIn(components = [SingletonComponent::class], replaces = [AppModule::class])
@Module
object TestAppModule {

    @Provides
    fun provideContext(): Context {
        val context = mockk<Context>(relaxed = true)
        every { context.getString(any()) } returns "Some Error Message"
        every { context.isConnectedToInternet() } returns true
        return context
    }

    @Provides
    fun provideDispatchers(): AppDispatchers {
        return object : AppDispatchers {
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
    }


    @ExperimentalCoroutinesApi
    @Provides
    fun provideNewsRepository(
        retrofitNewsMapper: RetrofitImageMapper, imageApi: ImageApi,
        imageNewsMapper: RoomImageMapper,
        imageDao: ImageDao,
        appDispatchers: AppDispatchers, context: Context
    ): ImageRepository {
        return ImageRepositoryImpl(
            retrofitNewsMapper,
            imageApi,
            imageNewsMapper,
            imageDao,
            appDispatchers,
            context
        )
    }
}
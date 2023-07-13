package com.syyamnoor.imgurapp.di

import com.syyamnoor.imgurapp.data.retrofit.ImageApi
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [RetrofitModule::class])
object TestRetrofitModule {

    @Singleton
    @Provides
    fun provideNyTimesApi(): ImageApi {
        val mockk = mockk<ImageApi>(relaxed = true)
        return mockk
    }

}
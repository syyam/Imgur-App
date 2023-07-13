package com.syyamnoor.imgurapp.di

import com.syyamnoor.imgurapp.data.retrofit.utils.GsonDateDeSerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.syyamnoor.imgurapp.BuildConfig
import com.syyamnoor.imgurapp.data.retrofit.ImageApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    private const val BASE_URL = BuildConfig.BASE_URL

    @Singleton
    @Provides
    fun provideGson(dateDeSerializer: GsonDateDeSerializer): Gson {
        return GsonBuilder().registerTypeAdapter(Date::class.java, dateDeSerializer).create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getAuthorizationHeaderInterceptor())
            .build()
    }
    @Singleton
    @Provides
    fun getAuthorizationHeaderInterceptor(): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor { chain ->
            val originalRequest = chain.request()
            val builder = originalRequest.newBuilder().header(
                "Authorization",
                "Client-ID " + BuildConfig.CLIENT_ID
            )
            val newRequest = builder.build()
            chain.proceed(newRequest)
        }.build()
    }

    @Singleton
    @Provides
    fun provideImgurApi(retrofit: Retrofit): ImageApi {
        return retrofit.create(ImageApi::class.java)
    }

}
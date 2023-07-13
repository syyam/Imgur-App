package com.syyamnoor.imgurapp.di

import android.content.Context
import androidx.room.Room
import com.syyamnoor.imgurapp.data.room.ImageDatabase
import com.syyamnoor.imgurapp.data.room.ImageDatabase.Companion.IMAGE_DATABASE_NAME
import com.syyamnoor.imgurapp.data.room.daos.ImageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {


    @Singleton
    @Provides
    fun provideImageRoomDatabase(@ApplicationContext context: Context): ImageDatabase {
        return Room.databaseBuilder(context, ImageDatabase::class.java, IMAGE_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideImageDao(imageDatabase: ImageDatabase): ImageDao {
        return imageDatabase.imageDao
    }

}
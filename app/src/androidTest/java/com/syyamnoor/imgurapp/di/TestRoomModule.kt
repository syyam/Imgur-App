package com.syyamnoor.imgurapp.di

import android.content.Context
import androidx.room.Room
import com.syyamnoor.imgurapp.data.room.ImageDatabase
import com.syyamnoor.imgurapp.data.room.daos.ImageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(components = [SingletonComponent::class], replaces = [RoomModule::class])
@Module
object TestRoomModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): ImageDatabase {
        return Room.inMemoryDatabaseBuilder(context, ImageDatabase::class.java)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsDao(imageDatabase: ImageDatabase): ImageDao {
        return imageDatabase.imageDao
    }
}
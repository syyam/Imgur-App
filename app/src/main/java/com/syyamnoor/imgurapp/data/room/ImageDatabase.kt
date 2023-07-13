package com.syyamnoor.imgurapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.syyamnoor.imgurapp.data.room.daos.ImageDao
import com.syyamnoor.imgurapp.data.room.models.ImageInDb
import com.syyamnoor.imgurapp.data.room.utils.DateConverter

@Database(entities = [ImageInDb::class], version = 2, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class ImageDatabase : RoomDatabase() {

    abstract val imageDao: ImageDao

    companion object {
        const val IMAGE_DATABASE_NAME = "image_db"
    }
}
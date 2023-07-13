package com.syyamnoor.imgurapp.data.room.daos

import androidx.room.*
import com.syyamnoor.imgurapp.data.room.models.ImageInDb
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ImageDao : BaseDao<ImageInDb>() {

    @Transaction
    @Query("SELECT * FROM image")
    abstract fun getAllImages(): Flow<List<ImageInDb>>

    @Query("DELETE FROM image")
    abstract fun deleteAllImages()

    @Transaction
    @Query(
        "SELECT * FROM image WHERE title LIKE :query OR description LIKE :query OR topic LIKE :query OR topic LIKE :query"
    )
    abstract fun getAllImagesWithQuery(
        query: String,
    ): Flow<List<ImageInDb>>
}
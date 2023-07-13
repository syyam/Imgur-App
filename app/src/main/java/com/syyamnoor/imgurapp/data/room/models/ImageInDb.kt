package com.syyamnoor.imgurapp.data.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.syyamnoor.imgurapp.data.retrofit.models.ImageResponse

@Entity(
    tableName = "image", indices = [Index("id", unique = true), Index("title"), Index("description"),
        Index("link"), Index("nsfw"), Index("topic"), Index("image")]
)
data class ImageInDb(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "link")
    val link: String,
    @ColumnInfo(name = "nsfw")
    val nsfw: Boolean,
    @ColumnInfo(name = "topic")
    val topic: String,
    @ColumnInfo(name = "image")
    val images: String,

)
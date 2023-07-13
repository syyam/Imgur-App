package com.syyamnoor.imgurapp.data.retrofit

import com.google.common.truth.Truth.assertThat
import com.syyamnoor.imgurapp.data.retrofit.models.ImageResponse
import com.syyamnoor.imgurapp.data.retrofit.models.Tag
import com.syyamnoor.imgurapp.domain.models.Image
import org.junit.Before
import org.junit.Test
import java.util.*

class RetrofitNewsMapperTest
{

    private lateinit var retrofitImageMapper: RetrofitImageMapper
    private lateinit var imageResponse: ImageResponse
    private lateinit var image: Image

    @Before
    fun setUp()
    {
        retrofitImageMapper = RetrofitImageMapper()

        val id = "1231"
        val title = "title"
        val description = "Abstract"
        val publishDate = 0
        val cover = "Category"
        val coverWidth = 0
        val coverHeight = 0
        val accountUrl = "link"
        val accountId = 0
        val privacy = "2022"
        val layout = "link"
        val views = 0
        val link = "link"
        val ups = 0
        val downs = 0
        val points = 0
        val score = 0
        val isAlbum = false
        val vote = "link"
        val favorite = false
        val nsfw = false
        val section = "link"
        val commentCount = 0
        val topic = 0
        val topicId = ""
        val favoriteCount = 0
        val imagesCount = 0
        val inGallery = false
        val isAd = false
        val adType = emptyList<Tag>()
        val adUrl = 0
        val inMostViral = "false"
        val includeAlbumAds = false
        val images = false
        val adConfig = emptyList<ImageResponse>()

        // Initialize image in Retrofit
        imageResponse = ImageResponse(
            id,
            title,
            description,
            publishDate,
            cover,
            coverWidth,
            coverHeight,
            accountUrl,
            accountId,
            privacy,
            layout,
            views,
            link,
            ups,
            downs,
            points,
            score,
            isAlbum,
            vote,
            favorite,
            nsfw,
            section,
            commentCount,
            topic,
            topicId,
            favoriteCount,
            imagesCount,
            inGallery,
            isAd,
            adType,
            adUrl,
            inMostViral,
            includeAlbumAds,
            images,
            adConfig

        )


        image = Image(
            id, title, description, link, nsfw, topicId, link
        )
    }

}
package com.syyamnoor.imgurapp.data.room

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.syyamnoor.imgurapp.data.room.models.ImageInDb
import com.syyamnoor.imgurapp.domain.models.Image
import org.junit.Before
import org.junit.Test

@SmallTest
class RoomNewsMapperTest
{

    private lateinit var roomImageMapper: RoomImageMapper
    private lateinit var imageInDb: ImageInDb
    private lateinit var image: Image

    @Before
    fun setUp()
    {
        roomImageMapper = RoomImageMapper()

        val title = "title"
        val imageUrl = "Category"
        val imageId = "123"
        val description = "description"
        val topic = "link"

        // Initialize image in DB

        imageInDb = ImageInDb(
            imageId, title, description, imageUrl, false, topic, imageUrl
        )

        image = Image(
            imageId, title, description, imageUrl, false, topic, imageUrl
        )
    }

    @Test
    fun `image in db is mapped to image correctly`()
    {
        val mappedNews = roomImageMapper.entityToDomain(imageInDb)
        assertThat(mappedNews).isEqualTo(image)
    }

    @Test
    fun `image is mapped to image in db correctly`()
    {
        val mappedNewsInDb = roomImageMapper.domainToEntity(image)
        assertThat(mappedNewsInDb).isEqualTo(imageInDb)
    }

    @Test
    fun `list of image in db is mapped to image correctly`()
    {
        val entitiesList = listOf(imageInDb)
        val mappedNews = roomImageMapper.entityListToDomainList(entitiesList)
        val domainList = listOf(image)
        assertThat(mappedNews).isEqualTo(domainList)
    }

    @Test
    fun `list of image is mapped to image in db correctly`()
    {
        val domainList = listOf(image)
        val mappedNewsInDb = roomImageMapper.domainListToEntityList(domainList)
        val entitiesListOf = listOf(imageInDb)
        assertThat(mappedNewsInDb).isEqualTo(entitiesListOf)
    }

}
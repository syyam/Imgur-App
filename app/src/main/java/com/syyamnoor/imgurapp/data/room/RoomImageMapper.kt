package com.syyamnoor.imgurapp.data.room

import com.syyamnoor.imgurapp.data.room.models.ImageInDb
import com.syyamnoor.imgurapp.domain.models.Image
import com.syyamnoor.imgurapp.domain.utils.EntityDomainMapper
import javax.inject.Inject

class RoomImageMapper
@Inject constructor() : EntityDomainMapper<ImageInDb, Image>() {
    override fun entityToDomain(entity: ImageInDb): Image {

        return Image(
            entity.id, entity.title, entity.description, entity.link, entity.nsfw, entity.topic, entity.images
        )
    }

    override fun domainToEntity(domain: Image): ImageInDb
    {
        return ImageInDb(
            domain.id, domain.title, domain.description, domain.link, domain.nsfw, domain.topic, domain.images
        )
    }
}
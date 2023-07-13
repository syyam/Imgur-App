package com.syyamnoor.imgurapp.data.retrofit

import com.syyamnoor.imgurapp.data.retrofit.models.ImageResponse
import com.syyamnoor.imgurapp.domain.models.Image
import com.syyamnoor.imgurapp.domain.utils.EntityDomainMapper
import javax.inject.Inject

class RetrofitImageMapper
@Inject constructor() : EntityDomainMapper<ImageResponse, Image>() {
    override fun entityToDomain(entity: ImageResponse): Image {

        var desc = ""
        var topic = ""
        var title = ""
        var link = ""
        var nsfw = false
        var image = ""
        if (entity.description != null ){
            desc = entity.description!!
        }
        if (entity.topic != null ){
            topic = entity.topic!!
        }
        if (entity.title != null ){
            title = entity.title!!
        }
        if (entity.link != null ){
            link = entity.link!!
        }
        if (entity.nsfw != null ){
            nsfw = entity.nsfw!!
        }
        if (entity.images != null ){
            if (entity.images.isNotEmpty() && entity.images[0].link != null )
                image = entity.images[0].link!!
        }
        return Image(
            entity.id,
            title,
            desc,
            link,
            nsfw,
            topic,
            image
        )
    }

    //
    override fun domainToEntity(domain: Image): ImageResponse {
        return ImageResponse(
            domain.id,
            domain.title,
            domain.description,
            0,
            "",
            0,
            0,
            "",
            0,
            "",
            "",
            0,
            domain.link,
            0,
            0,
            0,
            0,
            false,
            0,
            false,
            domain.nsfw,
            "",
            0,
            0,
            domain.topic,
            0,
            0,
            false,
            false,
            emptyList(),
            0,
            "",
            false,
            false,
            emptyList(),
            null,
        )
    }
}
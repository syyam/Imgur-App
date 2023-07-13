package com.syyamnoor.imgurapp.domain.models

data class Image(
    val id: String,
    val title: String,
    val description: String,
    val link: String,
    val nsfw: Boolean,
    val topic: String,
    val images: String,

)
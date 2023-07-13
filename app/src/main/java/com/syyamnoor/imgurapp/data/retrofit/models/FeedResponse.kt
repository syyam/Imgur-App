package com.syyamnoor.imgurapp.data.retrofit.models

import com.google.gson.annotations.SerializedName

data class FeedResponse(

    @SerializedName("data")
    var data: List<ImageResponse>,

    @SerializedName("success")
    var success: Boolean? = null,

    @SerializedName("status")
    var status: Int? = null
)
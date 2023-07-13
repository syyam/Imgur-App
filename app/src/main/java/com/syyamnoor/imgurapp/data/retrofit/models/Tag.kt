package com.syyamnoor.imgurapp.data.retrofit.models

import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("name")
    var name: String? = null,

    @SerializedName("display_name")
    var displayName: String? = null,

    @SerializedName("followers")
    var followers: Int? = null,

    @SerializedName("total_items")
    var totalItems: Int? = null,

    @SerializedName("following")
    var following: Boolean? = null,

    @SerializedName("is_whitelisted")
    var isWhitelisted: Boolean? = null,

    @SerializedName("background_hash")
    var backgroundHash: String? = null,

    @SerializedName("thumbnail_hash")
    var thumbnailHash: Any? = null,

    @SerializedName("accent")
    var accent: Any? = null,

    @SerializedName("background_is_animated")
    var backgroundIsAnimated: Boolean? = null,

    @SerializedName("thumbnail_is_animated")
    var thumbnailIsAnimated: Boolean? = null,

    @SerializedName("is_promoted")
    var isPromoted: Boolean? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("logo_hash")
    var logoHash: Any? = null,

    @SerializedName("logo_destination_url")
    var logoDestinationUrl: Any? = null,

    @SerializedName("description_annotations")
    var descriptionAnnotations: Any? = null
)
package com.syyamnoor.imgurapp.data.retrofit.models

import com.google.gson.annotations.SerializedName

data class ImageResponse(

    @SerializedName("id")
    var id: String = "",

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("datetime")
    var datetime: Int,

    @SerializedName("cover")
    var cover: String,

    @SerializedName("cover_width")
    var coverWidth: Int,

    @SerializedName("cover_height")
    var coverHeight: Int,

    @SerializedName("account_url")
    var accountUrl: String,

    @SerializedName("account_id")
    var accountId: Int,

    @SerializedName("privacy")
    var privacy: String,

    @SerializedName("layout")
    var layout: String,

    @SerializedName("views")
    var views: Int,

    @SerializedName("link")
    var link: String? = null,

    @SerializedName("ups")
    var ups: Int,

    @SerializedName("downs")
    var downs: Int,

    @SerializedName("points")
    var points: Int,

    @SerializedName("score")
    var score: Int,

    @SerializedName("is_album")
    var isAlbum: Boolean,

    @SerializedName("vote")
    var vote: Any,

    @SerializedName("favorite")
    var favorite: Boolean,

    @SerializedName("nsfw")
    var nsfw: Boolean? = null,

    @SerializedName("section")
    var section: String,

    @SerializedName("comment_count")
    var commentCount: Int,

    @SerializedName("favorite_count")
    var favoriteCount: Int,

    @SerializedName("topic")
    var topic: String? = null,

    @SerializedName("topic_id")
    var topicId: Int,

    @SerializedName("images_count")
    var imagesCount: Int,

    @SerializedName("in_gallery")
    var inGallery: Boolean,

    @SerializedName("is_ad")
    var isAd: Boolean,

    @SerializedName("tags")
    var tags: List<Tag>,

    @SerializedName("ad_type")
    var adType: Int,

    @SerializedName("ad_url")
    var adUrl: String,

    @SerializedName("in_most_viral")
    var inMostViral: Boolean,

    @SerializedName("include_album_ads")
    var includeAlbumAds: Boolean,

    @SerializedName("images")
    var images: List<ImageResponse>,

    @SerializedName("ad_config")
    var adConfig: AdConfig? = null
)
package com.syyamnoor.imgurapp.data.retrofit

import com.syyamnoor.imgurapp.data.retrofit.models.FeedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImageApi {
    @GET("gallery/{section}/{sort}/{window}/{page}")
    suspend fun getGalleryImages(
        @Path("section") section : String,
        @Path("sort") sort : String,
        @Path("window") window : String,
        @Path("page") page : String,
        @Query("showViral") showViral : Boolean,
        @Query("mature") showMature : Boolean,
        @Query("album_previews") albumPreview : Boolean
    ) : Response<FeedResponse>


    @GET("gallery/search")
    suspend fun getSearchGalleryImages(
        @Query("q") search : String,
    ) : Response<FeedResponse>


}
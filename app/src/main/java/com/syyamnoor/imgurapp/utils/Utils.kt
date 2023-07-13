package com.syyamnoor.imgurapp.utils

import com.syyamnoor.imgurapp.domain.models.Image
import kotlin.random.Random


object Utils {

    fun createRandomImage(): Image
    {
        val titles = listOf(
            "Syyam", "Another Title", "My Title", "Whose Title",
            "Another Title", "Get Your title", "Be for Titles", "I have no ideas"
        )
        val descriptions =
            titles.map { "$it abstract" }.shuffled()
        val topic = listOf(
            "arts", "automobiles", "books", "business", "fashion", "food",
            "health", "insider", "magazine"
        ).shuffled()
        val url = "https://imgur.com/4B06Bhr"
        

        return Image(
            "1231",
            titles[Random.nextInt(titles.size)],
            descriptions[Random.nextInt(descriptions.size)],
            url,
            false,
            topic[Random.nextInt(topic.size)],
            url
        )
    }
}
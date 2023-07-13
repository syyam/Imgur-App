package com.syyamnoor.imgurapp.utils

import kotlinx.coroutines.CoroutineDispatcher

interface AppDispatchers {
    fun main(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
}
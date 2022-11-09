package com.example.shadowproject.util

import com.example.shadowproject.BuildConfig


object Constants {
    const val BASE_URL = "https://dapi.kakao.com/"
    const val API_KEY = BuildConfig.bookApiKey
    const val SEARCH_BOOKS_TIME_DELAY = 100L
    const val DATASTORE_NAME = "preferences_datastore"
    const val PAGING_SIZE = 15 // 한번에 가져올 Page수
}
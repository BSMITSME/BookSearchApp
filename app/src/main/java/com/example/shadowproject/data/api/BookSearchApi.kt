package com.example.shadowproject.data.api

import com.example.shadowproject.data.model.SearchResponse
import com.example.shadowproject.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

//retrofit은 http api의 request를 인터페이스로 정의해서 사용
interface BookSearchApi {
    @Headers("Authorization: KakaoAK $API_KEY")
    @GET("v3/search/book")
    suspend fun searchBooks(
        @Query("query") query : String,
        @Query("sort") sort : String,
        @Query("page") page : Int,
        @Query("size") size : Int
    ) : Response<SearchResponse>
}
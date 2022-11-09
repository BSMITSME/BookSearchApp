package com.example.shadowproject.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.shadowproject.data.model.Book
import com.example.shadowproject.data.model.SearchResponse
import retrofit2.Response
import kotlinx.coroutines.flow.Flow

interface BookSearchRepository {
     suspend fun searchBooks(
         query : String,
         sort : String,
         page : Int,
         size : Int
     ):Response<SearchResponse>
    //Room
    suspend fun insertBooks(book:Book)

    suspend fun deleteBooks(book:Book)

    fun getFavoriteBooks() : Flow<List<Book>>

    //DataStore
    suspend fun saveSortMode(mode : String)
    suspend fun getSortMode(): Flow<String>

    //Paging
    fun getFavoritePagingBooks(): Flow<PagingData<Book>>
}


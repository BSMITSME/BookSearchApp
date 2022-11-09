package com.example.shadowproject.repository

import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import androidx.room.Database
import com.example.shadowproject.data.api.RetrofitInstance.api
import com.example.shadowproject.data.db.BookSearchDatabase
import com.example.shadowproject.data.model.Book
import com.example.shadowproject.data.model.SearchResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.security.PrivateKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.shadowproject.repository.BookSearchRepositoryImpl.PreferencesKeys.SORT_MODE
import com.example.shadowproject.util.Constants.PAGING_SIZE
import com.example.shadowproject.util.Sort
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class BookSearchRepositoryImpl(
    private val db : BookSearchDatabase,
    private val dataStore : DataStore<Preferences>

) : BookSearchRepository {
    override suspend fun searchBooks(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): Response<SearchResponse> {
        return api.searchBooks(query, sort, page, size)
    }

    override suspend fun insertBooks(book: Book) {
        db.bookSearchDao().insertBook(book)
    }

    override suspend fun deleteBooks(book: Book) {
        db.bookSearchDao().deleteBook(book)
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return db.bookSearchDao().getFavoriteBooks()
    }

    //DataStore
    //저장 및 불러오기에 사용할 키를 설정 PreferencesKey(타입 안정을 위해) -> 저장할 데이터 타입이 String
    private object PreferencesKeys{
        val SORT_MODE = stringPreferencesKey("sort_made")
    }
    //저장하는 작업은 코루틴안에서 동작해야 하기 때문에 suspend
    override suspend fun saveSortMode(mode: String) {
        dataStore.edit { prefs ->
            prefs[SORT_MODE] = mode //전달받은 mode 값을 edit블록에서 저장
        }
    }


    override suspend fun getSortMode(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if(exception is IOException){
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw  exception
                }
            }
            .map { prefs ->
                prefs[SORT_MODE] ?: Sort.ACCURACY.value //기본값은 accuracy
            }
    }

    override fun getFavoritePagingBooks(): Flow<PagingData<Book>> {
        val pagingSourceFactory = {db.bookSearchDao().getFavoritePagingBooks()}
        return Pager(
            config = PagingConfig(
                pageSize = PAGING_SIZE,
                enablePlaceholders = false,
                maxSize = PAGING_SIZE * 3
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}

package com.example.shadowproject.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.shadowproject.data.model.Book
import com.example.shadowproject.data.model.SearchResponse
import com.example.shadowproject.repository.BookSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BookSearchViewModel(
    private val bookSearchRepository : BookSearchRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    //API
    private val _searchResult = MutableLiveData<SearchResponse>()
    val searchResult : LiveData<SearchResponse> get() = _searchResult

    fun searchBooks(query: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = bookSearchRepository.searchBooks(query, getSortMode(), 1, 15)
        if(response.isSuccessful){
            response.body()?.let {
                _searchResult.postValue(it)
            }
        }
    }

    //Room
    fun saveBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        bookSearchRepository.insertBooks(book)
    }

    fun deleteBook(book:Book) = viewModelScope.launch(Dispatchers.IO) {
        bookSearchRepository.deleteBooks(book)
    }

    //val favoriteBooks : Flow<List<Book>> = bookSearchRepository.getFavoriteBooks()
    val favoriteBooks : StateFlow<List<Book>> = bookSearchRepository.getFavoriteBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())


    //SavedState
    var query = String()
        set(value){
            field = value
            savedStateHandle.set(SAVE_STATE_KEY, value)
        }
    init {
        query = savedStateHandle.get<String>(SAVE_STATE_KEY) ?: ""
    }
    companion object{
        private const val SAVE_STATE_KEY ="query"
    }

    //DataStore
    fun saveSortMode(value : String) = viewModelScope.launch(Dispatchers.IO){
        bookSearchRepository.saveSortMode(value)
    }

    suspend fun getSortMode() = withContext(Dispatchers.IO){ //반드시 값을 반환하고 종료하는 withContext활용
       bookSearchRepository.getSortMode().first()
    }

    //paging
    val favoritePagingBooks : StateFlow<PagingData<Book>> =
        bookSearchRepository.getFavoritePagingBooks()
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())
}
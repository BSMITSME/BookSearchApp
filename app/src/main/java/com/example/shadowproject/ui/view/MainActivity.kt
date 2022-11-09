package com.example.shadowproject.ui.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.shadowproject.R
import com.example.shadowproject.data.db.BookSearchDatabase
import com.example.shadowproject.databinding.ActivityMainBinding
import com.example.shadowproject.repository.BookSearchRepository
import com.example.shadowproject.repository.BookSearchRepositoryImpl
import com.example.shadowproject.ui.viewmodel.BookSearchViewModel
import com.example.shadowproject.ui.viewmodel.BookSearchViewModelProviderFactory
import com.example.shadowproject.util.Constants.DATASTORE_NAME

class MainActivity : AppCompatActivity() {
    val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var bookSearchViewModel : BookSearchViewModel
    private lateinit var navController : NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

    private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)//dataStore의 싱글톤 객체 만들기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navigation()

        val database = BookSearchDatabase.getInstance(this)
        val bookSearchRepository = BookSearchRepositoryImpl(database,dataStore)
        val factory = BookSearchViewModelProviderFactory(bookSearchRepository,this)
        bookSearchViewModel = ViewModelProvider(this, factory)[BookSearchViewModel::class.java]

    }

    fun navigation(){
        val host = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHost
        navController = host.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }
}
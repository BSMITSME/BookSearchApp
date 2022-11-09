package com.example.shadowproject.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.shadowproject.R
import com.example.shadowproject.databinding.FragmentFavoriteBinding
import com.example.shadowproject.databinding.FragmentSettingBinding
import com.example.shadowproject.ui.viewmodel.BookSearchViewModel
import com.example.shadowproject.util.Sort
import kotlinx.coroutines.launch

class SettingFragment : Fragment(){
    private var _binding : FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookSearchViewModel: BookSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookSearchViewModel = (activity as MainActivity).bookSearchViewModel

        saveSettings()
        loadSettings()

    }
    private fun saveSettings(){
        binding.rgSort.setOnCheckedChangeListener{ _,checkedId ->
            val value = when(checkedId){
                R.id.rb_accuracy -> Sort.ACCURACY.value
                R.id.rb_latest -> Sort.LATEST.value
                else -> return@setOnCheckedChangeListener
            }
            bookSearchViewModel.saveSortMode(value)
        }
    }

    private fun loadSettings(){
        lifecycleScope.launch{
            val buttonId = when(bookSearchViewModel.getSortMode()){
                Sort.ACCURACY.value -> R.id.rb_accuracy
                Sort.LATEST.value -> R.id.rb_latest
                else -> return@launch
            }
            binding.rgSort.check(buttonId)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
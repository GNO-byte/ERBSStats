package com.gno.erbs.erbs.stats.ui.guide.characters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gno.erbs.erbs.stats.model.erbs.characters.Character
import com.gno.erbs.erbs.stats.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharactersViewModel (
    private val dataRepository: DataRepository
        ): ViewModel() {

    val charactersLiveData = MutableLiveData<List<Character>>()

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        viewModelScope.launch(Dispatchers.IO) {
            charactersLiveData.postValue(
                dataRepository.getCharacters()?.filter { it.iconWebLink != null })
        }

    }

    class CharactersViewModelFactory @Inject constructor(
        private val dataRepository: DataRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == CharactersViewModel::class.java)
            return CharactersViewModel(dataRepository) as T
        }
    }
}

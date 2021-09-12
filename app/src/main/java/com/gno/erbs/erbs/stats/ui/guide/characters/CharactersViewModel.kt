package com.gno.erbs.erbs.stats.ui.guide.characters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gno.erbs.erbs.stats.model.erbs.characters.Character
import com.gno.erbs.erbs.stats.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharactersViewModel : ViewModel() {

    val charactersLiveData = MutableLiveData<List<Character>>()

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        viewModelScope.launch(Dispatchers.IO) {
            charactersLiveData.postValue(
                DataRepository.getCharacters()?.filter { it.iconWebLink != null })
        }

    }
}

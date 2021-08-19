package com.gno.erbs.erbs.stats.ui.guide.characters

import androidx.lifecycle.MutableLiveData
import com.gno.erbs.erbs.stats.model.erbs.characters.Character
import com.gno.erbs.erbs.stats.repository.DataRepository
import com.gno.erbs.erbs.stats.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class CharactersViewModel : BaseViewModel() {

    val charactersLiveData = MutableLiveData<List<Character>>()

    init {
        scope.launch {
            charactersLiveData.postValue(DataRepository.getCharacters())
        }
    }


}

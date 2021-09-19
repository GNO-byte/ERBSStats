package com.gno.erbs.erbs.stats.ui.guide.characterdetail

import androidx.lifecycle.*
import com.gno.erbs.erbs.stats.model.CharacterStats
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreCharacter
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreSkill
import com.gno.erbs.erbs.stats.model.erbs.characters.WeaponType
import com.gno.erbs.erbs.stats.repository.DataRepository
import com.gno.erbs.erbs.stats.ui.userstats.UserStatsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    private val code: Int,
    private val dataRepository: DataRepository
) : ViewModel() {

    val coreCharacterLiveData = MutableLiveData<CoreCharacter?>()
    val characterStatsLiveData = MutableLiveData<CharacterStats?>()
    private val weaponTypesLiveData = MutableLiveData<List<WeaponType>?>()
    val resultWeaponTypesLiveData = weaponTypesLiveData.combineWith(coreCharacterLiveData)
    { weaponTypes, coreCharacter ->
        weaponTypes?.forEach { weaponType ->
            weaponType.name = coreCharacter?.weapons?.find { it.id == weaponType.mastery }?.name
        }
        weaponTypes
    }
    val skillsLiveData = MutableLiveData<List<CoreSkill>?>()
    private var downloadCompleted = false

    fun <T, K, R> LiveData<T>.combineWith(
        liveData: LiveData<K>,
        block: (T?, K?) -> R
    ): LiveData<R> {
        val result = MediatorLiveData<R>()
        result.addSource(this) {
            result.value = block(this.value, liveData.value)
        }
        result.addSource(liveData) {
            result.value = block(this.value, liveData.value)
        }
        return result
    }
     init {
         loadCharacterDetail()
     }

    fun loadCharacterDetail() {

        if (!downloadCompleted) {
            viewModelScope.launch(Dispatchers.IO) {
                loadCoreCharacter(code)
                loadCharacterStats(code)
                loadWeaponTypes(code)
                loadSkills(code)
                downloadCompleted = true
            }
        }
    }

    private fun loadCoreCharacter(code: Int) {
        coreCharacterLiveData.postValue(dataRepository.getCoreCharacter(code))
    }

    private suspend fun loadCharacterStats(code: Int) {
        val characterAsync =
            viewModelScope.async(Dispatchers.IO) { dataRepository.getCharacter(code) }
        val characterLevelUpStatAsync =
            viewModelScope.async(Dispatchers.IO) { dataRepository.getCharacterLevelUpStat(code) }
        val character = characterAsync.await()
        val characterLevelUpStat = characterLevelUpStatAsync.await()

        if (character != null && characterLevelUpStat != null) {

            val characterStats = CharacterStats(character, characterLevelUpStat)?.also {
                dataRepository.addCharacterHalfWebLink(it)
            }
            characterStatsLiveData.postValue(characterStats)
        } else {
            characterStatsLiveData.postValue(null)

        }
    }

    private suspend fun loadWeaponTypes(code: Int) {
        weaponTypesLiveData.postValue(dataRepository.getCharacterWeaponTypes(code))
    }

    private suspend fun loadSkills(code: Int) {
        skillsLiveData.postValue(dataRepository.getSkills(code))
    }

    class CharacterDetailViewModelFactory @AssistedInject constructor(
        @Assisted("code") private val code: Int,
        private val dataRepository: DataRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == CharacterDetailViewModel::class.java)
            return CharacterDetailViewModel(code, dataRepository) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(
                @Assisted("code") code: Int
            ): CharacterDetailViewModelFactory
        }
    }

}
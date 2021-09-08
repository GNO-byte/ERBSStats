package com.gno.erbs.erbs.stats.ui.guide.characterdetail

import androidx.lifecycle.*
import com.gno.erbs.erbs.stats.model.CharacterStats
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreCharacter
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreSkill
import com.gno.erbs.erbs.stats.model.erbs.characters.WeaponType
import com.gno.erbs.erbs.stats.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CharacterDetailViewModel : ViewModel() {

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

    fun loadCharacterDetail(code: Int) {

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
        coreCharacterLiveData.postValue(DataRepository.getCoreCharacter(code))
    }

    private suspend fun loadCharacterStats(code: Int) {
        val characterAsync =
            viewModelScope.async(Dispatchers.IO) { DataRepository.getCharacter(code) }
        val characterLevelUpStatAsync =
            viewModelScope.async(Dispatchers.IO) { DataRepository.getCharacterLevelUpStat(code) }
        val character = characterAsync.await()
        val characterLevelUpStat = characterLevelUpStatAsync.await()

        if (character != null && characterLevelUpStat != null) {
            val characterStats = CharacterStats(character, characterLevelUpStat)
            DataRepository.addCharacterHalfWebLink(characterStats)
            characterStatsLiveData.postValue(characterStats)
        } else {
            characterStatsLiveData.postValue(null)

        }
    }

    private suspend fun loadWeaponTypes(code: Int) {
        weaponTypesLiveData.postValue(DataRepository.getCharacterWeaponTypes(code))
    }

    private suspend fun loadSkills(code: Int) {
        skillsLiveData.postValue(DataRepository.getSkills(code))
    }

}
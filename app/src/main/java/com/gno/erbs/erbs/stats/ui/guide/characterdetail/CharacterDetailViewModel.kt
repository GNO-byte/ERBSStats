package com.gno.erbs.erbs.stats.ui.guide.characterdetail

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.gno.erbs.erbs.stats.model.CharacterStats
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreCharacter
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreSkill
import com.gno.erbs.erbs.stats.model.erbs.characters.Character
import com.gno.erbs.erbs.stats.model.erbs.characters.WeaponType
import com.gno.erbs.erbs.stats.repository.DataRepository
import com.gno.erbs.erbs.stats.ui.base.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CharacterDetailViewModel : BaseViewModel() {

    val coreCharacterLiveData = MutableLiveData<CoreCharacter?>()
    val characterStatsLiveData = MutableLiveData<CharacterStats>()
    private val weaponTypesLiveData = MutableLiveData<List<WeaponType>?>()
    val resultWeaponTypesLiveData = weaponTypesLiveData.combineWith(coreCharacterLiveData)
    { weaponTypes, coreCharacter ->
        weaponTypes?.forEach { weaponType ->
            weaponType.name = coreCharacter?.weapons?.find { it.id == weaponType.mastery }?.name
        }
        weaponTypes
    }
    val skillsLiveData = MutableLiveData<List<CoreSkill>?>()


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

        scope.launch {
            loadCoreCharacter(code)
            loadCharacterStats(code)
            loadWeaponTypes(code)
            loadSkills(code)
        }
    }

    private fun loadCoreCharacter(code: Int) {
        coreCharacterLiveData.postValue(DataRepository.getCoreCharacter(code))
    }

    private suspend fun loadCharacterStats(code: Int) {
        val characterAsync = scope.async { DataRepository.getCharacter(code) }
        val characterLevelUpStatAsync = scope.async { DataRepository.getCharacterLevelUpStat(code) }
        val character = characterAsync.await()
        val characterLevelUpStat = characterLevelUpStatAsync.await()

        if (character != null && characterLevelUpStat != null) {
            val characterStats = CharacterStats(character, characterLevelUpStat)
            DataRepository.addCharacterHalfWebLink(characterStats)
            characterStatsLiveData.postValue(characterStats)
        }
    }

    private suspend fun loadWeaponTypes(code: Int) {
        weaponTypesLiveData.postValue(DataRepository.getCharacterWeaponTypes(code))
    }

    private suspend fun loadSkills(code: Int) {
        skillsLiveData.postValue(DataRepository.getSkills(code))
    }

}
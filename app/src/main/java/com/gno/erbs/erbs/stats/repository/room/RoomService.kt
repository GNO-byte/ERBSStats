package com.gno.erbs.erbs.stats.repository.room

import android.content.Context
import android.os.Bundle
import com.gno.erbs.erbs.stats.repository.AsyncInitialized
import com.gno.erbs.erbs.stats.repository.FirebaseService
import com.gno.erbs.erbs.stats.repository.room.cache.RoomCache
import com.gno.erbs.erbs.stats.repository.room.character.RoomCharacter
import com.gno.erbs.erbs.stats.repository.room.corecharacter.RoomCoreCharacter
import com.gno.erbs.erbs.stats.repository.room.history.RoomHistory
import com.gno.erbs.erbs.stats.repository.room.update.RoomUpdate
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

class RoomService private constructor(
    private val db: AppDatabase
) : AsyncInitialized {

    override suspend fun initData(context: Context) {
        db.roomHistoryDao().let {
            if (it.getRowCount() >= 100) it.delete95lastHistories()
        }
    }

    fun getCurrentDate(): Date = Calendar.getInstance().time

    fun addCharacters(roomCharacters: List<RoomCharacter>) {
        db.roomCharacterDao().insertCharacters(roomCharacters)
    }

    fun getCharacters() = try {
        db.roomCharacterDao().getCharacters()
    } catch (e: Exception) {
        Timber.e(e)
        null
    }

    fun addCoreCharacters(roomCoreCharacters: List<RoomCoreCharacter>) {
        db.roomCoreCharacterDao().let { roomCoreCharacterDao ->
            roomCoreCharacters.forEach {
                roomCoreCharacterDao.insertCoreCharacterFull(it)
            }
        }
    }

    fun getCoreCharacters(): List<RoomCoreCharacter> {
        val coreCharacters = db.roomCoreCharacterDao().getCoreCharactersSkillWeapon()

        return coreCharacters.map {
            it.character.apply {
                skills = it.skills
                weapons = it.weapons
            }
        }
    }

    fun addUpdate(roomUpdate: RoomUpdate) {
        db.roomUpdateDao().insertTableUpdate(roomUpdate)
    }

    fun getUpdate() = db.roomUpdateDao().getTableUpdate()

    fun addCache(cache: RoomCache) {
        db.roomCacheDao().insertCache(cache)
    }

    fun getcache(name: String) = db.roomCacheDao().getCache(name)

    fun addHistory(id: Int, bundle: Bundle?) {
        db.roomHistoryDao().insertHistory(RoomHistory(null, getCurrentDate(), bundle, id))
    }

    fun getLast5Histories(): Flow<List<RoomHistory>?>? {
        return db.roomHistoryDao().getLast5Histories()
    }

    class Builder @Inject constructor(
        private val db: AppDatabase
    ) {

        suspend fun build(context: Context) = RoomService(db).apply {
            initData(context)
        }
    }

}
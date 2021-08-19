package com.gno.erbs.erbs.stats.repository.room

import android.content.Context
import android.os.Build
import com.gno.erbs.erbs.stats.repository.room.cache.RoomCache
import com.gno.erbs.erbs.stats.repository.room.cache.RoomCacheDao
import com.gno.erbs.erbs.stats.repository.room.character.RoomCharacter
import com.gno.erbs.erbs.stats.repository.room.character.RoomCharacterDao
import com.gno.erbs.erbs.stats.repository.room.corecharacter.RoomCoreCharacter
import com.gno.erbs.erbs.stats.repository.room.corecharacter.RoomCoreCharacterDao
import com.gno.erbs.erbs.stats.repository.room.update.RoomUpdate
import com.gno.erbs.erbs.stats.repository.room.update.RoomUpdateDao
import java.text.SimpleDateFormat
import java.util.*

object RoomService {

    private var locale: Locale? = null
    private var db: AppDatabase? = null
    private var roomCharacterDao: RoomCharacterDao? = null
    private var roomCoreCharacterDao: RoomCoreCharacterDao? = null
    private var roomUpdateDao: RoomUpdateDao? = null
    private var roomCacheDao: RoomCacheDao? = null

    operator fun invoke(context: Context): RoomService {
        db = AppDatabase.getAppDataBase(context)

        roomCharacterDao = db?.roomCharacterDao()
        roomCoreCharacterDao = db?.roomCoreCharacterDao()
        roomUpdateDao = db?.roomUpdateDao()
        roomCacheDao = db?.roomCacheDao()

        locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            context.resources.configuration.locales.get(0)
        else context.resources.configuration.locale

        return this
    }

    fun getCurrentDate(): Date = Calendar.getInstance().time

    fun getIso8601Format(): SimpleDateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ssZ", locale
    )

    fun addCharacters(RoomCharacters: List<RoomCharacter>) {
        roomCharacterDao?.let { thisRoomCharacterDao ->
            RoomCharacters.forEach { roomCharacter ->
                thisRoomCharacterDao.insertCharacter(roomCharacter)
            }
        }
    }

    fun getCharacters(): List<RoomCharacter>? {
        return roomCharacterDao?.getCharacters()
    }

    fun addCoreCharacters(roomCoreCharacters: List<RoomCoreCharacter>) {
        roomCoreCharacterDao?.let { thisRoomCoreCharacter ->
            roomCoreCharacters.forEach {
                thisRoomCoreCharacter.insertCoreCharacterFull(it)
            }
        }
    }

    fun getCoreCharacters(): List<RoomCoreCharacter>? {
        val coreCharacters = roomCoreCharacterDao?.getCoreCharactersSkillWeapon()

        return coreCharacters?.map {
            it.character.apply {
                skills = it.skills
                weapons = it.weapons
            }
        }
    }

    fun addUpdate(roomUpdate: RoomUpdate) {
        roomUpdateDao?.insertTableUpdate(roomUpdate)
    }

    fun getUpdate() = roomUpdateDao?.getTableUpdate()

    fun addcache(cache: RoomCache) {
        roomCacheDao?.insertCache(cache)
    }


    fun getcache(name: String) = roomCacheDao?.getCache(name)

}
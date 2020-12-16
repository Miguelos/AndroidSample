package me.miguelos.sample.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import me.miguelos.sample.data.source.local.entity.MarvelCharacterDBEntity

/**
 * The Room Database that contains Marvel Character table.
 */
@Database(entities = [MarvelCharacterDBEntity::class], version = 1)
abstract class MarvelDatabase : RoomDatabase() {

    abstract fun marvelCharacterDao(): MarvelCharacterDao
}

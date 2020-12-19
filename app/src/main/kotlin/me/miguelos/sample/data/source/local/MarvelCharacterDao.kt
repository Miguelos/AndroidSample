package me.miguelos.sample.data.source.local

import androidx.room.*
import me.miguelos.sample.data.source.local.entity.MarvelCharacterDBEntity
import me.miguelos.sample.data.source.local.entity.MarvelCharacterDBEntity.Companion.TABLE_NAME


/**
 * Data Access Object for the marvel characters table.
 */
@Dao
interface MarvelCharacterDao {

    /**
     * Select all Marvel characters from the MarvelCharacters table.
     *
     * @return all Marvel characters.
     */
    @Query("SELECT * FROM $TABLE_NAME WHERE name LIKE :search")
    fun getMarvelCharacters(search: String?): List<MarvelCharacterDBEntity>

    /**
     * Select a Marvel character by id.
     *
     * @param marvelCharacterId the Marvel character id.
     * @return the Marvel character with marvelCharacterId.
     */
    @Query("SELECT * FROM $TABLE_NAME WHERE entryid = :marvelCharacterId")
    fun getMarvelCharacterById(marvelCharacterId: Long): MarvelCharacterDBEntity?

    /**
     * Insert a Marvel character in the database. If the Marvel character already exists,
     * replace it.
     *
     * @param marvelCharacterEntity the Marvel character to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMarvelCharacter(marvelCharacterEntity: MarvelCharacterDBEntity)

    /**
     * Update a Marvel character.
     *
     * @param marvelCharacterEntity Marvel character to be updated
     * @return the number of Marvel Characters updated. This should always be 1.
     */
    @Update
    fun updateMarvelCharacter(marvelCharacterEntity: MarvelCharacterDBEntity): Int

    /**
     * Delete a Marvel character by id.
     *
     * @return the number of Marvel characters deleted. This should always be 1.
     */
    @Query("DELETE FROM $TABLE_NAME WHERE entryid = :marvelCharacterId")
    fun deleteMarvelCharacterById(marvelCharacterId: String): Int

    /**
     * Delete all Marvel Characters.
     */
    @Query("DELETE FROM $TABLE_NAME")
    fun deleteMarvelCharacters()
}

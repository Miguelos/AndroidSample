package me.miguelos.sample.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.miguelos.sample.data.source.local.entity.MarvelCharacterDBEntity.Companion.TABLE_NAME


/**
 * Immutable model class for a Task.
 *
 * @param id          The unique ID of the character resource
 * @param name        The name of the character
 * @param description A short bio or description of the character
 * @param thumbnail   The representative image for this character
 */
@Entity(tableName = TABLE_NAME)
data class MarvelCharacterDBEntity constructor(
    @PrimaryKey @ColumnInfo(name = COL_ID) var id: Long,
    @ColumnInfo(name = COL_NAME) var name: String,
    @ColumnInfo(name = COL_DESC) var description: String,
    @ColumnInfo(name = COL_THUMB) var thumbnail: String,
    @ColumnInfo(name = COL_COMICS) var comics: Int
) {

    val titleForList: String
        get() = if (name.isNotEmpty()) name else description

    val isEmpty
        get() = name.isEmpty() && description.isEmpty()

    companion object {
        const val TABLE_NAME = "marvelCharacter"
        const val COL_ID = "entryid"
        const val COL_NAME = "name"
        const val COL_DESC = "description"
        const val COL_THUMB = "thumbnail"
        const val COL_COMICS = "availableComics"
    }
}

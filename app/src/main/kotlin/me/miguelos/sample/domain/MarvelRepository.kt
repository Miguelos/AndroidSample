package me.miguelos.sample.domain

import io.reactivex.rxjava3.core.Single
import me.miguelos.sample.domain.usecase.GetCharacter
import me.miguelos.sample.domain.usecase.GetCharacters
import me.miguelos.sample.domain.usecase.SaveCharacters

/**
 * Main entry point for accessing Marvel data.
 */
interface MarvelRepository {

    fun getMarvelCharacters(
        requestValues: GetCharacters.RequestValues
    ): Single<GetCharacters.ResponseValues?>

    fun getMarvelCharacter(
        requestValues: GetCharacter.RequestValues
    ): Single<GetCharacter.ResponseValues?>

    fun deleteAllMarvelCharacters()

    fun saveMarvelCharacters(
        requestValues: SaveCharacters.RequestValues
    )
}

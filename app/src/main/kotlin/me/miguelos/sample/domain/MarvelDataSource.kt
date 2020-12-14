package me.miguelos.sample.domain

import io.reactivex.rxjava3.core.Single
import me.miguelos.sample.domain.usecase.GetCharacter
import me.miguelos.sample.domain.usecase.GetCharacters

/**
 * Main entry point for accessing Marvel data.
 */
interface MarvelDataSource {

    fun getMarvelCharacters(requestValues: GetCharacters.RequestValues): Single<GetCharacters.ResponseValues?>

    fun getMarvelCharacter(requestValues: GetCharacter.RequestValues): Single<GetCharacter.ResponseValues?>

    //fun getMarvelCharacterDetail(marvelCharacterDetailId: Long): Single<CharactersResponse>

    fun refreshMarvelCharacters()

    fun deleteAllMarvelCharacters()

    //fun saveMarvelCharacter(requestValues: SaveCharacter.RequestValues)
}

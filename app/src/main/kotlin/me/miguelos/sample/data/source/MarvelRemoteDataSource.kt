package me.miguelos.sample.data.source

import io.reactivex.rxjava3.core.Single
import me.miguelos.sample.domain.usecase.GetCharacter
import me.miguelos.sample.domain.usecase.GetCharacters

/**
 * Main entry point for accessing Marvel data.
 */
interface MarvelRemoteDataSource {

    fun getMarvelCharacters(
        requestValues: GetCharacters.RequestValues
    ): Single<GetCharacters.ResponseValues?>

    fun getMarvelCharacter(
        requestValues: GetCharacter.RequestValues
    ): Single<GetCharacter.ResponseValues?>
}

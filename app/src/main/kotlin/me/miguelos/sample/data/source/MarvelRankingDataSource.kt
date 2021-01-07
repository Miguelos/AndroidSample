package me.miguelos.sample.data.source

import io.reactivex.rxjava3.core.Single
import me.miguelos.sample.domain.usecase.GetCharacter
import me.miguelos.sample.domain.usecase.GetCharacters
import me.miguelos.sample.domain.usecase.SaveCharacters

/**
 * Entry point for accessing Marvel Ranking data.
 */
interface MarvelRankingDataSource {

    fun getRankedMarvelCharacters(
        requestValues: GetCharacters.RequestValues
    ): Single<GetCharacters.ResponseValues?>

    fun getRankedMarvelCharacter(
        requestValues: GetCharacter.RequestValues
    ): Single<GetCharacter.ResponseValues?>

    fun saveMarvelCharacters(
        requestValues: SaveCharacters.RequestValues
    )

    fun deleteRankedMarvelCharacters()
}

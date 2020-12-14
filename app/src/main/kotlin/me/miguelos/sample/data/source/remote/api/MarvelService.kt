package me.miguelos.sample.data.source.remote.api

import io.reactivex.rxjava3.core.Single
import me.miguelos.sample.data.source.remote.api.responses.CharactersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * REST API access points
 */
interface MarvelService {

    @GET("characters/{$ID}")
    fun getCharacter(@Path(ID) id: Long): Single<CharactersResponse>

    @GET("characters")
    fun getCharacters(
        @Query(OFFSET) offset: Int? = null,
        @Query(LIMIT) limit: Int? = null
    ): Single<CharactersResponse>

    companion object Params {
        const val API_KEY = "apikey"
        const val HASH = "hash"
        const val TIMESTAMP = "ts"
        const val ID = "id"
        const val OFFSET = "offset"
        const val LIMIT = "limit"
    }
}

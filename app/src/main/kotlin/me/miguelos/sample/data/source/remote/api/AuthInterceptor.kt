package me.miguelos.sample.data.source.remote.api

import me.miguelos.sample.data.source.remote.api.MarvelService.Params.API_KEY
import me.miguelos.sample.data.source.remote.api.MarvelService.Params.HASH
import me.miguelos.sample.data.source.remote.api.MarvelService.Params.TIMESTAMP
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.math.BigInteger
import java.security.MessageDigest


class AuthInterceptor(
    private val publicKey: String,
    private val privateKey: String,
    private val timeStamp: Long = System.currentTimeMillis(),
    private val messageDigest: MessageDigest = MessageDigest.getInstance("MD5")
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().authorize())
    }

    private fun Request.authorize(): Request = this.newBuilder()
        .url(this.createAuthorizedUrl())
        .build()

    private fun Request.createAuthorizedUrl(): HttpUrl = this.url
        .newBuilder()
        .addQueryParameter(API_KEY, publicKey)
        .addQueryParameter(HASH, generateMd5Hash())
        .addQueryParameter(TIMESTAMP, timeStamp.toString())
        .build()

    private fun generateMd5Hash(): String {
        val input = timeStamp.toString() + privateKey + publicKey
        return BigInteger(
            SIG_NUM,
            messageDigest.digest(input.toByteArray())
        ).toString(RADIX).padStart(LENGTH, '0')
    }

    companion object {
        private const val SIG_NUM = 1
        private const val RADIX = 16
        private const val LENGTH = 32
    }
}

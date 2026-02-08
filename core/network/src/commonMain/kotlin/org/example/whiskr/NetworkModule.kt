package org.example.whiskr

import com.liftric.kvault.KVault
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.di.Singleton
import org.example.whiskr.dto.AuthResponseDto
import org.example.whiskr.dto.RefreshTokenRequestDto
import org.example.whiskr.util.BASE_URL
import co.touchlab.kermit.Logger as KermitLogger

interface NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        json: Json,
        tokenStorage: TokenStorage
    ): HttpClient {
        return HttpClient {

            install(HttpTimeout) {
                requestTimeoutMillis = 300_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 300_000
            }

            install(ContentNegotiation) {
                json(json)
            }

            install(Logging) {
                level = LogLevel.INFO
                logger = object : Logger {
                    override fun log(message: String) {
                        KermitLogger.withTag("HTTP").d { message }
                    }
                }
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val access = tokenStorage.accessToken
                        val refreshToken = tokenStorage.refreshToken

                        if (access != null && refreshToken != null) {
                            BearerTokens(access, refreshToken)
                        } else {
                            null
                        }
                    }

                    refreshTokens {
                        val refreshToken = tokenStorage.refreshToken ?: return@refreshTokens null

                        try {
                            val refreshClient = HttpClient {
                                install(ContentNegotiation) { json(json) }

                                install(Logging) {
                                    level = LogLevel.BODY
                                    logger = object : Logger {
                                        override fun log(message: String) {
                                            KermitLogger.withTag("AuthRefresh").d { message }
                                        }
                                    }
                                }
                            }
                            val authResponse: AuthResponseDto =
                                refreshClient.post("${BASE_URL.removeSuffix("/")}/auth/refresh") {
                                    contentType(ContentType.Application.Json)
                                    setBody(body = RefreshTokenRequestDto(refreshToken))
                                }.body()

                            tokenStorage.accessToken = authResponse.accessToken
                            tokenStorage.refreshToken = authResponse.refreshToken

                            BearerTokens(
                                accessToken = tokenStorage.accessToken ?: "",
                                refreshToken = tokenStorage.refreshToken
                            )
                        } catch (e: Exception) {
                            KermitLogger.withTag("Auth").e(e) { "Token refresh failed" }
                            tokenStorage.clear()
                            null
                        }
                    }
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideKtorfit(httpClient: HttpClient): Ktorfit {
        return Ktorfit.Builder()
            .baseUrl(BASE_URL)
            .httpClient(httpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideTokenStorage(kVault: KVault): TokenStorage = TokenStorage(kVault)
}
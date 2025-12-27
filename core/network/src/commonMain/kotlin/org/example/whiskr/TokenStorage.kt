package org.example.whiskr

import com.liftric.kvault.KVault
import me.tatarka.inject.annotations.Inject

@Inject
class TokenStorage(private val kvault: KVault) {

    var accessToken: String?
        get() = kvault.string(ACCESS_TOKEN)
        set(value) {
            if (value != null) kvault.set(ACCESS_TOKEN, value)
            else kvault.deleteObject(ACCESS_TOKEN)
        }

    var refreshToken: String?
        get() = kvault.string(REFRESH_TOKEN)
        set(value) {
            if (value != null) kvault.set(REFRESH_TOKEN, value)
            else kvault.deleteObject(REFRESH_TOKEN)
        }

    fun clear() {
        kvault.clear()
    }

    companion object {
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
    }
}
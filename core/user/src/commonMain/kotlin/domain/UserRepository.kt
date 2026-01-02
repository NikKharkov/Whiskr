package domain

import com.arkivanov.decompose.value.Value

interface UserRepository {
    val user: Value<UserState>
    suspend fun getMyProfile()
}
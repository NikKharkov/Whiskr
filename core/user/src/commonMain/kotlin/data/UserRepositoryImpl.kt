package data

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import domain.UserRepository
import domain.UserState
import me.tatarka.inject.annotations.Inject

@Inject
class UserRepositoryImpl(
    private val userApiService: UserApiService
) : UserRepository {
    private val _user = MutableValue(UserState())
    override val user: Value<UserState> = _user

    override suspend fun getMyProfile() {
        try {
            _user.update { it.copy(isLoading = true) }
            val response = userApiService.getProfile()
            _user.value = UserState(profile = response, isLoading = false)
        } catch (e: Exception) {
            Logger.withTag("UserRepository").e(e) { "Failed to fetch user" }
            _user.update { it.copy(isLoading = false) }
        }
    }
}
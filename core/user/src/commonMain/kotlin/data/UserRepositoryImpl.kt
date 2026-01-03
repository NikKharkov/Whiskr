package data

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import domain.UserRepository
import domain.UserState
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.database.daos.UserDao
import util.toDto
import util.toEntity

@Inject
class UserRepositoryImpl(
    private val userApiService: UserApiService,
    private val userDao: UserDao
) : UserRepository {
    private val _user = MutableValue(UserState(isLoading = true))
    override val user: Value<UserState> = _user

    override suspend fun getMyProfile() {
        val cachedUser = userDao.fetchUser()

        if (cachedUser != null) {
            _user.value = UserState(
                profile = cachedUser.toDto(),
                isLoading = false
            )
        }

        try {
            val response = userApiService.getProfile()
            userDao.insertUser(response.toEntity())

            _user.value = UserState(profile = response, isLoading = false)
        } catch (e: Exception) {
            Logger.withTag("UserRepository").e(e) { "Failed to fetch user" }
            _user.update { it.copy(isLoading = false) }
        }
    }
}
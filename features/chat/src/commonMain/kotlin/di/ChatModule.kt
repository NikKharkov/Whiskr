package di

import com.arkivanov.decompose.ComponentContext
import component.detail.ChatDetailComponent
import component.detail.DefaultChatDetailComponent
import component.list.ChatListComponent
import component.list.DefaultChatListComponent
import data.ChatApiService
import data.ChatRepositoryImpl
import data.ChatSocketService
import data.createChatApiService
import de.jensklingenberg.ktorfit.Ktorfit
import domain.ChatRepository
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.TokenStorage
import org.example.whiskr.di.Singleton
import org.example.whiskr.dto.Media

interface ChatModule {

    @Provides
    @Singleton
    fun provideChatApiService(ktorfit: Ktorfit): ChatApiService = ktorfit.createChatApiService()

    @Provides
    @Singleton
    fun provideChatRepository(
        chatApiService: ChatApiService
    ): ChatRepository = ChatRepositoryImpl(chatApiService)

    @Provides
    @Singleton
    fun provideChatSocketService(
        json: Json,
        tokenStorage: TokenStorage
    ): ChatSocketService = ChatSocketService(json,tokenStorage)

    @Provides
    @Singleton
    fun provideChatDetailFactory(
        factory: (
            ComponentContext,
            Long, // initialChatId
            Long, // targetUserId
            () -> Unit,
            (String) -> Unit,
            (List<Media>, Int) -> Unit
        ) -> DefaultChatDetailComponent
    ): ChatDetailComponent.Factory {
        return ChatDetailComponent.Factory(factory)
    }

    @Provides
    @Singleton
    fun provideChatListFactory(
        factory: (
            ComponentContext,
            (Long) -> Unit // onNavigateToChat
        ) -> DefaultChatListComponent
    ): ChatListComponent.Factory {
        return ChatListComponent.Factory(factory)
    }
}
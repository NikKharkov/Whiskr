package data

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.TokenStorage
import org.example.whiskr.util.WS_BASE_URL
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.config.HeartBeat
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.convertAndSend
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.websocket.ktor.KtorWebSocketClient
import kotlin.time.Duration.Companion.seconds

@Inject
class ChatSocketService(
    private val json: Json,
    private val tokenStorage: TokenStorage
) {
    private val ktorClient = HttpClient {
        install(WebSockets)
    }

    private val client = StompClient(KtorWebSocketClient(ktorClient)) {
        connectionTimeout = 15.seconds
        heartBeat = HeartBeat(10.seconds, 10.seconds)
        autoReceipt = false
    }

    private var session: StompSessionWithKxSerialization? = null

    suspend fun connect() {
        if (session != null) return

        val token = tokenStorage.accessToken ?: throw IllegalStateException("No token")

        try {
            val headers = mapOf("Authorization" to "Bearer $token")

            session = client.connect(
                url = WS_BASE_URL,
                customStompConnectHeaders = headers
            ).withJsonConversions(json)

            Logger.d("Socket Connected")
        } catch (e: Exception) {
            Logger.e(e) { "Socket Error: ${e.message}" }
        }
    }

    suspend fun disconnect() {
        session?.disconnect()
        session = null
    }

    suspend fun observeMessages(chatId: Long): Flow<ChatMessageDto> {
        val s = session ?: throw IllegalStateException("Not connected")
        return s.subscribe<ChatMessageDto>("/topic/chat/$chatId")
    }

    suspend fun observeStates(chatId: Long): Flow<ChatStateEvent> {
        val s = session ?: throw IllegalStateException("Not connected")
        return s.subscribe<ChatStateEvent>("/topic/chat/$chatId/states")
    }

    suspend fun sendMessage(chatId: Long, request: SendMessageRequest) {
        val s = session ?: throw IllegalStateException("Not connected")

        s.convertAndSend(
            destination = "/app/chat/$chatId/send",
            body = request,
            serializer = SendMessageRequest.serializer()
        )
    }

    suspend fun sendTyping(chatId: Long, isTyping: Boolean) {
        val s = session ?: throw IllegalStateException("Not connected")

        s.convertAndSend(
            destination = "/app/chat/$chatId/typing",
            body = isTyping,
            serializer = Boolean.serializer()
        )
    }

    suspend fun sendMarkAsRead(chatId: Long) {
        val s = session ?: return
        try {
            s.convertAndSend(
                destination = "/app/chat/$chatId/read",
                body = "",
                serializer = String.serializer()
            )
        } catch (e: Exception) {
            Logger.e(e) { "Failed to send read receipt" }
        }
    }

    suspend fun observeChatStates(chatId: Long): Flow<ChatStateEvent> {
        val s = session ?: throw IllegalStateException("Not connected")
        return s.subscribe<ChatStateEvent>("/topic/chat/$chatId/states")
    }

    suspend fun observePrivateEvents(): Flow<ChatStateEvent> {
        val s = session ?: throw IllegalStateException("Not connected")
        return s.subscribe<ChatStateEvent>("/user/queue/messages")
    }
}
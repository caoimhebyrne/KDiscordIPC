package dev.cbyrne.example

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.event.DiscordEvent
import dev.cbyrne.kdiscordipc.core.event.impl.*
import dev.cbyrne.kdiscordipc.core.packet.outbound.impl.SetVoiceSettingsPacket
import dev.cbyrne.kdiscordipc.core.util.Platform
import dev.cbyrne.kdiscordipc.core.util.platform
import dev.cbyrne.kdiscordipc.data.activity.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.*
import okio.IOException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.util.*
import kotlin.time.Duration.Companion.seconds

val logger: Logger = LogManager.getLogger("Example")

suspend fun main() {
    val ipc = KDiscordIPC(clientID)//"945428344806183003")
    logger.info("Starting example!")

    ipc.on<ReadyEvent> {
        logger.info("Ready! (${data.user.username}#${data.user.discriminator})")

        // Set the user's activity (a.k.a. rich presence)
        ipc.activityManager.setActivity("Hello", "world") {
            largeImage("https://avatars.githubusercontent.com/u/71222289?v=4", "KDiscordIPC")
            smallImage("https://avatars.githubusercontent.com/u/71222289?v=4", "Testing")

            party(UUID.randomUUID().toString(), listOf(1, 2))
            secrets(UUID.randomUUID().toString())
//            button("Click me", "https://google.com")
            timestamps(System.currentTimeMillis(), System.currentTimeMillis() + 50000)
        }

        // Subscribe to some events
        ipc.subscribe(DiscordEvent.CurrentUserUpdate)
        ipc.subscribe(DiscordEvent.ActivityJoinRequest)
        ipc.subscribe(DiscordEvent.ActivityJoin)
        ipc.subscribe(DiscordEvent.ActivityInvite)
        ipc.subscribe(DiscordEvent.ActivitySpectate)

        // Get a specific user by ID
        val user = ipc.userManager.getUser("843135686173392946")
        logger.info("User by ID: $user")

        // Get the user's friend list
        val relationships = ipc.relationshipManager.getRelationships()
        logger.info("Relationships: ${relationships.size}")

        val dataFolder = File(if(platform == Platform.WINDOWS) "${System.getenv("APPDATA")}/KDiscordIPC" else "${System.getenv("HOME")}/.KDiscordIPC")
        val authFile = File("${dataFolder.absolutePath}/authentication.json")

        val okHttpClient = OkHttpClient()

        var accessToken: String? = null
        if (authFile.exists()) {
            val timelessResponse = Json.decodeFromString<TimelessOAuthResponse>(authFile.readText())

            if (timelessResponse.expiresOn < Clock.System.now()) {
                val refreshBody =
                    MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("client_id", clientID)
                        .addFormDataPart("client_secret", clientSecret).addFormDataPart("grant_type", "refresh_token")
                        .addFormDataPart("refresh_token", timelessResponse.refreshToken).build()

                val refreshRequest = Request.Builder().header("Content-Type", "application/x-www-form-urlencoded")
                    .url("https://discord.com/api/v8/oauth2/token").post(refreshBody).build()

                val refreshResponse = okHttpClient.newCall(refreshRequest).execute()
                if (!refreshResponse.isSuccessful) throw IOException("Unexpected code ${refreshResponse.code}")

                if (refreshResponse.body == null) throw Exception("error while getting access token")

                val oauthResponse = Json.decodeFromString<OAuthResponse>(refreshResponse.body!!.string())
                logger.info(oauthResponse)

                accessToken = oauthResponse.accessToken

                saveOAuthResponse(oauthResponse)
            } else {
                accessToken = timelessResponse.accessToken
            }
        }

        if (accessToken == null) {

            val authorization =
                ipc.applicationManager.authorize(arrayOf("rpc", "rpc.voice.read", "identify"), "971413122470531142")
            logger.info("Authorization: $authorization")

            val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("client_id", clientID)
                .addFormDataPart("client_secret", clientSecret)
                .addFormDataPart("grant_type", "authorization_code")
                .addFormDataPart("code", authorization.code)
                .addFormDataPart("redirect_uri", "http://127.0.0.1")
                .build()

            val request = Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .url("https://discord.com/api/v8/oauth2/token")
                .post(body)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            if (response.body == null) throw Exception("error while getting access token")

            val oauthResponse = Json.decodeFromString<OAuthResponse>(response.body!!.string())
            logger.info(oauthResponse)

            accessToken = oauthResponse.accessToken

            saveOAuthResponse(oauthResponse)
        }

        // Get an oauth token for the currently logged-in user
        val oauthToken = ipc.applicationManager.getOAuthToken(accessToken)
        logger.info("Received oauth token from Discord! Expires on: ${oauthToken.expires}")

        val voiceSettings = ipc.voiceSettingsManager.getVoiceSettings()
        logger.info("Voice Settings: $voiceSettings")

        ipc.voiceSettingsManager.setVoiceSettings(SetVoiceSettingsPacket.VoiceSettingArguments(mute = true))
        ipc.voiceSettingsManager.subscribeToVoiceSettingsUpdate()

    }

    ipc.on<ErrorEvent> {
        logger.error("IPC communication error (${data.code}): ${data.message}")
    }

    ipc.on<CurrentUserUpdateEvent> {
        logger.info("Current user updated!")
    }

    ipc.on<ActivityJoinEvent> {
        logger.info("The user has joined someone else's party! ${data.secret}")
    }

    ipc.on<ActivityInviteEvent> {
        logger.info("We have been invited to join ${data.user.username}'s party! (${data.activity.party.id})")

        ipc.activityManager.acceptInvite(data)
    }

    ipc.connect()
}

@Suppress("unused")
@Serializable
data class OAuthResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Int,
    @SerialName("refresh_token") val refreshToken: String,
    val scope: String
)

@Suppress("MemberVisibilityCanBePrivate", "unused")
@Serializable
class TimelessOAuthResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_on") val expiresOn: Instant,
    @SerialName("refresh_token") val refreshToken: String,
    val scope: String
) {
    constructor(response: OAuthResponse) : this(
        accessToken = response.accessToken,
        tokenType = response.tokenType,
        refreshToken = response.refreshToken,
        scope = response.scope,
        expiresOn = Clock.System.now() + response.expiresIn.seconds
    )
}

fun saveOAuthResponse(response: OAuthResponse) {
    val dir = File("${System.getenv("APPDATA")}/KDiscordIPC")
    if (!dir.exists()) dir.mkdir()
    File("${System.getenv("APPDATA")}/KDiscordIPC/authentication.json").writeText(
        Json.encodeToString(
            TimelessOAuthResponse.serializer(), TimelessOAuthResponse(response)
        )
    )
}
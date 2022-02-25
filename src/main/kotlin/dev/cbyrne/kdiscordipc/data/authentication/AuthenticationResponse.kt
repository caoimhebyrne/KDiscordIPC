package dev.cbyrne.kdiscordipc.data.authentication

import dev.cbyrne.kdiscordipc.data.application.Application
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationResponse(
    val application: Application,
    val scopes: List<String>,
    val expires: String,
    val user: User,
    @SerialName("access_token")
    val accessToken: String
) {
    @Serializable
    data class User(
        val id: String,
        val username: String,
        val avatar: String,
        val discriminator: String,
        @SerialName("public_flags")
        val publicFlags: Int
    )
}

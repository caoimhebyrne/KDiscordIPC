package dev.cbyrne.kdiscordipc.core.packet.inbound.impl

import dev.cbyrne.kdiscordipc.core.packet.inbound.CommandPacket
import dev.cbyrne.kdiscordipc.core.packet.inbound.InboundPacket
import dev.cbyrne.kdiscordipc.data.application.Application
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticatePacket(
    override val data: Data,
    override val cmd: String = "AUTHENTICATE",
    override val opcode: Int = 0x01,
    override val nonce: String = "0"
) : CommandPacket() {
    @Serializable
    data class Data(
        val application: Application,
        val scopes: List<String>,
        val expires: String,
        val user: User,
        @SerialName("access_token")
        val accessToken: String
    ) : InboundPacket.Data() {
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
}

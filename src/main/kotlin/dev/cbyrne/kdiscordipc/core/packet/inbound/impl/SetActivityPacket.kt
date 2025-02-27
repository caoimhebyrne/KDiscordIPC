package dev.cbyrne.kdiscordipc.core.packet.inbound.impl

import dev.cbyrne.kdiscordipc.core.packet.inbound.CommandPacket
import dev.cbyrne.kdiscordipc.core.packet.inbound.InboundPacket
import dev.cbyrne.kdiscordipc.data.activity.Activity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetActivityPacket(
    override val data: Data?,
    override val cmd: String = "SET_ACTIVITY",
    override val opcode: Int = 0x01,
    override val nonce: String = "0"
) : CommandPacket() {
    @Serializable
    data class Data(
        val details: String? = null,
        val state: String? = null,
        val timestamps: Activity.Timestamps? = null,
        val assets: Activity.Assets? = null,
        val party: Activity.Party? = null,
        val buttons: List<String> = emptyList(),
        val name: String,
        @SerialName("application_id")
        val applicationId: String,
        val type: Int,
        val metadata: Metadata?
    ) : InboundPacket.Data()

    @Serializable
    data class Metadata(
        @SerialName("button_urls")
        val buttonUrls: List<String> = emptyList()
    )
}

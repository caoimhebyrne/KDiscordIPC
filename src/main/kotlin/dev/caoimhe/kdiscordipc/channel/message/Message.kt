package dev.caoimhe.kdiscordipc.channel.message

import dev.caoimhe.kdiscordipc.channel.message.inbound.InboundMessageData
import kotlinx.serialization.KSerializer

sealed class Message {
    /**
     * The expected/actual opcode of this message
     */
    abstract val opcode: Int

    /**
     * A message being sent to the Discord client
     */
    open class Outbound<T>(
        override val opcode: Int,

        /**
         * The data of type [T] for this message
         */
        val data: T,

        /**
         * The serializer to be used for encoding this data of type [T] to JSON
         */
        val serializer: KSerializer<T>
    ) : Message()

    /**
     * A message being received from the Discord client, after it has been parsed and serialized
     */
    data class Inbound(
        override val opcode: Int,

        /**
         * The data associated with this message, see [InboundMessageData] for how it is de-serialized.
         */
        val data: InboundMessageData
    ) : Message()

    /**
     * A raw message received from the Discord client
     */
    data class Raw(
        override val opcode: Int,

        /**
         * The amount of bytes in this message
         */
        val length: Int,

        /**
         * The JSON data encoded as UTF-8
         */
        val data: ByteArray
    ) : Message() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Raw

            if (opcode != other.opcode) return false
            if (length != other.length) return false
            return data.contentEquals(other.data)
        }

        override fun hashCode(): Int {
            var result = opcode
            result = 31 * result + length
            result = 31 * result + data.contentHashCode()
            return result
        }
    }
}

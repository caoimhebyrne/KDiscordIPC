package dev.caoimhe.kdiscordipc.channel.message.serializer

import dev.caoimhe.kdiscordipc.channel.message.Message
import dev.caoimhe.kdiscordipc.utils.reverse
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.nio.ByteBuffer

// There are 2 integers (4 bytes each) at the start of the header
const val HEADER_LENGTH = 8

/**
 * Converts an outgoing [Message] to a [ByteArray].
 *
 * > The opcode and length in the header are Little Endian Unsigned Integers (32bits).
 *   In some languages, you must convert them as they can be architecture specific.
 *
 * @see <a href="https://github.com/discord/discord-rpc/blob/963aa9f3e5ce81a4682c6ca3d136cddda614db33/documentation/hard-mode.md">Discord Documentation</a>
 */
object MessageToByteEncoder {
    private val json = Json {
        encodeDefaults = true
    }

    /**
     * Converts an outgoing [Message] to a [ByteArray].
     */
    fun <T> encode(message: Message<T>): ByteArray {
        val data = json.encodeToString(message.serializer, message.data)
        val bytes = data.encodeToByteArray()

        // The structure of the bytes is as follows:
        // - Message opcode         (e.g. 1)
        // - Message data length    (e.g. 24)
        // - Message data in bytes
        //
        // The opcode and length in the header are expected to be little endian, so we must reverse the bytes.
        val buffer = ByteBuffer.allocate(HEADER_LENGTH + bytes.size)
        buffer.putInt(message.opcode.reverse())
        buffer.putInt(bytes.size.reverse())
        buffer.put(bytes)

        return buffer.array()
    }
}

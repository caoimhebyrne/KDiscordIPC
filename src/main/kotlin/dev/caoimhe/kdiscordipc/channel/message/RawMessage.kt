package dev.caoimhe.kdiscordipc.channel.message

/**
 * A non-decoded message received through the socket.
 */
data class RawMessage(
    val opcode: Int,
    val length: Int,
    val data: ByteArray
) {
    // For some reason, having `val data: ByteArray` requires this...
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RawMessage

        if (opcode != other.opcode) return false
        if (length != other.length) return false
        return data.contentEquals(other.data)
    }

    // For some reason, having `val data: ByteArray` requires this...
    override fun hashCode(): Int {
        var result = opcode
        result = 31 * result + length
        result = 31 * result + data.contentHashCode()
        return result
    }
}

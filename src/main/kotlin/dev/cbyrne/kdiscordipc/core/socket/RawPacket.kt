package dev.cbyrne.kdiscordipc.core.socket

data class RawPacket(
    val opcode: Int,
    val length: Int,
    val data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RawPacket

        if (opcode != other.opcode) return false
        if (length != other.length) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = opcode
        result = 31 * result + length
        result = 31 * result + data.contentHashCode()
        return result
    }
}

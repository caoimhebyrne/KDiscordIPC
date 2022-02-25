package dev.cbyrne.kdiscordipc.core.error

sealed class EncodeError(reason: String) : Error(reason) {
    class NotSupported(opcode: Int) : EncodeError("Packet with opcode $opcode is not supported!")
}
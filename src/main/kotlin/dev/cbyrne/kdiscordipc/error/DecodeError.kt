package dev.cbyrne.kdiscordipc.error

sealed class DecodeError(reason: String) : Error(reason) {
    class NotSupported(opcode: Int, data: String) :
        DecodeError("Packet with opcode $opcode is not supported! ($data)")
}
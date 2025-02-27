package dev.cbyrne.kdiscordipc.core.error

sealed class DecodeError(reason: String, override val cause: Throwable?) : Error(reason) {
    class NotSupported(opcode: Int, data: String) :
        DecodeError("Packet with opcode $opcode is not supported! ($data)", null)

    class InvalidData(cause: Throwable?) :
        DecodeError("Didn't get to read full data due to the client disconnecting prematurely.", cause)
}
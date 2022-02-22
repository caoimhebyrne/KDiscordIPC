package dev.cbyrne.kdiscordipc.socket.handler

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.error.ConnectionError
import dev.cbyrne.kdiscordipc.packet.pipeline.ByteToMessageDecoder
import dev.cbyrne.kdiscordipc.socket.Socket
import dev.cbyrne.kdiscordipc.util.Platform
import dev.cbyrne.kdiscordipc.util.onBytes
import dev.cbyrne.kdiscordipc.util.platform
import dev.cbyrne.kdiscordipc.util.temporaryDirectory
import java.io.File
import kotlin.concurrent.thread

/**
 * A bridge between [KDiscordIPC] and the Discord IPC server.
 *
 * @see Socket
 * @see KDiscordIPC
 */
class SocketHandler(private val ipc: KDiscordIPC) {
    private val socket = Socket.get()

    /**
     * Connects to the Discord IPC server.
     * This spawns a new thread ("KDiscordIPC Packet Reading"), which is responsible for... well... reading packets?
     *
     * @throws ConnectionError.NoIPCFile If an IPC file isn't found after 9 attempts.
     * @throws ConnectionError.AlreadyConnected If the socket is already connected.
     *
     * @see findIPCFile
     */
    fun connect() {
        if (socket.connected)
            throw ConnectionError.AlreadyConnected

        socket.connect(findIPCFile())

        thread(true, name = "KDiscordIPC Packet Reading") {
            while (socket.connected) {
                read()
            }
        }
    }

    /**
     * Disconnects from the Discord IPC server.
     *
     * @see [Socket.close]
     */
    fun disconnect() = socket.close()

    /**
     * Writes a [ByteArray] to the socket.
     *
     * @see Socket
     * @throws ConnectionError.NotConnected If the socket is closed, or, was never connected.
     */
    fun write(bytes: ByteArray) {
        if (!socket.connected)
            throw ConnectionError.NotConnected

        socket.outputStream.write(bytes)
    }

    /**
     * @throws ConnectionError.NotConnected If the socket is closed, or, was never connected.
     */
    private fun read() {
        // Crab in the code 🦀
        if (!socket.connected)
            throw ConnectionError.NotConnected

        socket.inputStream.onBytes {
            val packet = ByteToMessageDecoder.decode(ipc, it)
            ipc.firePacketRead(packet)
        }
    }

    /**
     * Attempts to find an IPC file to connect with the Discord client's IPC server.
     *
     * This is a recursive function, if no [index] is supplied, it will be defaulted to 0.
     * If ``$TEMP/discord-ipc-0`` doesn't exist, it will try ipc-1...ipc-9 until it finds one.
     *
     * @throws ConnectionError.NoIPCFile If an IPC file isn't found after 9 attempts.
     */
    @Throws(ConnectionError.NoIPCFile::class)
    private fun findIPCFile(index: Int = 0): File {
        if (index > 9)
            throw ConnectionError.NoIPCFile

        val base = if (platform == Platform.WINDOWS) "\\\\?\\pipe\\" else temporaryDirectory
        val file = File(base, "discord-ipc-${index}")
        return file.takeIf { it.exists() } ?: findIPCFile(index + 1)
    }
}
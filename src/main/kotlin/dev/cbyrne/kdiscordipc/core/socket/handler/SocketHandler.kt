package dev.cbyrne.kdiscordipc.core.socket.handler

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.error.ConnectionError
import dev.cbyrne.kdiscordipc.core.error.DecodeError
import dev.cbyrne.kdiscordipc.core.packet.pipeline.ByteToMessageDecoder
import dev.cbyrne.kdiscordipc.core.socket.Socket
import dev.cbyrne.kdiscordipc.core.util.Platform
import dev.cbyrne.kdiscordipc.core.util.platform
import dev.cbyrne.kdiscordipc.core.util.temporaryDirectory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.net.SocketException

/**
 * A bridge between [KDiscordIPC] and the Discord IPC server.
 *
 * @see Socket
 * @see KDiscordIPC
 */
class SocketHandler(
    scope: CoroutineScope,
    socketSupplier: () -> Socket,
    private val onDisconnect: () -> Unit
) {
    private val socket = socketSupplier()
    private val outboundBytes = MutableSharedFlow<ByteArray>()

    val connected: Boolean
        get() = socket.connected

    val events = flow {
        while (connected) {
            try {
                val rawPacket = socket.read()
                ByteToMessageDecoder.decode(rawPacket)?.let { emit(it) }
            } catch (e: DecodeError) {
                if (e is DecodeError.InvalidData) {
                    throw ConnectionError.Disconnected
                }
            } catch (e: IOException) {
                if (!isDisconnectionException(e)) {
                    KDiscordIPC.logger.error("An error occurred when attempting to read from the Discord socket. (Attempting to disconnect if not disconnected already!): ", e)
                }

                disconnect()
            }
        }
    }.flowOn(Dispatchers.IO)

    init {
        outboundBytes.onEach {
            try {
                socket.write(it)
            } catch (e: IOException) {
                if (!isDisconnectionException(e)) {
                    KDiscordIPC.logger.error("An error occurred when attempting to write to the Discord socket. (Attempting to disconnect if not disconnected already!): ", e)
                }

                // Attempt to disconnect, if that fails, an error will be thrown anyway. We can't send errors back
                // to the user here.
                disconnect()
            }
        }.launchIn(scope)
    }

    /**
     * Connects to the Discord IPC server.
     * This spawns a new thread ("KDiscordIPC Packet Reading"), which is responsible for... well... reading packets?
     *
     * @throws ConnectionError.NoIPCFile If an IPC file isn't found after 9 attempts.
     * @throws ConnectionError.AlreadyConnected If the socket is already connected.
     *
     * @see findIPCFile
     */
    fun connect(index: Int = 0) {
        if (socket.connected)
            throw ConnectionError.AlreadyConnected

        try {
            socket.connect(findIPCFile(index))
        } catch (e: IOException) {
            throw ConnectionError.Failed
        }
    }

    /**
     * Disconnects from the Discord IPC server.
     *
     * @see [Socket.close]
     */
    fun disconnect() {
        socket.close()
        onDisconnect()
    }

    /**
     * Writes a [ByteArray] to the socket.
     *
     * @see Socket
     * @throws ConnectionError.NotConnected If the socket is closed, or, was never connected.
     */
    suspend fun write(bytes: ByteArray) {
        if (!socket.connected)
            throw ConnectionError.NotConnected

        withContext(Dispatchers.IO) {
            outboundBytes.emit(bytes)
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
        var file = File(base, "discord-ipc-${index}")

        if (platform == Platform.LINUX) {
            // Snap support
            if (!file.exists()) {
                file = File(base, "snap.discord/discord-ipc-${index}")
            }
            // Flatpak support
            if (!file.exists()) {
                file = File(base, "app/com.discordapp.Discord/discord-ipc-${index}")
            }
        }

        return file.takeIf { it.exists() } ?: findIPCFile(index + 1)
    }

    /**
     * If a given [IOException] is disconnection-related or not.
     */
    private fun isDisconnectionException(e: IOException) =
        e is SocketException ||
        e.message?.contains("Stream Closed", true) == true || e.message?.contains("The pipe is being closed", true) == true
}
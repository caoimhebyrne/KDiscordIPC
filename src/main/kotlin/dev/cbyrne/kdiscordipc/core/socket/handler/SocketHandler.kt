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
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.exists

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
                    KDiscordIPC.logger.error("Received invalid data, assuming that Discord has disconnected from the socket.", e)
                    disconnect()
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
     * @see findIPCPath
     */
    fun connect(index: Int = 0) {
        if (socket.connected)
            throw ConnectionError.AlreadyConnected

        try {
            socket.connect(findIPCPath(index).toFile())
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
     * A list of subdirectories of the temporary directory that the discord-ipc file may be present in.
     */
    private val temporarySubdirectories = listOf(
        // Flatpak
        listOf("app", "com.discordapp.Discord"),
        listOf("app", "com.discordapp.DiscordCanary"),

        // Snap
        listOf("snap.discord"),
        listOf("snap.discord-canary"),
        listOf("snap.discord-ptb"),
    )

    /**
     * @see findIPCPath
     */
    private fun findIPCPath(index: Int): Path {
        // On Windows, we should immediately have some Discord IPC file within the pipe device.
        return if (platform == Platform.WINDOWS) {
            findIPCPath(index, Path.of("\\\\.\\pipe\\"))
        } else {
            // The IPC file may be in the temporary directory itself. If not, then we should check some common
            // subdirectories.
            findIPCPath(index, Path.of(temporaryDirectory))?.let { return it }

            temporarySubdirectories.firstNotNullOfOrNull { directory ->
                findIPCPath(index, Path.of(temporaryDirectory, *directory.toTypedArray()))
            }
        } ?: throw ConnectionError.NoIPCFile
    }

    /**
     * Attempts to find an IPC file to connect with the Discord client's IPC server.
     *
     * This is a recursive function, if no [index] is supplied, it will be defaulted to 0.
     * If ``$TEMP/discord-ipc-0`` doesn't exist, it will try ipc-1...ipc-9 until it finds one.
     *
     * @throws ConnectionError.NoIPCFile If an IPC file isn't found after 9 attempts.
     */
    private fun findIPCPath(index: Int = 0, tempDirectory: Path): Path? {
        if (index > 9) {
            return null
        }

        val ipcFilePath = tempDirectory / "discord-ipc-${index}"
        return ipcFilePath.takeIf { it.exists() } ?: findIPCPath(index + 1, tempDirectory)
    }

    /**
     * If a given [IOException] is disconnection-related or not.
     */
    private fun isDisconnectionException(e: IOException) =
        e is SocketException ||
        e.message?.contains("Stream Closed", true) == true || e.message?.contains("The pipe is being closed", true) == true
}
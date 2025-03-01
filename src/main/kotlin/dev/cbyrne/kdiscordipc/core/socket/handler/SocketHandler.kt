package dev.cbyrne.kdiscordipc.core.socket.handler

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.error.ConnectionError
import dev.cbyrne.kdiscordipc.core.error.DecodeError
import dev.cbyrne.kdiscordipc.core.packet.pipeline.ByteToMessageDecoder
import dev.cbyrne.kdiscordipc.core.socket.Socket
import dev.cbyrne.kdiscordipc.core.util.Platform.WINDOWS
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
    private val possibleUnixPaths = listOf("", "/app/com.discordapp.Discord/", "/snap.discord/", "/snap.discord-canary/", "/snap.discord-ptb/")

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
     * @see findIPCFile
     */
    fun connect(index: Int = 0) {
        if (socket.connected)
            throw ConnectionError.AlreadyConnected

        socket.connect(findIPCFile(index))
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
     * This function recursively searches for IPC files with names `discord-ipc-[index]` in various directories.
     * If no [index] is supplied, it will default to 0. It first checks for the file with `discord-ipc-0`,
     * then `discord-ipc-1`, and so on, until it finds an existing IPC file or exceeds the maximum index of [MAX_IPC].
     *
     * If no file is found in the current path, it moves to the next path and starts over with index 0.
     * The search stops when the IPC file is found or all paths have been exhausted.
     *
     * The search will attempt to find IPC files at the following directories (depending on the platform):
     * - For Windows: `\\\\?\\pipe\\`
     * - For Unix-like platforms: various predefined paths stored in [possibleUnixPaths].
     *
     * @param index The current index for the IPC file search, starting at 0. This is incremented recursively until a file is found or the max index is reached.
     * @param path The current path index to check in [possibleUnixPaths]. If the current path doesn't contain an IPC file, the function will move to the next path.
     *
     * @throws ConnectionError.NoIPCFile If no IPC file is found after searching all paths and exceeding the maximum index.
     */
    @Throws(ConnectionError.NoIPCFile::class)
    private fun findIPCFile(index: Int = 0, path: Int = 0): File {
        if (index > MAX_IPC) {
            if (platform != WINDOWS)
            // If the ipc index is >MAX_IPC, reset to 0 and try the next unix path
                return findIPCFile(index = 0, path = path + 1)

            throw ConnectionError.NoIPCFile
        }

        val basePath = when (platform) {
            WINDOWS -> "\\\\?\\pipe\\"
            else -> "${temporaryDirectory}/${possibleUnixPaths[path]}"
        }

        val file = File(basePath, "discord-ipc-$index")

        return file.takeIf { it.exists() }
            ?: findIPCFile(index + 1, path)
    }

    /**
     * If a given [IOException] is disconnection-related or not.
     */
    private fun isDisconnectionException(e: IOException) =
        e is SocketException ||
        e.message?.contains("Stream Closed", true) == true || e.message?.contains("The pipe is being closed", true) == true

    companion object {
        // The maximum index for the IPC
        private const val MAX_IPC = 9
    }
}

/*
 *     KDiscordIPC is a library for interacting with the discord client via IPC
 *     Copyright (C) 2021  Conor Byrne
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

@file:Suppress("ControlFlowWithEmptyBody")

package dev.cbyrne.kdiscordipc.socket

import dev.cbyrne.kdiscordipc.exceptions.SocketConnectionException
import dev.cbyrne.kdiscordipc.exceptions.SocketDisconnectionException
import dev.cbyrne.kdiscordipc.packet.Packet
import dev.cbyrne.kdiscordipc.packet.RawPacket
import dev.cbyrne.kdiscordipc.packet.pipeline.ByteArrayToRawPacketDecoder
import dev.cbyrne.kdiscordipc.packet.pipeline.PacketToByteArrayEncoder
import dev.cbyrne.kdiscordipc.packet.pipeline.RawPacketToPacketDecoder
import dev.cbyrne.kdiscordipc.socket.impl.UnixSystemSocket
import dev.cbyrne.kdiscordipc.socket.impl.WindowsSystemSocket
import dev.cbyrne.kdiscordipc.util.readBytes
import org.newsclub.net.unix.AFUNIXSocket
import java.io.File
import java.io.IOException
import kotlin.concurrent.thread

/**
 * The class which interacts with Discord via unix sockets
 *
 * @see AFUNIXSocket
 * @see SocketListener
 */
class DiscordSocket {
    // TODO: Maybe do something better
    private val socket: SystemSocket =
        if (System.getProperty("os.name").contains("win", true)) {
            WindowsSystemSocket()
        } else {
            UnixSystemSocket()
        }

    private val encoder = PacketToByteArrayEncoder()
    private val decoder = ByteArrayToRawPacketDecoder()
    private val packetDecoder = RawPacketToPacketDecoder()

    val isConnected: Boolean
        get() = socket.isConnected

    /**
     * The listener which will handle packets once they are decoded
     *
     * @see ByteArrayToRawPacketDecoder
     * @see RawPacketToPacketDecoder
     */
    var listener: SocketListener? = null

    /**
     * Connects to the Discord IPC socket
     * This method starts a thread which will constantly attempt to read for packets whilst the socket is still connected
     *
     * @throws SocketConnectionException If an error has occurred during the connection
     */
    @Throws(SocketConnectionException::class)
    fun connect() {
        socket.connect(getIpcFile(0))

        thread(start = true) {
            while (isConnected) {
                listener?.onPacket(readPacket())
            }
        }
    }

    /**
     * Closes the connection to the unix socket
     *
     * @throws IllegalStateException If the socket is already closed
     * @throws SocketDisconnectionException If an error has occurred when closing this socket
     */
    @Throws(IllegalStateException::class, SocketDisconnectionException::class)
    fun disconnect() = socket.disconnect()


    /**
     * Sends a [Packet] tot the connected [AFUNIXSocket] socket
     *
     * @param packet The packet to send
     * @throws IllegalStateException If the socket is not connected yet
     */
    fun send(packet: Packet) {
        if (!socket.isConnected) throw IllegalStateException("You must connect to the socket before sending packets")

        socket.outputStream?.apply {
            val encodedPacket = encoder.encode(packet)

            try {
                write(encodedPacket)
            } catch (t: Throwable) {
                disconnect()
                listener?.onSocketClosed(t.localizedMessage)
            }
        }
    }

    /**
     * Reads a [RawPacket] from the socket's InputStream
     * @return A [RawPacket] instance containing the packet data
     * @see ByteArrayToRawPacketDecoder
     */
    @Suppress("StatementWithEmptyBody")
    private fun readRawPacket(): RawPacket? {
        if (!socket.isConnected) throw IllegalStateException("You must connect to the socket before reading packets")

        socket.inputStream?.apply {
            while (available() == 0) {
            }

            return try {
                val bytes = readBytes(available())
                decoder.decode(bytes)
            } catch (t: Throwable) {
                disconnect()
                listener?.onSocketClosed(t.localizedMessage)

                null
            }
        }

        return null
    }

    /**
     * Decodes a [RawPacket] to a [Packet] via [RawPacketToPacketDecoder]
     *
     * @see RawPacketToPacketDecoder
     * @see connect
     * @see readRawPacket
     */
    private fun readPacket(): Packet {
        val rawPacket = readRawPacket() ?: throw IllegalStateException("Failed to read raw packet")
        return packetDecoder.decode(rawPacket)
            ?: throw IllegalStateException("Received unknown packet ${rawPacket.opcode} with data ${rawPacket.data}")
    }

    /**
     * Returns a path to the IPC socket depending on the platform
     *
     * If on Windows, "\\?\pipe\discord-ipc-index" will always be returned
     * If on a Unix system, "%tempdir%/discord-ipc-index" will be returned
     *
     * @throws IOException On unix if a temporary directory could not be found
     * @see [getTempDir]
     */
    @Throws(IOException::class)
    private fun getIpcFile(rpcIndex: Int): File {
        val platform = System.getProperty("os.name").lowercase()

        return if (platform.contains("win")) {
            File("\\\\?\\pipe\\discord-ipc-$rpcIndex")
        } else {
            File(getTempDir(), "discord-ipc-${rpcIndex}")
        }
    }

    /**
     * Gets the temporary directory path for the system
     *
     * @see [File.createTempFile]
     * @throws IOException If the temporary file could not be created
     */
    @Throws(IOException::class)
    private fun getTempDir(): String {
        return File.createTempFile("kdiscordipc", "").parent
    }
}

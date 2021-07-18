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

package dev.cbyrne.kdiscordipc.socket.impl

import dev.cbyrne.kdiscordipc.exceptions.SocketConnectionException
import dev.cbyrne.kdiscordipc.exceptions.SocketDisconnectionException
import dev.cbyrne.kdiscordipc.socket.SystemSocket
import org.newsclub.net.unix.AFUNIXSocket
import org.newsclub.net.unix.AFUNIXSocketAddress
import java.io.File
import java.io.InputStream
import java.io.OutputStream

internal class UnixSystemSocket : SystemSocket {
    private val socket = AFUNIXSocket.newInstance()

    override val isConnected: Boolean
        get() = socket.isConnected && !socket.isClosed

    override val inputStream: InputStream?
        get() = socket.inputStream

    override val outputStream: OutputStream?
        get() = socket.outputStream

    @Throws(SocketConnectionException::class)
    override fun connect(file: File) {
        try {
            socket.connect(AFUNIXSocketAddress(file))
        } catch (t: Throwable) {
            throw SocketConnectionException(t.localizedMessage)
        }
    }

    @Throws(IllegalStateException::class, SocketDisconnectionException::class)
    override fun disconnect() {
        if (!isConnected) throw IllegalStateException("You must connect to a UnixSystemSocket before disconnecting!")

        try {
            socket.close()
        } catch (t: Throwable) {
            throw SocketDisconnectionException(t.localizedMessage)
        }
    }
}

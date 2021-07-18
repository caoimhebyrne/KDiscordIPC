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

package dev.cbyrne.kdiscordipc.socket

import dev.cbyrne.kdiscordipc.exceptions.SocketConnectionException
import dev.cbyrne.kdiscordipc.exceptions.SocketDisconnectionException
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * An interface for all system sockets to implement
 * This will handle the communication between the library and the discord ipc socket.
 */
internal interface SystemSocket {
    val isConnected: Boolean

    val inputStream: InputStream?
    val outputStream: OutputStream?

    @Throws(SocketConnectionException::class)
    fun connect(file: File)

    @Throws(IllegalStateException::class, SocketDisconnectionException::class)
    fun disconnect()
}

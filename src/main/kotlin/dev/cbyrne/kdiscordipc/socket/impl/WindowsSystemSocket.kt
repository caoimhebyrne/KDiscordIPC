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
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.io.RandomAccessFile

internal class WindowsSystemSocket : SystemSocket {
    private lateinit var socket: RandomAccessFile

    override val inputStream: InputStream = object : InputStream() {
        override fun read(): Int = socket.read()
        override fun available(): Int = socket.length().toInt()
    }

    override val outputStream: OutputStream = object : OutputStream() {
        override fun write(b: Int) = socket.write(b)
        override fun write(b: ByteArray) = socket.write(b)
        override fun write(b: ByteArray, off: Int, len: Int) = socket.write(b, off, len)
    }

    override var isConnected = false

    @Throws(SocketConnectionException::class)
    override fun connect(file: File) {
        try {
            socket = RandomAccessFile(file, "rw")
            isConnected = true
        } catch (t: Throwable) {
            throw SocketConnectionException(t.localizedMessage)
        }
    }

    @Throws(IllegalStateException::class, SocketDisconnectionException::class)
    override fun disconnect() {
        if (!isConnected) throw IllegalStateException("You must connect to a WindowsSystemSocket before disconnecting!")
        isConnected = false

        try {
            socket.close()
        } catch (t: Throwable) {
            throw SocketDisconnectionException(t.localizedMessage)
        }
    }
}

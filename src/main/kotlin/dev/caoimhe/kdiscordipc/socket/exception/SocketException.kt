package dev.caoimhe.kdiscordipc.socket.exception

import java.nio.file.Path
import kotlin.io.path.absolutePathString

internal const val DEFAULT_SOCKET_EXCEPTION_MESSAGE = "An unknown socket error has occurred"

/**
 * A generic error type for all socket-related errors.
 *
 * IOException is a bit too generic, and the [dev.caoimhe.kdiscordipc.socket.Socket] API should use these errors instead.
 */
sealed class SocketException(message: String = DEFAULT_SOCKET_EXCEPTION_MESSAGE) : Exception(message) {
    /**
     * Should be used when we can't determine the exact cause of the exception
     */
    class Generic(underlyingError: Exception? = null) :
        SocketException(underlyingError?.localizedMessage ?: DEFAULT_SOCKET_EXCEPTION_MESSAGE)

    /**
     * Thrown when we are unable to find the socket file
     */
    class NotFound(path: Path) : SocketException("Unable to locate file ${path.absolutePathString()}")

    /**
     * When we are not allowed to read to/write from the socket
     */
    class NotAllowed(path: Path) : SocketException("We do not have permission to access ${path.absolutePathString()}")

    /**
     * When the socket was not known as closed, but we failed to read/write from/to it due to it being closed.
     */
    object Closed : SocketException("The socket was closed unexpectedly")

    /**
     * When an attempt was made to read/write from/to the socket, but we know that it was already closed.
     */
    object Disconnected : SocketException("Attempt was made to interact with closed socket!")
}

package dev.cbyrne.kdiscordipc.core.error

sealed class ConnectionError(reason: String) : Error(reason) {
    object NoIPCFile :
        ConnectionError("Failed to find an IPC file. Is Discord open? Is this application allowed to access temporary files?")

    object AlreadyConnected :
        ConnectionError("You are already connected!")

    object NotConnected :
        ConnectionError("This socket has either been closed, or, was never connected.")

    object Failed :
        ConnectionError("Failed to connect to socket.")
}

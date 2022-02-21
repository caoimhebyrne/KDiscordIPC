package dev.cbyrne.kdiscordipc.error

sealed class ConnectionError(val reason: String) : Error(reason) {
    object NoIPCFile :
        ConnectionError("Failed to find an IPC file. Is Discord open? Is this application allowed to access temporary files?")

    object AlreadyConnected :
        ConnectionError("You are already connected!")
}

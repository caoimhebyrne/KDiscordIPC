# KDiscordIPC

A Kotlin library for interacting with Discord via IPC

## Features

- Fully documented codebase
- macOS and Linux support (including M1 Macs, Windows support is coming soon)
- Easy to use Kotlin DSL for building presence objects

## Installation

You can add KDiscordIPC to your project via [Jitpack](https://jitpack.io):

```groovy
repositories {
    mavenCentral()

    // Required to retrieve KDiscordIPC from GitHub
    maven("https://jitpack.io/")
}

dependencies {
    implementation("com.github.cbyrneee:KDiscordIPC:latest-commit-hash")
}
```

## Usage

```kotlin
import dev.cbyrne.kdiscordipc.*

fun main(args: Array<String>) {
    val ipc = DiscordIPC("application id")

    // Once the READY event is received, the presence will be set
    // This can be set at any time unless the connection has been closed
    ipc.presence = presence {
        state = "Testing..."
        details = "Hello world"
        largeImageKey = "icon"
        largeImageText = "So cool!"
    }

    ipc.listener = object : IPCListener {
        override fun onReadyEvent(event: DiscordEvent.Ready) {
            println("User: ${event.user}")
            
            // You could also set the presence in here if you wish
            // ipc.presence = ...
        }
    }

    ipc.connect()
}
```

## License

[GPL 3.0](https://choosealicense.com/licenses/gpl-3.0)

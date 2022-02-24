# KDiscordIPC

A lightweight and easy to use Discord IPC wrapper for Kotlin

Many other Discord RPC / IPC libraries are either lacking in features, don't work on certain architectures or platforms,
or are no longer maintained. KDiscordIPC aims to stay up-to-date for as long as the Discord IPC API is actually
supported.

## Table of Contents

- [Install](#install)
- [Usage](#usage)
- [Maintainers](#maintainers)
- [License](#license)

## Install

**Maven repository coming soon!**

```groovy
dependencies {
    implementation("dev.cbyrne:kdiscordipc:1.0.0")
}
```

## Usage

```kotlin
val ipc = KDiscordIPC("YOUR_CLIENT_ID")

// It doesn't matter where you set `activity`.
// If your client has not connected yet, the presence will be set when it has connected. 
ipc.activity = activity("Hello", "world") {
    // This is a simple example, you can do things like smallImage(), largeImage(), etc.
    button("Click me", "https://google.com")
    timestamps(System.currentTimeMillis(), System.currentTimeMillis() + 50000)
}

ipc.observer = object : KDiscordIPCObserver {
    override fun onReady(data: ReadyEventData) {
        logger.info("Ready! (${data.user.username}#${data.user.discriminator})")
    }
}

ipc.connect()
```

## Maintainers

[@cbyrneee](https://github.com/cbyrneee)

## License

MIT © 2022 Conor Byrne

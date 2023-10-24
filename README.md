# KDiscordIPC

``Current version: 0.2.2``

A lightweight and easy to use Discord IPC wrapper for Kotlin

Many other Discord RPC / IPC libraries are either lacking in features, don't work on certain architectures or platforms,
or are no longer maintained. KDiscordIPC aims to stay up-to-date for as long as the Discord IPC API is actually
supported

**For documentation on how to use KDiscordIPC, check out the [wiki](https://github.com/cbyrneee/KDiscordIPC/wiki)!**

### Features

- Easy to use Kotlin DSL
- Coroutines based
- KDiscordIPC will work on any platform that supports Java (and Discord), due to using [junixsocket](https://kohlschutter.github.io/junixsocket/), a library that has
  support for many unix based operating systems, and architectures. On Windows, KDiscordIPC will use the [RandomAccessFile](https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/io/RandomAccessFile.html) API
- Aiming to have full interop with Discord's [GameSDK](https://discord.com/developers/docs/game-sdk/sdk-starter-guide)

## Install

You can add KDiscordIPC to your project via [Jitpack](https://jitpack.io).

```kts
repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("com.github.caoimhebyrne:KDiscordIPC:0.2.2")
}
```

## Example

```kotlin
val ipc = KDiscordIPC("YOUR_CLIENT_ID")

ipc.on<ReadyEvent> {
    logger.info("Ready! (${data.user.username}#${data.user.discriminator})")

    ipc.activityManager.setActivity("Hello", "world") {
        button("Click me", "https://google.com")
        timestamps(System.currentTimeMillis(), System.currentTimeMillis() + 50000)
    }

    // Want to get the information of another user? Sure thing!
    val user = ipc.userManager.getUser("USER_ID")
}

ipc.connect()
```

## Maintainers

[Caoimhe Byrne](https://github.com/caoimhebyrne)

## License

MIT © 2023 Caoimhe Byrne

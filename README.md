# KDiscordIPC

A lightweight and easy to use Discord IPC wrapper for Kotlin

Many other Discord RPC / IPC libraries are either lacking in features, don't work on certain architectures or platforms,
or are no longer maintained. KDiscordIPC aims to stay up-to-date for as long as the Discord IPC API is actually
supported

**For documentation on how to use KDiscordIPC, check out the [wiki](https://github.com/cbyrneee/KDiscordIPC/wiki)!**

### Features

- Easy to use Kotlin DSL
- Coroutines based
- Uses Java's [native socket library](https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/net/UnixDomainSocketAddress.html)
    - This means that KDiscordIPC will work on any platform that supports Java 16+ (and Discord)
- Aiming to have full interop with Discord's [GameSDK](https://discord.com/developers/docs/game-sdk/sdk-starter-guide)

## Install

**Maven repository coming soon!**

```groovy
dependencies {
    implementation("dev.cbyrne:kdiscordipc:1.0.0")
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

[@cbyrneee](https://github.com/cbyrneee)

## License

MIT © 2022 Conor Byrne

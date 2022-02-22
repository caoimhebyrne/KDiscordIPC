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
val ipc = KDiscordIPC("YOUR_CLIENT_ID)
ipc.connect()

// More info soon
```

## Maintainers

[@cbyrneee](https://github.com/cbyrneee)

## License

MIT © 2022 Conor Byrne

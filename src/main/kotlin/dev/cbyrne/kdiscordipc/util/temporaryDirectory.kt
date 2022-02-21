package dev.cbyrne.kdiscordipc.util

val temporaryDirectory =
    System.getenv("XDG_RUNTIME_DIR") ?: System.getenv("TMPDIR") ?: System.getenv("TMP") ?: System.getenv("TMPDIR")

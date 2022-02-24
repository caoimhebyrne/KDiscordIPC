package dev.cbyrne.kdiscordipc.activity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscordActivity(
    var details: String,
    var state: String,
    var timestamps: Timestamps? = null,
    var assets: Assets? = null,
    var party: Party? = null,
    var secrets: Secrets? = null,
    var buttons: MutableList<Button>? = null,
    var instance: Boolean? = false
) {
    @Serializable
    data class Timestamps(
        val start: Long,
        val end: Long
    )

    @Serializable
    data class Assets(
        @SerialName("large_image")
        var largeImage: String? = null,
        @SerialName("large_text")
        var largeText: String? = null,
        @SerialName("small_image")
        var smallImage: String? = null,
        @SerialName("small_text")
        var smallText: String? = null
    )

    @Serializable
    data class Party(
        val id: String,
        val size: List<Int>
    )

    @Serializable
    data class Secrets(
        val join: String? = null,
        val match: String? = null,
        val spectate: String? = null
    )

    @Serializable
    data class Button(
        val label: String,
        val url: String
    )
}

fun activity(
    details: String,
    state: String,
    init: DiscordActivity.() -> Unit
) = DiscordActivity(details, state).apply(init)

fun DiscordActivity.button(label: String, url: String) {
    if (this.buttons == null)
        this.buttons = mutableListOf()

    this.buttons?.add(DiscordActivity.Button(label, url))
}

fun DiscordActivity.timestamps(start: Long, end: Long) {
    this.timestamps = DiscordActivity.Timestamps(start, end)
}

fun DiscordActivity.smallImage(key: String, text: String? = null) {
    if (this.assets == null)
        this.assets = DiscordActivity.Assets()

    this.assets?.smallImage = key
    this.assets?.smallText = text
}

fun DiscordActivity.largeImage(key: String, text: String? = null) {
    if (this.assets == null)
        this.assets = DiscordActivity.Assets()

    this.assets?.largeImage = key
    this.assets?.largeText = text
}

fun DiscordActivity.party(id: String, size: List<Int>) {
    this.party = DiscordActivity.Party(id, size)
}

fun DiscordActivity.secrets(join: String? = null, match: String? = null, spectate: String? = null) {
    this.secrets = DiscordActivity.Secrets(join, match, spectate)
}
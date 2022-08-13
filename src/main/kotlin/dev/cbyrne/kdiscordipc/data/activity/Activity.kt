@file:Suppress("unused")

package dev.cbyrne.kdiscordipc.data.activity

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.*

@Serializable
data class Activity(
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
        var start: Long,
        var end: Long
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
        var id: String,
        var size: PartySize
    ) {
        @Serializable(with = PartySize.PartySizeSerializer::class)
        data class PartySize(
            var currentSize: Int,
            var maxSize: Int
        ) {
            class PartySizeSerializer : KSerializer<PartySize> {
                override val descriptor: SerialDescriptor =
                    IntArraySerializer().descriptor

                override fun deserialize(decoder: Decoder) =
                    decoder.decodeStructure(IntArraySerializer().descriptor) {
                        val currentSize = decodeIntElement(Int.serializer().descriptor, 0)
                        val maxSize = decodeIntElement(Int.serializer().descriptor, 1)
                        PartySize(currentSize, maxSize)
                    }

                override fun serialize(encoder: Encoder, value: PartySize) {
                    encoder.encodeCollection(IntArraySerializer().descriptor, 2) {
                        encodeIntElement(Int.serializer().descriptor, 0, value.currentSize)
                        encodeIntElement(Int.serializer().descriptor, 1, value.maxSize)
                    }
                }
            }
        }
    }

    @Serializable
    data class Secrets(
        var join: String? = null,
        var match: String? = null,
        var spectate: String? = null
    )

    @Serializable
    data class Button(
        var label: String,
        var url: String
    )
}

fun activity(
    details: String,
    state: String,
    init: Activity.() -> Unit
) = Activity(details, state).apply(init)

fun Activity.button(label: String, url: String) {
    if (this.buttons == null)
        this.buttons = mutableListOf()

    this.buttons?.add(Activity.Button(label, url))
}

fun Activity.timestamps(start: Long, end: Long) {
    this.timestamps = Activity.Timestamps(start, end)
}

fun Activity.smallImage(key: String, text: String? = null) {
    if (this.assets == null)
        this.assets = Activity.Assets()

    this.assets?.smallImage = key
    this.assets?.smallText = text
}

fun Activity.largeImage(key: String, text: String? = null) {
    if (this.assets == null)
        this.assets = Activity.Assets()

    this.assets?.largeImage = key
    this.assets?.largeText = text
}

fun Activity.party(id: String, currentSize: Int, maxSize: Int) {
    this.party = Activity.Party(id, Activity.Party.PartySize(currentSize, maxSize))
}

fun Activity.secrets(join: String? = null, match: String? = null, spectate: String? = null) {
    this.secrets = Activity.Secrets(join, match, spectate)
}
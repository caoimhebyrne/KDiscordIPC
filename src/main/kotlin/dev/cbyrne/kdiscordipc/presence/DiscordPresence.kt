/*
 *     KDiscordIPC is a library for interacting with the discord client via IPC
 *     Copyright (C) 2021  Conor Byrne
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.cbyrne.kdiscordipc.presence

import dev.cbyrne.kdiscordipc.DiscordIPC

/**
 * A class representing a discord activity / presence object
 * Instead of using the constructor, it is recommended to use the DSL ([presence])
 *
 * @see [DiscordIPC.presence]
 */
data class DiscordPresence(
    var state: String? = null,
    var details: String? = null,
    var startTimestamp: Long? = null,
    var endTimestamp: Long? = null,
    var largeImageKey: String? = null,
    var largeImageText: String? = null,
    var smallImageKey: String? = null,
    var smallImageText: String? = null,
    var partyId: String? = null,
    var partySize: Int = 1,
    var partyMax: Int = 1,
    var matchSecret: String? = null,
    var joinSecret: String? = null,
    var spectateSecret: String? = null,
    var instance: Boolean = false
) {
    fun toNativeJson() = mapOf(
        "state" to state,
        "details" to details,
        "timestamps" to mapOf("start" to startTimestamp, "end" to endTimestamp),
        "assets" to mapOf(
            "large_image" to largeImageKey,
            "large_text" to largeImageText,
            "small_image" to smallImageKey,
            "small_text" to smallImageText
        ),
        "party" to if (partyId == null) null else mapOf(
            "id" to partyId,
            "size" to arrayOf(partySize, partyMax)
        ),
        "secrets" to mapOf(
            "join" to joinSecret,
            "spectate" to spectateSecret,
            "match" to matchSecret
        ),
        "instance" to instance
    )

    companion object {
        fun fromNative(native: Map<String, Any>) = presence {
            val timestamps = native["timestamps"] as Map<*, *>?
            val assets = native["assets"] as Map<*, *>?
            val party = native["party"] as Map<*, *>?
            val secrets = native["secrets"] as Map<*, *>?

            state = native["state"]?.toString()
            details = native["details"]?.toString()

            startTimestamp = (timestamps?.get("start") as Double?)?.toLong()
            endTimestamp = (timestamps?.get("end") as Double?)?.toLong()

            largeImageKey = assets?.get("large_image")?.toString()
            smallImageKey = assets?.get("small_image")?.toString()
            largeImageText = assets?.get("large_text")?.toString()
            smallImageText = assets?.get("small_text")?.toString()

            partyId = party?.get("id")?.toString()

            joinSecret = secrets?.get("join")?.toString()
            spectateSecret = secrets?.get("spectate")?.toString()
            matchSecret = secrets?.get("match")?.toString()

            instance = native["instance"] as Boolean? == true
        }
    }
}

fun presence(init: DiscordPresence.() -> Unit) = DiscordPresence().apply(init)

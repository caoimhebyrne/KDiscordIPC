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
            "large_image" to (largeImageKey ?: "null"),
            "large_text" to (largeImageText ?: "null"),
            "small_image" to (smallImageKey ?: "null"),
            "small_text" to (smallImageText ?: "null")
        ),
        "party" to if (partyId == null) null else mapOf(
            "id" to (partyId ?: "null"),
            "size" to arrayOf(partySize, partyMax)
        ),
        "secrets" to mapOf(
            "join" to joinSecret,
            "spectate" to spectateSecret,
            "match" to matchSecret
        ),
        "instance" to instance
    )
}

fun presence(init: DiscordPresence.() -> Unit) = DiscordPresence().apply(init)

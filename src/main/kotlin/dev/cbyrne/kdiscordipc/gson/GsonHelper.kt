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

package dev.cbyrne.kdiscordipc.gson

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

inline fun <reified T : Any> String?.fromJson(): T? = this?.let {
    try {
        val type = object : TypeToken<T>() {}.type
        return GsonBuilder().setLenient().serializeNulls().create().fromJson(this, type)
    } catch (t: Throwable) {
        t.printStackTrace()
        return null
    }
}

fun Any.toJson(): String? = this.let {
    runCatching {
        return GsonBuilder().setLenient().create().toJson(this)
    }

    return null
}

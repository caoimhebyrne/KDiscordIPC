package dev.cbyrne.kdiscordipc.data.application

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Application(
    val id: String,
    val name: String,
    val icon: String? = null,
    val description: String? = null,
    val summary: String? = null,
    val hook: Boolean,
    @SerialName("verify_key")
    val verifyKey: String
)

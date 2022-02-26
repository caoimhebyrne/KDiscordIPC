package dev.cbyrne.kdiscordipc.data.relationship

import dev.cbyrne.kdiscordipc.data.user.User
import kotlinx.serialization.Serializable

@Serializable
data class Relationship(
    val type: Int,
    val user: User
    // TODO: Add activity object
)

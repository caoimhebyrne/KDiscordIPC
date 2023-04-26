package dev.caoimhe.kdiscordipc.channel.message

import kotlinx.serialization.KSerializer

/**
 * A message to be sent to, or received from the Discord client
 */
open class Message<T>(
    /**
     * The opcode specified by Discord for this message
     */
    val opcode: Int,

    /**
     * The JSON data to send
     */
    val data: T,

    /**
     * The serializer used to encode data of type T
     */
    val serializer: KSerializer<T>,
)
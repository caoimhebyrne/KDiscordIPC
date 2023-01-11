package dev.cbyrne.kdiscordipc.data.user

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = PremiumType.Companion.PremiumTypeSerializer::class)
enum class PremiumType(val index: Int) {
    /**
     * This user does not have Discord Nitro
     */
    None(0),

    /**
     * This user has 'Discord Nitro Classic' ($5)
     */
    Tier1(1),

    /**
     * This user has 'Discord Nitro' ($10)
     */
    Tier2(2),
    
    /**
     * This user has 'Discord Nitro Basic' ($3)
     */
    Tier3(3);

    companion object {
        open class PremiumTypeSerializer : KSerializer<PremiumType> {
            override val descriptor: SerialDescriptor =
                PrimitiveSerialDescriptor("dev.cbyrne.kdiscordipc.data.user", PrimitiveKind.INT)

            override fun deserialize(decoder: Decoder) =
                decoder.decodeInt().let { value ->
                    values().first { it.index == value }
                }

            override fun serialize(encoder: Encoder, value: PremiumType) = encoder.encodeInt(value.index)
        }
    }
}

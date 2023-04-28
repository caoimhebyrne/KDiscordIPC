package dev.caoimhe.kdiscordipc.models.user

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// This feature is so cool!
// https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serializers.md#specifying-serializer-globally-using-typealias
typealias Flags = @Serializable(with = Flag.Companion::class) List<Flag>

// https://discord.com/developers/docs/resources/user#user-object-user-flags
enum class Flag(val value: Int) {
    /**
     * Discord employee
     */
    Staff(0),

    /**
     * Discord partner
     */
    Partner(1),

    /**
     * HypeSquad Events Member
     */
    Hypesquad(2),

    /**
     * Bug Hunter Level 1
     */
    BugHunterLevel1(3),

    /**
     * House Bravery Member (HYPESQUAD_ONLINE_HOUSE_1)
     */
    HypesquadBravery(6),

    /**
     * House Brilliance Member (HYPESQUAD_ONLINE_HOUSE_2)
     */
    HypesquadBrilliance(7),

    /**
     * House Balance Member (HYPESQUAD_ONLINE_HOUSE_3)
     */
    HouseBalance(8),

    /**
     * Early Nitro Supporter (PREMIUM_EARLY_SUPPORTER)
     */
    EarlyNitroSupporter(9),

    /**
     * User is a team
     */
    TeamPseudoUser(10),

    /**
     * Bug Hunter Level 2
     */
    BugHunterLevel2(14),

    /**
     * Verified bot
     */
    VerifiedBot(16),

    /**
     * Early Verified Bot Developer
     */
    VerifiedDeveloper(17),

    /**
     * Moderator Programs Alumni
     */
    CertifiedModerator(18),

    /**
     * Bot uses only HTTP interactions and is shown in the online member list
     */
    BotUsesHttpInteractions(19),

    /**
     * User is an Active Developer
     */
    ActiveDeveloper(22);

    /**
     * Converts an [Int] to a list of [Flag]
     */
    companion object : KSerializer<List<Flag>> {
        override val descriptor = PrimitiveSerialDescriptor("Flags", PrimitiveKind.INT)

        override fun deserialize(decoder: Decoder): List<Flag> {
            val integer = decoder.decodeInt()
            return Flag.values().filter { integer and (1 shl it.value) != 0 }
        }

        override fun serialize(encoder: Encoder, value: List<Flag>) {
            error("Flags can not be serialized!")
        }
    }
}

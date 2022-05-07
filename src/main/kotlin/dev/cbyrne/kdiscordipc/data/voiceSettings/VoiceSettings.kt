package dev.cbyrne.kdiscordipc.data.voiceSettings

import dev.cbyrne.kdiscordipc.core.event.data.EventData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface VoiceSettingsInterface {
    val input: InputOutput?
    val output: InputOutput?
    val mode: Mode?

    @SerialName("automatic_gain_control")
    val automaticGainControl: Boolean?

    @SerialName("echo_cancellation")
    val echoCancellation: Boolean?

    @SerialName("noise_suppression")
    val noiseSuppression: Boolean?

    val qos: Boolean?

    @SerialName("silence_warning")
    val silenceWarnings: Boolean?

    val mute: Boolean?
    val deaf: Boolean?
}

@Serializable
data class VoiceSettings(
    override val input: InputOutput,
    override val output: InputOutput,
    override val mode: Mode,
    @SerialName("automatic_gain_control") override val automaticGainControl: Boolean,
    @SerialName("echo_cancellation") override val echoCancellation: Boolean,
    @SerialName("noise_suppression") override val noiseSuppression: Boolean,
    override val qos: Boolean,
    @SerialName("silence_warning") override val silenceWarnings: Boolean,
    override val mute: Boolean,
    override val deaf: Boolean
): EventData(), VoiceSettingsInterface

@Serializable
data class InputOutput(
    @SerialName("available_devices") val availableDevices: Array<Device>? = null,
    @SerialName("device_id") val deviceId: String? = null,
    val volume: Float? = null
){
    @Serializable
    data class Device(
        val id: String,
        val name: String
    )
}
@Serializable
data class Mode(
    val type: String,
    @SerialName("auto_threshold") val autoThreshold: Boolean,
    val threshold: Float,
    val shortcut: Array<ShortCutKeyCombo>,
    val delay: Float
) {
    @Serializable
    data class ShortCutKeyCombo(
        val type: Int,
        val code: Int,
        val name: String
    )
}


package dev.cbyrne.kdiscordipc.core.event

import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable
enum class DiscordEvent(val eventName: String) {
    CurrentUserUpdate("CURRENT_USER_UPDATE"),
    ActivityJoin("ACTIVITY_JOIN"),
    ActivitySpectate("ACTIVITY_SPECTATE"),
    ActivityJoinRequest("ACTIVITY_JOIN_REQUEST"),
    ActivityInvite("ACTIVITY_INVITE"),
    LobbyUpdate("LOBBY_UPDATE"),
    LobbyDelete("LOBBY_DELETE"),
    LobbyMemberConnect("LOBBY_MEMBER_CONNECT"),
    LobbyMemberUpdate("LOBBY_MEMBER_UPDATE"),
    LobbyMemberDisconnect("LOBBY_MEMBER_DISCONNECT"),
    LobbyMessage("LOBBY_MESSAGE"),
    SpeakingStart("SPEAKING_START"),
    SpeakingStop("SPEAKING_STOP"),
    RelationshipUpdate("RELATIONSHIP_UPDATE")
}
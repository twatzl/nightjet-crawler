package eu.twatzl.njcrawler.model.oebb

import kotlinx.serialization.Serializable

@Serializable
data class OebbAccessToken(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val refresh_expires_in: Int,
    val session_state: String,
    val scope: String,
)
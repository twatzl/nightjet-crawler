package eu.twatzl.njcrawler.model.es

import kotlinx.serialization.Serializable

@Serializable
data class Line (
    val id: Int,
    val connectionId: Int,
    val code: String?,
    val from: String?,
    val to: String?,
)

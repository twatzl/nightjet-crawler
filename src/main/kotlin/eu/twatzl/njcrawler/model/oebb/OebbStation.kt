package eu.twatzl.njcrawler.model.oebb

import kotlinx.serialization.Serializable

@Serializable
data class OebbStation(
    val number: Int,
    val longitude: Int,
    val latitude: Int,
    val name: String,
    val meta: String,
)

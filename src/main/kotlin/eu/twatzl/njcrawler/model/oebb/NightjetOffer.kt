package eu.twatzl.njcrawler.model.oebb

import kotlinx.serialization.Serializable

@Serializable
data class NightjetOffer(
    val name: String,
    val price: Float,
)
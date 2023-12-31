package eu.twatzl.njcrawler.model

import kotlinx.serialization.Serializable

@Serializable
data class Offer(
    val name: String,
    val price: Float,
)
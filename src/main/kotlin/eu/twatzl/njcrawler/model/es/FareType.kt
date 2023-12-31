package eu.twatzl.njcrawler.model.es

import kotlinx.serialization.Serializable

@Serializable
data class FareType (
    val id: String,
    val bookable: Boolean,
    val availableSeats: Int,
    val price: Int,
)

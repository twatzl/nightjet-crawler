package eu.twatzl.njcrawler.model.es

import kotlinx.serialization.Serializable

@Serializable
data class PriceClass (
    val placeTypeKey: String,
    val placeTypeIcon: Int,
    val freeSeatsCount: Int,
    val price: Int,
    val fareTypes: List<FareType>,
    val bookable: Boolean,
)

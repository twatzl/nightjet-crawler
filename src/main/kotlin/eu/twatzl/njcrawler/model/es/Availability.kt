package eu.twatzl.njcrawler.model.es

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Availability (
    val requestedDepartureStationId: String,
    val requestedArrivalStationId: String,
    val departureStationId: String,
    val departureStationName: String,
    val departureTime: Instant,
    val arrivalStationId: String,
    val arrivalStationName: String,
    val arrivalTime: Instant,
    val priceClasses: List<PriceClass>,
    val sections: List<Section>, // doesn't seem to contain relevant data yet
)

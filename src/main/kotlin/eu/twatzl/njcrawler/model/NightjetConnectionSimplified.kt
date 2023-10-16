package eu.twatzl.njcrawler.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

/**
 * simplified class that is used to print to combined csv file
 */
data class NightjetConnectionSimplified(
    val trainId: String,
    val departureStationName: String,
    val arrivalStationName: String,
    val departure: Instant,
    val arrival: Instant,
    val seatingOffer: Float?,
    val couchetteOffer: Float?,
    val sleeperOffer: Float?,
)
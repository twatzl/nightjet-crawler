package eu.twatzl.njcrawler.model

import kotlinx.datetime.Instant

/**
 * simplified class that is used to print to combined csv file
 */
data class SimplifiedConnection(
    val trainId: String,
    val departureStationName: String,
    val arrivalStationName: String,
    val departure: Instant,
    val arrival: Instant,
    val seatingOffer: Float?,
    val couchetteOffer: Float?,
    val sleeperOffer: Float?,
)
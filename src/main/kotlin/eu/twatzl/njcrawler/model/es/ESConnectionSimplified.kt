package eu.twatzl.njcrawler.model.es

import kotlinx.datetime.Instant

/**
 * simplified class that is used to print to combined csv file
 */
data class ESConnectionSimplified(
    val trainId: String,
    val departureStationName: String,
    val arrivalStationName: String,
    val departure: Instant,
    val arrival: Instant,
    val seatingOffer: Int?,
    val couchetteOffer: Int?,
    val sleeperOffer: Int?,
)

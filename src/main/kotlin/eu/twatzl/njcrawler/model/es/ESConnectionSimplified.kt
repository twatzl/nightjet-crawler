package eu.twatzl.njcrawler.model.es

import kotlinx.datetime.LocalDateTime

/**
 * simplified class that is used to print to combined csv file
 */
data class ESConnectionSimplified(
    val trainId: String,
    val departureStationName: String,
    val arrivalStationName: String,
    val departure: LocalDateTime,
    val arrival: LocalDateTime,
    val seatingOffer: Int?,
    val couchetteOffer: Int?,
    val sleeperOffer: Int?,
)

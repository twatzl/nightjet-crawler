package eu.twatzl.njcrawler

import java.time.OffsetDateTime

/**
 * simplified class that is used to print to combined csv file
 */
data class OebbNightjetConnectionSimplified(
    val trainId: String,
    val departureStationName: String,
    val arrivalStationName: String,
    val departure: OffsetDateTime,
    val arrival: OffsetDateTime,
    val seatingOffer: Float?,
    val couchetteOffer: Float?,
    val sleeperOffer: Float?,
)
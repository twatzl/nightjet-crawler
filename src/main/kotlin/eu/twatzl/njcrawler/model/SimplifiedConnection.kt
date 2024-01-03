package eu.twatzl.njcrawler.model

import eu.twatzl.njcrawler.util.getCurrentTime
import eu.twatzl.njcrawler.util.getTimezone
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.plus

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
    val retrievedAt: Instant,
) {
    companion object {

        /**
         * creates a connection object without offer values so that CSV generation can stil proceed
         */
        fun errorOffer(
            trainId: String,
            fromStation: Station,
            toStation: Station,
            errorTime: Instant,
        ): SimplifiedConnection {
            return SimplifiedConnection(
                trainId,
                fromStation.name,
                toStation.name,
                errorTime,
                errorTime.plus(1, DateTimeUnit.DAY, getTimezone()),
                null,
                null,
                null,
                getCurrentTime(),
            )
        }
    }
}
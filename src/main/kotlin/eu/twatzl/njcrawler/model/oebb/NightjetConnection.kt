package eu.twatzl.njcrawler.model.oebb

import eu.twatzl.njcrawler.model.NightjetConnectionWithMetadata
import eu.twatzl.njcrawler.model.Station
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class NightjetConnection(
    val departure: Instant,
    val arrival: Instant,
    val bestOffers: Map<String, NightjetOffer>,
)

fun NightjetConnection.addMetadata(
    trainId: String,
    departureStation: Station,
    arrivalStation: Station,
    retrievedAt: Instant,
): NightjetConnectionWithMetadata {
    return NightjetConnectionWithMetadata(
        trainId, departureStation, arrivalStation, departure, arrival, bestOffers, retrievedAt
    )
}
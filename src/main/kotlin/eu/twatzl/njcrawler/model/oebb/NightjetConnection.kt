package eu.twatzl.njcrawler.model.oebb

import eu.twatzl.njcrawler.model.Offer
import eu.twatzl.njcrawler.model.Station
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class NightjetConnection(
    val departure: Instant,
    val arrival: Instant,
    val bestOffers: Map<String, Offer>,
) {
    fun addMetadata(
        trainId: String,
        departureStation: Station,
        arrivalStation: Station,
        retrievedAt: Instant,
    ): NightjetConnectionWithMetadata {
        return NightjetConnectionWithMetadata(
            trainId, departureStation, arrivalStation, departure, arrival, bestOffers, retrievedAt
        )
    }
}
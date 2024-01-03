package eu.twatzl.njcrawler.model.oebb

import eu.twatzl.njcrawler.data.COUCHETTE_OFFER_KEY
import eu.twatzl.njcrawler.data.SEATING_OFFER_KEY
import eu.twatzl.njcrawler.data.SLEEPER_OFFER_KEY
import eu.twatzl.njcrawler.model.Offer
import eu.twatzl.njcrawler.model.SimplifiedConnection
import eu.twatzl.njcrawler.model.Station
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class NightjetConnection(
    val departure: Instant,
    val arrival: Instant,
    val bestOffers: Map<String, Offer>,
) {
    fun toSimplified(
        trainId: String,
        departureStation: Station,
        arrivalStation: Station,
        retrievedAt: Instant,
    ): SimplifiedConnection {
        return SimplifiedConnection(
            trainId,
            departureStation.name,
            arrivalStation.name,
            this.departure,
            this.arrival,
            this.bestOffers[SEATING_OFFER_KEY]?.price,
            this.bestOffers[COUCHETTE_OFFER_KEY]?.price,
            this.bestOffers[SLEEPER_OFFER_KEY]?.price,
            retrievedAt
        )
    }
}
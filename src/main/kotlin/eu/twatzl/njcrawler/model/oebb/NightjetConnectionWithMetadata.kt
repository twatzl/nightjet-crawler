package eu.twatzl.njcrawler.model.oebb

import eu.twatzl.njcrawler.data.COUCHETTE_OFFER_KEY
import eu.twatzl.njcrawler.data.SEATING_OFFER_KEY
import eu.twatzl.njcrawler.data.SLEEPER_OFFER_KEY
import eu.twatzl.njcrawler.model.ConnectionWithMetadata
import eu.twatzl.njcrawler.model.Offer
import eu.twatzl.njcrawler.model.SimplifiedConnection
import eu.twatzl.njcrawler.model.Station
import kotlinx.datetime.Instant

data class NightjetConnectionWithMetadata(
    override val trainId: String,
    val departureStation: Station,
    val arrivalStation: Station,
    val departure: Instant,
    val arrival: Instant,
    val bestOffers: Map<String, Offer>,
    override val retrievedAt: Instant,
) : ConnectionWithMetadata {
    override fun toSimplified(): SimplifiedConnection {
        return SimplifiedConnection(
            this.trainId,
            this.departureStation.name,
            this.arrivalStation.name,
            this.departure,
            this.arrival,
            this.bestOffers[SEATING_OFFER_KEY]?.price,
            this.bestOffers[COUCHETTE_OFFER_KEY]?.price,
            this.bestOffers[SLEEPER_OFFER_KEY]?.price
        )
    }
}

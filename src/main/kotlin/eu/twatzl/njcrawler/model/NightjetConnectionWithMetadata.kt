package eu.twatzl.njcrawler.model

import eu.twatzl.njcrawler.data.COUCHETTE_OFFER_KEY
import eu.twatzl.njcrawler.data.SEATING_OFFER_KEY
import eu.twatzl.njcrawler.data.SLEEPER_OFFER_KEY
import eu.twatzl.njcrawler.model.oebb.NightjetOffer
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

data class NightjetConnectionWithMetadata(
    val trainId: String,
    val departureStation: Station,
    val arrivalStation: Station,
    val departure: Instant,
    val arrival: Instant,
    val bestOffers: Map<String, NightjetOffer>,
    val retrievedAt: Instant,
)

fun NightjetConnectionWithMetadata.toSimplified(): NightjetConnectionSimplified {
    val trainId = this.trainId
    val departure = this.departure
    val arrival = this.arrival
    val departureStationName = this.departureStation.name
    val arrivalStationName = this.arrivalStation.name
    val seatingOffer = this.bestOffers[SEATING_OFFER_KEY]?.price
    val couchetteOffer = this.bestOffers[COUCHETTE_OFFER_KEY]?.price
    val sleeperOffer = this.bestOffers[SLEEPER_OFFER_KEY]?.price
    return NightjetConnectionSimplified(
        trainId,
        departureStationName,
        arrivalStationName,
        departure,
        arrival,
        seatingOffer,
        couchetteOffer,
        sleeperOffer
    )
}
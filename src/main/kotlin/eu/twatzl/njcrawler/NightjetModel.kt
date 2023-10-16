package eu.twatzl.njcrawler

import eu.twatzl.njcrawler.data.Station
import java.time.OffsetDateTime
import java.time.ZonedDateTime

fun OebbNightjetConnection.addMetadata(
    trainId: String,
    departureStation: Station,
    arrivalStation: Station,
    retrievedAt: ZonedDateTime,
): OebbNightjetConnectionWithMetadata {
    return OebbNightjetConnectionWithMetadata(
        trainId, departureStation, arrivalStation, departure, arrival, bestOffers, retrievedAt
    )
}

data class OebbNightjetConnectionWithMetadata(
    val trainId: String,
    val departureStation: Station,
    val arrivalStation: Station,
    val departure: OffsetDateTime,
    val arrival: OffsetDateTime,
    val bestOffers: Map<String, OebbNightjetOffer>,
    val retrievedAt: ZonedDateTime,
)

data class OebbNightjetOffer(
    val name: String,
    val price: Float,
)

//[{"departure":"2023-04-05T21:27:00+02:00","arrival":"2023-04-06T08:34:00+02:00","bestOffers":{"se":{"name":"Komfortticket inkl. Reservierung","price":68}}},{"departure":"2023-04-07T21:27:00+02:00","arrival":"2023-04-08T08:34:00+02:00","bestOffers":{"se":{"name":"Komfortticket inkl. Reservierung","price":68}}},{"departure":"2023-04-08T21:27:00+02:00","arrival":"2023-04-09T08:34:00+02:00","bestOffers":{"se":{"name":"Komfortticket inkl. Reservierung","price":68},"be":{"name":"Komfortticket inkl. Reservierung","price":107.4},"le":{"name":"Komfortticket inkl. Reservierung","price":87.7}}}]
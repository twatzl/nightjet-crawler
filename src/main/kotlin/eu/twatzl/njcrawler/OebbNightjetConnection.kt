package eu.twatzl.njcrawler

import java.time.OffsetDateTime

data class OebbNightjetConnection(
    val departure: OffsetDateTime,
    val arrival: OffsetDateTime,
    val bestOffers: Map<String, OebbNightjetOffer>,
)
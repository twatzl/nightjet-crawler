package eu.twatzl.njcrawler.model.es

/**
 * simplified class that is used to print to combined csv file
 */
data class ESConnectionSimplified(
    val trainId: String,
    val departureStationName: String,
    val arrivalStationName: String,
    val departure: String, // TODO use Instant or LocalDateTime instead
    val arrival: String, // TODO use Instant or LocalDateTime instead
    val seatingOffer: Int?,
    val couchetteOffer: Int?,
    val sleeperOffer: Int?,
)

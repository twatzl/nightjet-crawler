package eu.twatzl.njcrawler.model.es

import eu.twatzl.njcrawler.model.Station
import eu.twatzl.njcrawler.util.getTimezone
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable

@Serializable
data class ESConnectionWithMetadata(
    val trainId: String,
    val availability: Availability,
    val retrievedAt: Instant,
)

fun ESConnectionWithMetadata.toSimplified(): ESConnectionSimplified {
    return ESConnectionSimplified(
        this.trainId,
        this.availability.departureStationName,
        this.availability.arrivalStationName,
        this.availability.departureTime,
        this.availability.arrivalTime,
        // private and women only cabins not considered in the following
        this.availability.priceClasses.find { priceClass -> priceClass.placeTypeKey == "seat-second-class" }?.price,
        this.availability.priceClasses
            .filter { priceClass ->
                (priceClass.placeTypeKey == "couchette-4" || priceClass.placeTypeKey == "couchette-6")
                        && priceClass.price != null}
            .minByOrNull { it.price!! }?.price,
        this.availability.priceClasses
            .filter { priceClass ->
                (priceClass.placeTypeKey == "berth-single" || priceClass.placeTypeKey == "berth-double" || priceClass.placeTypeKey == "berth-triple")
                        && priceClass.price != null}
            .minByOrNull { it.price!! }?.price,
    )
}

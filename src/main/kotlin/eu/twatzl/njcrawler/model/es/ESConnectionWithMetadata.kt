package eu.twatzl.njcrawler.model.es

import eu.twatzl.njcrawler.model.ConnectionWithMetadata
import eu.twatzl.njcrawler.model.SimplifiedConnection
import eu.twatzl.njcrawler.util.getTimezone
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable

@Serializable
data class ESConnectionWithMetadata(
    override val trainId: String,
    val availability: Availability,
    override val retrievedAt: Instant,
): ConnectionWithMetadata {
    override fun toSimplified(): SimplifiedConnection {
        return SimplifiedConnection(
            this.trainId,
            this.availability.departureStationName,
            this.availability.arrivalStationName,
            this.availability.departureTime.toInstant(getTimezone()),
            this.availability.arrivalTime.toInstant(getTimezone()),
            // private and women only cabins not considered in the following
            this.availability.priceClasses.find { priceClass -> priceClass.placeTypeKey == "seat-second-class" }?.price?.toFloat(),
            this.availability.priceClasses
                .filter { priceClass ->
                    (priceClass.placeTypeKey == "couchette-4" || priceClass.placeTypeKey == "couchette-6")
                            && priceClass.price != null
                }
                .minByOrNull { it.price!! }?.price?.toFloat(),
            this.availability.priceClasses
                .filter { priceClass ->
                    (priceClass.placeTypeKey == "berth-single" || priceClass.placeTypeKey == "berth-double" || priceClass.placeTypeKey == "berth-triple")
                            && priceClass.price != null
                }
                .minByOrNull { it.price!! }?.price?.toFloat(),
        )
    }
}

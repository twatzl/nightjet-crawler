package eu.twatzl.njcrawler.model.es

import eu.twatzl.njcrawler.model.SimplifiedConnection
import eu.twatzl.njcrawler.util.getTimezone
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable

@Serializable
data class ESConnection(
    val availabilityResult: AvailabilityResult,
) {
    @Serializable
    data class AvailabilityResult(
        val availability: Availability,
    )

    fun toSimplified(
        trainId: String,
        retrievedAt: Instant,
    ): SimplifiedConnection {
        return SimplifiedConnection(
            trainId,
            this.availabilityResult.availability.departureStationName,
            this.availabilityResult.availability.arrivalStationName,
            this.availabilityResult.availability.departureTime.toInstant(getTimezone()),
            this.availabilityResult.availability.arrivalTime.toInstant(getTimezone()),
            // private and women only cabins not considered in the following
            this.availabilityResult.availability.priceClasses.find { priceClass -> priceClass.placeTypeKey == "seat-second-class" }?.price?.toFloat(),
            this.availabilityResult.availability.priceClasses
                .filter { priceClass ->
                    (priceClass.placeTypeKey == "couchette-4" || priceClass.placeTypeKey == "couchette-6")
                            && priceClass.price != null
                }
                .minByOrNull { it.price!! }?.price?.toFloat(),
            this.availabilityResult.availability.priceClasses
                .filter { priceClass ->
                    (priceClass.placeTypeKey == "berth-single" || priceClass.placeTypeKey == "berth-double" || priceClass.placeTypeKey == "berth-triple")
                            && priceClass.price != null
                }
                .minByOrNull { it.price!! }?.price?.toFloat(),
            retrievedAt
        )
    }
}

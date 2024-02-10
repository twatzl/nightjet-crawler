package eu.twatzl.njcrawler.model.es

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class AvailabilityRequest(
    val fromLocationId: String,
    val toLocationId: String,
    val trainNumber: String,
    val travelDate: LocalDateTime,
    // currently, only one route exists;
    // long term, this should probably be included in TrainConnection data type
    val trainRouteId: String = "77ab1c5a-ea0b-4634-7cdd-08db0daabe3f",
    val passengerTypes: IntArray = intArrayOf(72),  // one adult passenger
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AvailabilityRequest

        if (fromLocationId != other.fromLocationId) return false
        if (toLocationId != other.toLocationId) return false
        if (trainNumber != other.trainNumber) return false
        if (travelDate != other.travelDate) return false
        if (trainRouteId != other.trainRouteId) return false
        if (!passengerTypes.contentEquals(other.passengerTypes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fromLocationId.hashCode()
        result = 31 * result + toLocationId.hashCode()
        result = 31 * result + trainNumber.hashCode()
        result = 31 * result + travelDate.hashCode()
        result = 31 * result + trainRouteId.hashCode()
        result = 31 * result + passengerTypes.contentHashCode()
        return result
    }
}

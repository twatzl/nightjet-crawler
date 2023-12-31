package eu.twatzl.njcrawler.model.es

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Availability (
    val requestedDepartureStationId: String,
    val requestedArrivalStationId: String,
    val departureStationId: String,
    val departureStationName: String,
    val departureTime: String, // TODO use Instant or LocalDateTime instead
    val arrivalStationId: String,
    val arrivalStationName: String,
    val arrivalTime: String, // TODO use Instant or LocalDateTime instead
    val priceClasses: Array<PriceClass>,
    val sections: Array<Section>, // doesn't seem to contain relevant data yet
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Availability

        if (requestedDepartureStationId != other.requestedDepartureStationId) return false
        if (requestedArrivalStationId != other.requestedArrivalStationId) return false
        if (departureStationId != other.departureStationId) return false
        if (departureStationName != other.departureStationName) return false
        if (departureTime != other.departureTime) return false
        if (arrivalStationId != other.arrivalStationId) return false
        if (arrivalStationName != other.arrivalStationName) return false
        if (arrivalTime != other.arrivalTime) return false
        if (!priceClasses.contentEquals(other.priceClasses)) return false
        if (!sections.contentEquals(other.sections)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = requestedDepartureStationId.hashCode()
        result = 31 * result + requestedArrivalStationId.hashCode()
        result = 31 * result + departureStationId.hashCode()
        result = 31 * result + departureStationName.hashCode()
        result = 31 * result + departureTime.hashCode()
        result = 31 * result + arrivalStationId.hashCode()
        result = 31 * result + arrivalStationName.hashCode()
        result = 31 * result + arrivalTime.hashCode()
        result = 31 * result + priceClasses.contentHashCode()
        result = 31 * result + sections.contentHashCode()
        return result
    }
}

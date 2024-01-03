package eu.twatzl.njcrawler.model.es

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Availability(
    val departureStationName: String,
    val departureTime: LocalDateTime,
    val arrivalStationName: String,
    val arrivalTime: LocalDateTime,
    val priceClasses: Array<PriceClass>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Availability

        if (departureStationName != other.departureStationName) return false
        if (departureTime != other.departureTime) return false
        if (arrivalStationName != other.arrivalStationName) return false
        if (arrivalTime != other.arrivalTime) return false
        if (!priceClasses.contentEquals(other.priceClasses)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = departureStationName.hashCode()
        result = 31 * result + departureTime.hashCode()
        result = 31 * result + arrivalStationName.hashCode()
        result = 31 * result + arrivalTime.hashCode()
        result = 31 * result + priceClasses.contentHashCode()
        return result
    }
}

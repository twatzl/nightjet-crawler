package eu.twatzl.njcrawler.model.es

import kotlinx.serialization.Serializable

@Serializable
data class PriceClass(
    val placeTypeKey: String,
    val freeSeatsCount: Int?,
    val price: Int?,
    val fareTypes: Array<FareType>,
    val bookable: Boolean,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PriceClass

        if (placeTypeKey != other.placeTypeKey) return false
        if (freeSeatsCount != other.freeSeatsCount) return false
        if (price != other.price) return false
        if (!fareTypes.contentEquals(other.fareTypes)) return false
        if (bookable != other.bookable) return false

        return true
    }

    override fun hashCode(): Int {
        var result = placeTypeKey.hashCode()
        result = 31 * result + (freeSeatsCount ?: 0)
        result = 31 * result + (price ?: 0)
        result = 31 * result + fareTypes.contentHashCode()
        result = 31 * result + bookable.hashCode()
        return result
    }
}

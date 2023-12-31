package eu.twatzl.njcrawler.model.es

import eu.twatzl.njcrawler.model.Station
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ESConnection(
    val availabilityResult: AvailabilityResult,
)

fun ESConnection.addMetadata(
    trainId: String,
    retrievedAt: Instant,
): ESConnectionWithMetadata {
    return ESConnectionWithMetadata(trainId, availabilityResult.availability, retrievedAt)
}

package eu.twatzl.njcrawler.model.es

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ESConnection(
    val availabilityResult: AvailabilityResult,
) {
    @Serializable
    data class AvailabilityResult(
        val availability: Availability,
    )
}

fun ESConnection.addMetadata(
    trainId: String,
    retrievedAt: Instant,
): ESConnectionWithMetadata {
    return ESConnectionWithMetadata(trainId, availabilityResult.availability, retrievedAt)
}

package eu.twatzl.njcrawler.model.es

import kotlinx.serialization.Serializable

@Serializable
data class AvailabilityResult (
    val availability: Availability,
)
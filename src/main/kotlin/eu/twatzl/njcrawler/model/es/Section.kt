package eu.twatzl.njcrawler.model.es

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Section (
    val id: Int,
    val line: Line,
    val notices: String?,
    val departureStationId: String,
    val departureStationName: String,
    val departureTime: String, // TODO use Instant or LocalDateTime instead
    val arrivalStationId: String,
    val arrivalStationName: String,
    val arrivalTime: String, // TODO use Instant or LocalDateTime instead
)

package eu.twatzl.njcrawler.model

import kotlinx.datetime.Instant

interface ConnectionWithMetadata {
    val trainId: String
    val retrievedAt: Instant

    fun toSimplified(): SimplifiedConnection
}
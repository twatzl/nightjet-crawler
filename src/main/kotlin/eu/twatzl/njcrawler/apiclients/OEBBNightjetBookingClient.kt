package eu.twatzl.njcrawler.apiclients

import eu.twatzl.njcrawler.model.oebb.NightjetConnection
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.datetime.Clock

class OEBBNightjetBookingClient(
    private val httpClient: HttpClient,
) {
    private val endpoint = "https://www.nightjet.com/nj-booking"

    /**
     * query nightjet offers from the nightjet.com platform
     */
    suspend fun getOffer(
        trainId: String,
        fromStationId: String,
        toStationId: String,
        timestamp: Long = Clock.System.now().epochSeconds * 1000,
        numberResults: Int = 3,
    ): Set<NightjetConnection?> {
        val nightjetOfferUrl =
            "${endpoint}/destinations/offers/${trainId}/${fromStationId}/${toStationId}/${timestamp}".encodeURLPath()

        return httpClient.get(nightjetOfferUrl) {
            headers {
                append(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"
                ) // no comment
            }
            parameter("max", numberResults) // ÖBB api doesn't support more than 3 results
        }.body<Set<NightjetConnection?>>()
    }
}
package eu.twatzl.njcrawler.apiclients

import eu.twatzl.njcrawler.model.es.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.datetime.LocalDateTime

class EuropeanSleeperClient(
    private val httpClient: HttpClient,
) {
    private val endpoint = "https://europeansleeperprod-api.azurewebsites.net/api/search/availability?lang=de-DE"

    /**
     * query European Sleeper offers from the europeansleeper.eu platform
     */
    suspend fun getOffer(
        trainNumber: String,
        fromLocationId: String,
        toLocationId: String,
        travelDate: LocalDateTime
    ): ESConnection? {
        val response = httpClient.post(endpoint) {
            headers {
                append(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"
                )
            }
            contentType(ContentType.Application.Json)
            setBody(AvailabilityRequest(fromLocationId, toLocationId, trainNumber, travelDate))
        }

        return if (response.status.value == 200) {
            response.body<ESConnection>()
        } else {
            null
        }
    }
}
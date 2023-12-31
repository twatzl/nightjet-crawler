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
    companion object {
        const val ENDPOINT = "https://europeansleeperprod-api.azurewebsites.net/api/search/availability?lang=de-DE"
    }

    /**
     * query European Sleeper offers from the europeansleeper.eu platform
     */
    suspend fun getOffer(
        trainNumber: String,
        fromLocationId: String,
        toLocationId: String,
        travelDate: LocalDateTime
    ): ESConnection? {
        val body = AvailabilityRequest(fromLocationId, toLocationId, trainNumber, travelDate)
        val response = httpClient.post(ENDPOINT) {
            headers {
                append(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"
                ) // no comment
            }
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        // TODO remove after debugging
        println("response status: ${response.status}")
        println("response body: ${response.body<String>()}")

        // TODO problem: JSON mapping to ESConnection doesn't work, function crashes and throws "timeout"

        return if (response.status.value == 200) {
            response.body<ESConnection>()
        } else {
            null
        }
    }
}
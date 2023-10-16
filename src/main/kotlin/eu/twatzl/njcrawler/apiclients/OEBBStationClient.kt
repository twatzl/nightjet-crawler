package eu.twatzl.njcrawler.apiclients

import eu.twatzl.njcrawler.model.oebb.OebbStation
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class OEBBStationClient(
    private val httpClient: HttpClient,
) {
    companion object {
        val endpoint = "https://shop.oebbtickets.at/api/hafas/v1"
    }

    /**
     * query stations by name from hafas
     * this is necessary to get the hafas ids which are needed to get offers for a connection
     */
    suspend fun getByName(
        accessToken: String,
        name: String,
        count: Int = 15,
    ): Set<OebbStation?>? {
        val url = Url("${endpoint}/stations")
        return httpClient.get(url) {
            headers {
                append("AccessToken", accessToken)
            }
            parameter("name", name)
            parameter("count", count)
        }.body()
    }
}
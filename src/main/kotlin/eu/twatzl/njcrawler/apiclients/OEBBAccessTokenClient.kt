package eu.twatzl.njcrawler.apiclients

import eu.twatzl.njcrawler.model.oebb.OebbAccessToken
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class OEBBAccessTokenClient(
    private val httpClient: HttpClient,
) {
    companion object {
        val endpoint = "https://shop.oebbtickets.at/api/domain/v1"
    }

    suspend fun getToken(): OebbAccessToken {
        val url = Url("$endpoint/anonymousToken")
        return httpClient.get(url).body()
    }
}
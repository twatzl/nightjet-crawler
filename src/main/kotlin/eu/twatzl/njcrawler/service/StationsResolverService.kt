package eu.twatzl.njcrawler.service

import eu.twatzl.njcrawler.apiclients.OEBBAccessTokenClient
import eu.twatzl.njcrawler.apiclients.OEBBStationClient
import eu.twatzl.njcrawler.model.StationQueryResult

/**
 * takes in station names, returns a list of possible stations with their hafas ids
 */
class StationsResolverService(
    private val tokenClient: OEBBAccessTokenClient,
    private val stationsClient: OEBBStationClient,
) {
    suspend fun getStationByName(name: String): List<String>? {
        val token = tokenClient.getToken()

        return stationsClient.getByName(token.access_token, name)
            ?.filterNotNull()
            ?.map { it ->
                val stationName = if (it.name.isEmpty()) it.meta else it.name
                "$stationName: ${it.number}"
            }
    }

    suspend fun getStationsBulk(stationsToCrawl: List<String>): List<StationQueryResult> {
        val token = tokenClient.getToken()

        return stationsToCrawl.map { name ->
            val queryResult = stationsClient.getByName(token.access_token, name)
                ?.filterNotNull()
                ?.map {
                    val stationName = it.name.ifEmpty { it.meta }
                    "$stationName: ${it.number}"
                }

            StationQueryResult(name, queryResult)
        }
    }
}
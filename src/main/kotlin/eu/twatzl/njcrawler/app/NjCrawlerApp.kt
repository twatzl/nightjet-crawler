package eu.twatzl.njcrawler.app

import eu.twatzl.njcrawler.apiclients.OEBBAccessTokenClient
import eu.twatzl.njcrawler.apiclients.OEBBNightjetBookingClient
import eu.twatzl.njcrawler.apiclients.OEBBStationClient
import eu.twatzl.njcrawler.data.allNightjets
import eu.twatzl.njcrawler.service.NightjetCrawlerService
import eu.twatzl.njcrawler.service.NightjetPersistenceService
import eu.twatzl.njcrawler.service.StationsResolverService
import eu.twatzl.njcrawler.util.getCurrentTime
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

// configuration

val requestedTrains = allNightjets
val writeCSVPerTrain = true
val writeOccupationCSV = true
val writePricingCSV = true

suspend fun main() {
    val httpClient = setupHttpClient()

//    getHafasIdForSingleStation(httpClient)
//    getHafasIdsForStationList(httpClient)

    getDataForNightjetsAndWriteToCsvFiles(httpClient)

    httpClient.close()
}

private suspend fun getHafasIdForSingleStation(httpClient: HttpClient) {
    val accessTokenClient = OEBBAccessTokenClient(httpClient)
    val stationClient = OEBBStationClient(httpClient)
    val stationsResolverService = StationsResolverService(
        accessTokenClient,
        stationClient,
    )

    val stationName = "Attnang"
    val result = stationsResolverService.getStationByName(stationName)
    println(result)
}

private suspend fun getHafasIdsForStationList(httpClient: HttpClient) {
    val accessTokenClient = OEBBAccessTokenClient(httpClient)
    val stationClient = OEBBStationClient(httpClient)
    val stationsResolverService = StationsResolverService(
        accessTokenClient,
        stationClient,
    )

    val stationNames = listOf(
        "Attnang",
        "Meidling",
        "Triest",
        "Bruxelles",
        "Bozen"
    )
    val result = stationsResolverService.getStationsBulk(stationNames)
    println(result)
}

suspend fun getDataForNightjetsAndWriteToCsvFiles(httpClient: HttpClient) {
    // configuration
    val trains = allNightjets
    val startTime = getCurrentTime()
    val totalTrainsRequested = 21 // must be divisible by 3

    // define services
    val bookingClient = OEBBNightjetBookingClient(httpClient)
    val nightjectCrawlerService = NightjetCrawlerService(bookingClient)
    val nightjetPersistenceService = NightjetPersistenceService()

    val connections =
        nightjectCrawlerService.requestNightjetData(trains, totalTrainsRequested = totalTrainsRequested, startTime)

    connections.map { entry ->
        nightjetPersistenceService.writeNightjetOffersForTrainToCSV(entry.key, entry.value)
    }

    nightjetPersistenceService.writeCombinedNightjetOccupationCsv(connections)
    println("finished")
}

private fun setupHttpClient() = HttpClient(CIO) {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.NONE
        // level = LogLevel.INFO
        // level = LogLevel.ALL // enable for full debug output
        sanitizeHeader { header -> header == HttpHeaders.Authorization || header == "accesstoken" }
    }
    install(ContentNegotiation) {
        json()
    }
}




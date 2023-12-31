package eu.twatzl.njcrawler.app

import eu.twatzl.njcrawler.apiclients.EuropeanSleeperClient
import eu.twatzl.njcrawler.apiclients.OEBBAccessTokenClient
import eu.twatzl.njcrawler.apiclients.OEBBNightjetBookingClient
import eu.twatzl.njcrawler.apiclients.OEBBStationClient
import eu.twatzl.njcrawler.data.allEuropeanSleepers
import eu.twatzl.njcrawler.data.allNightjets
import eu.twatzl.njcrawler.service.*
import eu.twatzl.njcrawler.util.getCurrentTime
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

// configuration
val writeCSVPerTrain = true
val writeOccupationCSV = true
val writePricingCSV = true

// TODO revert to 21 after testing
const val totalTrainsRequested = 3 // must be divisible by 3 for NJ API

suspend fun main() {
    val httpClient = setupHttpClient()

//    getHafasIdForSingleStation(httpClient)
//    getHafasIdsForStationList(httpClient)

    getDataForNightjetsAndWriteToCsvFiles(httpClient)
    getDataForESAndWriteToCsvFiles(httpClient)

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

suspend fun getDataForESAndWriteToCsvFiles(httpClient: HttpClient) {
    // define services
    val bookingClient = EuropeanSleeperClient(httpClient)
    val esCrawlerService = ESCrawlerService(bookingClient)
    val esPersistenceService = ESPersistenceService()

    val connections = esCrawlerService.requestData(allEuropeanSleepers, totalTrainsRequested, getCurrentTime())

    connections.forEach { (train, connections) ->
        esPersistenceService.writeESOffersForTrainToCSV(train, connections)
    }

    esPersistenceService.writeCombinedESOccupationCsv(connections)
    println("finished ES ✔")
}

suspend fun getDataForNightjetsAndWriteToCsvFiles(httpClient: HttpClient) {
    // define services
    val bookingClient = OEBBNightjetBookingClient(httpClient)
    val nightjectCrawlerService = NightjetCrawlerService(bookingClient)
    val nightjetPersistenceService = NightjetPersistenceService()

    val connections =
        nightjectCrawlerService.requestNightjetData(allNightjets, totalTrainsRequested, getCurrentTime())

    connections.map { entry ->
        nightjetPersistenceService.writeNightjetOffersForTrainToCSV(entry.key, entry.value)
    }

    nightjetPersistenceService.writeCombinedNightjetOccupationCsv(connections)
    println("finished NJ ✔")
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




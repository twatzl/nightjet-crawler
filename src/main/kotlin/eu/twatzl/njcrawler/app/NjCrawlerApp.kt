package eu.twatzl.njcrawler.app

import eu.twatzl.njcrawler.apiclients.EuropeanSleeperClient
import eu.twatzl.njcrawler.apiclients.OEBBAccessTokenClient
import eu.twatzl.njcrawler.apiclients.OEBBNightjetBookingClient
import eu.twatzl.njcrawler.apiclients.OEBBStationClient
import eu.twatzl.njcrawler.data.allEuropeanSleepers
import eu.twatzl.njcrawler.data.allNightjets
import eu.twatzl.njcrawler.model.SimplifiedConnection
import eu.twatzl.njcrawler.model.TrainConnection
import eu.twatzl.njcrawler.service.ESCrawlerService
import eu.twatzl.njcrawler.service.NightjetCrawlerService
import eu.twatzl.njcrawler.service.PersistenceService
import eu.twatzl.njcrawler.service.StationsResolverService
import eu.twatzl.njcrawler.util.getCurrentTime
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

// configuration
const val writeCSVPerTrain = true
const val writeOccupationCSV = true
// const val writePricingCSV = true

const val totalTrainsRequested = 21 // must be divisible by 3 for NJ API

suspend fun main() {
    val httpClient = setupHttpClient()
    val persistenceService = PersistenceService()

//    getHafasIdForSingleStation(httpClient)
//    getHafasIdsForStationList(httpClient)

    val njConnections = getDataForNightjets(httpClient)
    val esConnections = getDataForES(httpClient)

    // combine connections of different operators
    val allConnections = njConnections + esConnections

    if (writeCSVPerTrain) {
        allConnections.forEach { (train, connections) ->
            persistenceService.writeOffersForTrainToCSV(train, connections)
        }
    }

    if (writeOccupationCSV) {
        persistenceService.writeCombinedOccupationCsv(allConnections)
    }

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

suspend fun getDataForES(httpClient: HttpClient): Map<TrainConnection, List<SimplifiedConnection>> {
    // define services
    val bookingClient = EuropeanSleeperClient(httpClient)
    val esCrawlerService = ESCrawlerService(bookingClient)

    return esCrawlerService.requestData(allEuropeanSleepers, totalTrainsRequested, getCurrentTime())
}

suspend fun getDataForNightjets(httpClient: HttpClient): Map<TrainConnection, List<SimplifiedConnection>> {
    // define services
    val bookingClient = OEBBNightjetBookingClient(httpClient)
    val nightjetCrawlerService = NightjetCrawlerService(bookingClient)

    return nightjetCrawlerService.requestData(allNightjets, totalTrainsRequested, getCurrentTime())
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
        json(Json {
            // add more parameters from DefaultJson if needed
            encodeDefaults = true
            ignoreUnknownKeys = true
        })
    }
}




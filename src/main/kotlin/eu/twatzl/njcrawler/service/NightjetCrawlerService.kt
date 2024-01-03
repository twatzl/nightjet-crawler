package eu.twatzl.njcrawler.service

import eu.twatzl.njcrawler.apiclients.OEBBNightjetBookingClient
import eu.twatzl.njcrawler.model.SimplifiedConnection
import eu.twatzl.njcrawler.model.Station
import eu.twatzl.njcrawler.model.TrainConnection
import eu.twatzl.njcrawler.util.getCurrentTime
import eu.twatzl.njcrawler.util.getTimezone
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.plus

class NightjetCrawlerService(
    private val nightjetService: OEBBNightjetBookingClient,
) {
    suspend fun requestData(
        connectionSearchList: List<TrainConnection>,
        totalTrainsRequested: Int = 21,
        startTime: Instant = getCurrentTime(),
    ): Map<TrainConnection, List<SimplifiedConnection>> {
        val trainsPerRequest = 3 // 3 is what is on the website, it seems it does not work with more than 3
        val numRequests = totalTrainsRequested / trainsPerRequest

        val offers = mutableMapOf<TrainConnection, List<SimplifiedConnection>>()

        connectionSearchList.forEachIndexed { idx, nj ->
            val trainId = nj.trainId
            val fromStation = nj.fromStation
            val toStation = nj.toStation
            println("starting requesting offers for $trainId")

            val offersForTrain =
                requestOffers(trainId, fromStation, toStation, startTime, numRequests, trainsPerRequest)
                    .distinctBy { it.departure }
                    .sortedBy { it.departure }
            offers[nj] = offersForTrain

            println("(${idx + 1}/${connectionSearchList.size}) Train $trainId done ✔")
        }
        return offers
    }

    private suspend fun requestOffers(
        trainId: String,
        fromStation: Station,
        toStation: Station,
        startTime: Instant,
        numRequests: Int,
        trainsPerRequest: Int,
    ): List<SimplifiedConnection> {
        var time = startTime
        val maxRequest = 3

        val offers = mutableListOf<SimplifiedConnection>()

        // TODO: not every train runs every day, so with the overlap we get duplicate data. add that feature
        repeat(numRequests) {
            offers.addAll(callNightjetApiSafe(trainId, fromStation, toStation, time, maxRequest))
            time = time.plus(
                trainsPerRequest.toLong(), DateTimeUnit.DAY,
                getTimezone()
            )
        }
        return offers.distinctBy { it.departure }
    }

    private fun getTimeoutErrorOffer(
        trainId: String,
        fromStation: Station,
        toStation: Station,
        startTime: Instant,
    ): SimplifiedConnection {
        return SimplifiedConnection.errorOffer(
            trainId,
            fromStation,
            toStation,
            startTime
        )
    }

    private suspend fun callNightjetApiSafe(
        trainId: String,
        fromStation: Station,
        toStation: Station,
        startTime: Instant,
        maxRequest: Int,
    ): List<SimplifiedConnection> {
        val timestamp = startTime.epochSeconds * 1000
        val offers = mutableListOf<SimplifiedConnection>()

        val result = runCatching {
            nightjetService.getOffer(
                trainId,
                fromStation.id,
                toStation.id,
                timestamp,
                numberResults = maxRequest
            )
                .filterNotNull()
                .map { it.toSimplified(trainId, fromStation, toStation, getCurrentTime()) }
        }

        result.onSuccess {
            offers.addAll(it)
            if (it.isEmpty()) {
                println("$trainId ${fromStation.name} - ${toStation.name}: no connections for $startTime")
            } else {
                println("$trainId: request ok for $startTime")
            }
        }

        result.onFailure {
            println("$trainId ${fromStation.name} - ${toStation.name}: timeout for connections from $startTime")
            println(it.cause)
            println(it.message)

            // on failure, we add error offers to indicate in the final csv that a timeout occurred
            repeat(maxRequest) { count ->
                val errorTime = startTime.plus(count, DateTimeUnit.DAY, getTimezone())
                offers.add(
                    SimplifiedConnection.errorOffer(
                        trainId,
                        fromStation,
                        toStation,
                        errorTime,
                    )
                )
            }
        }

        return offers
    }
}
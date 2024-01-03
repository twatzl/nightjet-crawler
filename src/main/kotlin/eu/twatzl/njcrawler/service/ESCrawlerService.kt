package eu.twatzl.njcrawler.service

import eu.twatzl.njcrawler.apiclients.EuropeanSleeperClient
import eu.twatzl.njcrawler.model.SimplifiedConnection
import eu.twatzl.njcrawler.model.Station
import eu.twatzl.njcrawler.model.TrainConnection
import eu.twatzl.njcrawler.util.getCurrentTime
import eu.twatzl.njcrawler.util.getTimezone
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class ESCrawlerService(
    private val bookingClient: EuropeanSleeperClient
) {
    suspend fun requestData(
        trains: List<TrainConnection>,
        totalTrainsRequested: Int,
        startTime: Instant,
    ): Map<TrainConnection, List<SimplifiedConnection>> {
        val offers = mutableMapOf<TrainConnection, List<SimplifiedConnection>>()

        trains.forEachIndexed { idx, train ->
            val trainId = train.trainId
            val fromStation = train.fromStation
            val toStation = train.toStation
            println("starting requesting offers for $trainId")

            offers[train] =
                requestOffers(trainId, fromStation, toStation, startTime, totalTrainsRequested)
                    .distinctBy { it.departure }
                    .sortedBy { it.departure }

            println("(${idx + 1}/${trains.size}) Train $trainId done ✔")
        }

        return offers
    }

    private suspend fun requestOffers(
        trainId: String,
        fromStation: Station,
        toStation: Station,
        startTime: Instant,
        totalTrainsRequested: Int,
    ): List<SimplifiedConnection> {
        var time = startTime

        val offers = mutableListOf<SimplifiedConnection>()

        repeat(totalTrainsRequested) { _ ->
            offers.addAll(callESApiSafe(trainId, fromStation, toStation, time))
            time = time.plus(1, DateTimeUnit.DAY, getTimezone())
        }

        return offers.distinctBy { it.departure }
    }

    private suspend fun callESApiSafe(
        trainId: String,
        fromStation: Station,
        toStation: Station,
        startTime: Instant,
        maxRequest: Int = 3,
    ): List<SimplifiedConnection> {
        val trainNumber = trainId.substring(2) // remove prefix "ES " for API request
        val travelDate = startTime.toLocalDateTime(getTimezone())
        val offers = mutableListOf<SimplifiedConnection>()

        val result = runCatching {
            bookingClient.getOffer(
                trainNumber,
                fromStation.id,
                toStation.id,
                travelDate,
            )?.toSimplified(trainId, getCurrentTime())
        }

        result.onSuccess {
            if (it != null) {
                offers.add(it)
                println("$trainId: request ok for $startTime")
            } else {
                println("$trainId: no connections for $startTime")
            }
        }

        result.onFailure {
            println("$trainId ${fromStation.name} - ${toStation.name}: error fetchting connections from $startTime")
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
                        errorTime
                    )
                )
            }
        }

        return offers
    }
}
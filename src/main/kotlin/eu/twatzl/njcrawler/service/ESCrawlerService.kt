package eu.twatzl.njcrawler.service

import eu.twatzl.njcrawler.apiclients.EuropeanSleeperClient
import eu.twatzl.njcrawler.model.Station
import eu.twatzl.njcrawler.model.TrainConnection
import eu.twatzl.njcrawler.model.es.*
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
    ): MutableMap<TrainConnection, List<ESConnectionWithMetadata>> {
        val offers = mutableMapOf<TrainConnection, List<ESConnectionWithMetadata>>()

        trains.forEachIndexed { idx, train ->
            val trainNumber = train.trainId
            val fromStation = train.fromStation
            val toStation = train.toStation
            println("starting requesting offers for ES $trainNumber")

            offers[train] =
                requestOffers(trainNumber, fromStation, toStation, startTime, totalTrainsRequested)
                    .distinctBy { it.availability.departureTime }
                    .sortedBy { it.availability.departureTime }

            println("(${idx + 1}/${trains.size}) Train ES $trainNumber done ✔")
        }

        return offers
    }

    private suspend fun requestOffers(
        trainNumber: String,
        fromStation: Station,
        toStation: Station,
        startTime: Instant,
        totalTrainsRequested: Int,
    ): MutableList<ESConnectionWithMetadata> {
        var time = startTime

        val offers = mutableListOf<ESConnectionWithMetadata>()

        repeat(totalTrainsRequested) { _ ->
            offers.addAll(callESApiSafe(trainNumber, fromStation, toStation, time))
            time = time.plus(1, DateTimeUnit.DAY, getTimezone())
        }

        return offers.distinctBy { it.availability.departureTime }.toMutableList()
    }

    private suspend fun callESApiSafe(
        trainNumber: String,
        fromStation: Station,
        toStation: Station,
        startTime: Instant,
        maxRequest: Int = 3,
    ): MutableList<ESConnectionWithMetadata> {
        val trainId = "ES $trainNumber"
        val travelDate = startTime.toLocalDateTime(getTimezone())
        val offers = mutableListOf<ESConnectionWithMetadata>()

        val result = runCatching {
            bookingClient.getOffer(
                trainNumber,
                fromStation.id,
                toStation.id,
                travelDate,
            )?.addMetadata(trainId, getCurrentTime())
        }

        result.onSuccess {
            if (it != null) {
                offers.add(it)
                println("$trainId: request ok for $startTime")
            } else {
                println("$trainId ${fromStation.name} - ${toStation.name}: no connections for $startTime")
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
                    getTimeoutErrorOffer(
                        trainId,
                        fromStation,
                        toStation,
                        errorTime,
                        it
                    )
                )
            }
        }

        return offers
    }

    /**
     * currently the best way to make timeouts visible in the resulting csv is to create an offer that contains
     * the text 'timeout'
     */
    private fun getTimeoutErrorOffer(
        trainId: String,
        fromStation: Station,
        toStation: Station,
        errorTime: Instant,
        throwable: Throwable,
    ): ESConnectionWithMetadata {
        val message = throwable.message ?: "no message"

        return ESConnectionWithMetadata(
            trainId,
            Availability(
                "",
                "",
                "",
                fromStation.id,
                errorTime.toLocalDateTime(getTimezone()),
                "",
                toStation.id,
                errorTime.plus(1, DateTimeUnit.DAY, getTimezone()).toLocalDateTime(getTimezone()),
                arrayOf(PriceClass(
                    "timeout",
                    0,
                    0,
                    0,
                    arrayOf(FareType(message, false, 0, 0)),
                    false
                )),
                emptyArray(),
            ),
            getCurrentTime()
        )
    }
}
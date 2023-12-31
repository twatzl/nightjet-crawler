package eu.twatzl.njcrawler.service

import eu.twatzl.njcrawler.model.TrainConnection
import eu.twatzl.njcrawler.model.es.ESConnectionSimplified
import eu.twatzl.njcrawler.model.es.ESConnectionWithMetadata
import eu.twatzl.njcrawler.model.es.toSimplified
import eu.twatzl.njcrawler.util.getCurrentTime
import eu.twatzl.njcrawler.util.getFormattedDate
import eu.twatzl.njcrawler.util.getFormattedTime
import eu.twatzl.njcrawler.util.getNextDay
import java.io.FileOutputStream
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

class ESPersistenceService {
    private val csvService = ESCSVService()
    private val currentTime = getCurrentTime()
    private val formattedDate = getFormattedDate(currentTime)  // ISO datestamp (e.g. "2023-10-17")
    private val formattedTime = getFormattedTime(currentTime)  // timestamp (e.g. "2023-10-17_21-15" for time 21:15)

    /**
     * creates a separate CSV file for each train number
     */
    fun writeESOffersForTrainToCSV(train: TrainConnection, connections: List<ESConnectionWithMetadata>) {
        val outDir = Path(".").resolve("data").resolve(formattedDate).resolve("offers")
        if (!outDir.exists()) {
            outDir.createDirectories()
        }
        val file = outDir.resolve("${formattedTime}_${train.trainId}.csv").toFile()
        val fos = FileOutputStream(file).apply { csvService.writeCsv(this, connections) }
        fos.flush()
        fos.close()
    }

    fun writeCombinedNightjetOccupationCsv(connections: MutableMap<TrainConnection, List<ESConnectionWithMetadata>>) {
        connections.filter { it.value.isEmpty() }
            .forEach { (train, _) ->
                println("No connections found for train ${train.trainId} ${train.fromStation.name} - ${train.toStation.name}")
            }

        val simplifiedConnections = connections
            .filterNot { it.value.isEmpty() }
            .map { (train, connections) ->
                train.trainId to connections.map { it.toSimplified() }
            }.toMap()

        writeCombinedESOccupationCsvInternal(simplifiedConnections)
    }

    private fun writeCombinedESOccupationCsvInternal(
        connections: Map<String, List<ESConnectionSimplified>>,
        timestamp: String = formattedTime,
        date: String = formattedDate,
    ) {
        val departureDates = connections.values.map { connectionList ->
            val departureTimes = connectionList.map { it.departure }
            Pair(departureTimes.minOf { it }, departureTimes.maxOf { it })
        }

        val firstDepartureDate = departureDates.minOf { it.first }
        val lastDepartureDate = departureDates.maxOf { it.second }

        val trains = connections.keys.sorted()
        val origins = trains.map { connections[it]?.first()?.departureStationName }
        val destinations = trains.map { connections[it]?.first()?.arrivalStationName }
        val outDir = Path(".").resolve("data").resolve(date).resolve("combined")
        if (!outDir.exists()) {
            outDir.createDirectories()
        }
        val path = outDir.resolve("${timestamp}_combined_occupation.csv")
        val writer = path.toFile().bufferedWriter()
        writer.write("Train, ${trains.joinToString { it }}")
        writer.newLine()
        writer.write("Origin, ${origins.joinToString { it ?: "" }}")
        writer.newLine()
        writer.write("Destination, ${destinations.joinToString { it ?: "" }}")
        writer.newLine()

        var curDate = firstDepartureDate
        while (curDate <= lastDepartureDate) {
            val formattedDate = getFormattedDate(curDate)  // do not include departure time, just date
            val offers = trains.map { train ->
                val conn = connections[train]?.find {
                    getFormattedDate(it.departure) == formattedDate  // compare based on date, not exact timestamp
                }

                if (conn != null) {
                    if (conn.sleeperOffer != null) {
                        "sleeper"
                    } else if (conn.couchetteOffer != null) {
                        "couchette"
                    } else if (conn.seatingOffer != null) {
                        "seat"
                    } else {
                        "x"
                    }
                } else {
                    ""
                }
            }
            writer.write("$formattedDate, ")
            writer.write(offers.joinToString { it })
            writer.newLine()

            curDate = getNextDay(curDate)
        }

        writer.flush()
        writer.close()
    }
}

package eu.twatzl.njcrawler.service

import eu.twatzl.njcrawler.model.SimplifiedConnection
import eu.twatzl.njcrawler.model.TrainConnection
import eu.twatzl.njcrawler.util.getCurrentTime
import eu.twatzl.njcrawler.util.getFormattedDate
import eu.twatzl.njcrawler.util.getFormattedTime
import eu.twatzl.njcrawler.util.getTimezone
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
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
    fun writeESOffersForTrainToCSV(train: TrainConnection, connections: List<SimplifiedConnection>) {
        val outDir = Path(".").resolve("data").resolve(formattedDate).resolve("offers")
        if (!outDir.exists()) {
            outDir.createDirectories()
        }
        val file = outDir.resolve("${formattedTime}_${train.trainId}.csv").toFile()
        val fos = FileOutputStream(file).apply { csvService.writeCsv(this, connections) }
        fos.flush()
        fos.close()
    }

    fun writeCombinedESOccupationCsv(
        connections: Map<TrainConnection, List<SimplifiedConnection>>,
        timestamp: String = formattedTime,
        date: String = formattedDate,
    ) {
        val departureDates = connections.values.map { connectionList ->
            val departureTimes = connectionList.map { it.departure }
            Pair(departureTimes.minOf { it }, departureTimes.maxOf { it })
        }
        val filteredConnections = filterConnections(connections)

        val firstDepartureDate = departureDates.minOf { it.first }
        val lastDepartureDate = departureDates.maxOf { it.second }

        val trains = filteredConnections.keys.sorted()
        val origins = trains.map { filteredConnections[it]?.first()?.departureStationName }
        val destinations = trains.map { filteredConnections[it]?.first()?.arrivalStationName }
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
                val conn = filteredConnections[train]?.find {
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

            curDate = curDate.plus(1, DateTimeUnit.DAY, getTimezone())
        }

        writer.flush()
        writer.close()
    }


    /**
     * remove trains without connections from map and print corresponding message
     * also reduces the map key to the trainId
     */
    private fun filterConnections(connections: Map<TrainConnection, List<SimplifiedConnection>>): MutableMap<String, List<SimplifiedConnection>> {
        connections.filter { it.value.isEmpty() }
            .forEach { (train, _) ->
                println("No connections found for train ${train.trainId} ${train.fromStation.name} - ${train.toStation.name}")
            }

        return connections.filterNot { it.value.isEmpty() }.map { it.key.trainId to it.value }.toMap().toMutableMap()
    }
}

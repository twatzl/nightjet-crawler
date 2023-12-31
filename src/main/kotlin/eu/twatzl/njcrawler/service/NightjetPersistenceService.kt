package eu.twatzl.njcrawler.service

import eu.twatzl.njcrawler.model.oebb.NightjetConnectionSimplified
import eu.twatzl.njcrawler.model.oebb.NightjetConnectionWithMetadata
import eu.twatzl.njcrawler.model.TrainConnection
import eu.twatzl.njcrawler.model.oebb.toSimplified
import eu.twatzl.njcrawler.util.getCurrentTime
import eu.twatzl.njcrawler.util.getFormattedDate
import eu.twatzl.njcrawler.util.getFormattedTime
import eu.twatzl.njcrawler.util.getTimezone
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import java.io.FileOutputStream
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries

class NightjetPersistenceService {
    private val csvService = NightjetCSVService()
    private val currentTime = getCurrentTime()
    private val formattedDate = getFormattedDate(currentTime)  // ISO datestamp (e.g. "2023-10-17")
    private val formattedTime = getFormattedTime(currentTime)  // timestamp (e.g. "2023-10-17_21-15" for time 21:15)

    fun writeNightjetOffersForTrainToCSV(train: TrainConnection, connections: List<NightjetConnectionWithMetadata>) {
        val outDir = Path(".").resolve("data").resolve(formattedDate).resolve("offers")
        if (!outDir.exists()) {
            outDir.createDirectories()
        }
        val file = outDir.resolve("${formattedTime}_${train.trainId}.csv").toFile()
        val fos = FileOutputStream(file).apply { csvService.writeCsv(this, connections) }
        fos.flush()
        fos.close()
    }

    /**
     * loads connection files and combines them to a single csv file
     */
    fun loadAndCombineNightjetOccupationData(
        dataPath: Path,
        timestamp: String = formattedTime,  // unless specified otherwise, use this classes timestamp
        datestamp: String = formattedDate
    ) {
        val csvFiles = dataPath.listDirectoryEntries("$timestamp*.csv")

        val connections = mutableMapOf<String, List<NightjetConnectionSimplified>>()

        csvFiles.forEach { csv ->
            val trainConnections = csvService.readCsv(csv)
            if (trainConnections.isNotEmpty()) {
                connections[trainConnections.first().trainId] = trainConnections
            } else {
                println("no connections found in file: ${csv.fileName}")
            }
        }

        writeCombinedNightjetOccupationCsvInternal(connections, timestamp, datestamp)
    }

    fun writeCombinedNightjetOccupationCsv(
        connections: MutableMap<TrainConnection, List<NightjetConnectionWithMetadata>>,
    ) {
        connections.filter { it.value.isEmpty() }
            .forEach { println("No connections found for connection ${it.key.trainId} ${it.key.fromStation.name} - ${it.key.toStation.name}") }

        val simplifiedConnections = connections
            .filterNot { it.value.isEmpty() }
            .map { entry ->
                entry.key.trainId to entry.value.map { it.toSimplified() }
            }.toMap()

        writeCombinedNightjetOccupationCsvInternal(simplifiedConnections)
    }

    private fun writeCombinedNightjetOccupationCsvInternal(
        connections: Map<String, List<NightjetConnectionSimplified>>,
        timestamp: String = formattedTime,
        date: String = formattedDate,
    ) {
        val departureDates = connections.values.map { connectionList ->
            val departureTimes = connectionList.map { it.departure }
            Pair(departureTimes.minOf { it }, departureTimes.maxOf { it })
        }

        val firstDepartureDate = departureDates.minOf { it.first }
        val lastDepartureDate = departureDates.maxOf { it.second }
            .plus(1, DateTimeUnit.DAY, getTimezone())

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
        while (curDate < lastDepartureDate) {
            val date = getFormattedDate(curDate)  // do not include departure time, just date
            val offers = trains.map { train ->
                val conn = connections[train]?.find {
                    getFormattedDate(it.departure) == date  // compare based on date, not exact timestamp
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
            writer.write("$date, ")
            writer.write(offers.joinToString { it })
            writer.newLine()

            curDate = curDate.plus(1, DateTimeUnit.DAY, getTimezone())
        }

        writer.flush()
        writer.close()
    }
}

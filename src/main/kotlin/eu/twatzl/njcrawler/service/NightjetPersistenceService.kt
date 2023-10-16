package eu.twatzl.njcrawler.service

import eu.twatzl.njcrawler.model.NightjetConnectionSimplified
import eu.twatzl.njcrawler.model.NightjetConnectionWithMetadata
import eu.twatzl.njcrawler.model.TrainConnection
import eu.twatzl.njcrawler.model.toSimplified
import eu.twatzl.njcrawler.util.getCurrentTime
import eu.twatzl.njcrawler.util.getFormattedDate
import eu.twatzl.njcrawler.util.getFormattedTime
import eu.twatzl.njcrawler.util.getTimezone
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import kotlinx.datetime.toDatePeriod
import kotlinx.datetime.toInstant
import java.io.FileOutputStream
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries

class NightjetPersistenceService {
    private val csvService = NightjetCSVService()

    fun writeNightjetOffersForTrainToCSV(train: TrainConnection, connections: List<NightjetConnectionWithMetadata>) {
        val t = getCurrentTime()
        val formattedDate = getFormattedDate(t)
        val formattedTime = getFormattedTime(t)
        // assemble path append ISO datestamp (e.g. 2023-10-17)
        val outDir = Paths.get(".").resolve("data").resolve(formattedDate).resolve("offers")
        // create output directory if not exists
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
        date: String,
    ) {
        val csvFiles = dataPath.listDirectoryEntries("*.csv")

        val connections = mutableMapOf<String, List<NightjetConnectionSimplified>>()

        csvFiles.forEach { csv ->
            val trainConnections = csvService.readCsv(csv)
            if (trainConnections.isNotEmpty()) {
                connections[trainConnections.first().trainId] = trainConnections
            } else {
                println("no connections found in file: ${csv.fileName}")
            }
        }

        writeCombinedNightjetOccupationCsvInternal(connections, date)
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

        writeCombinedNightjetOccupationCsvInternal(
            simplifiedConnections,
            getFormattedTime(getCurrentTime()),
        )
    }

    private fun writeCombinedNightjetOccupationCsvInternal(
        connections: Map<String, List<NightjetConnectionSimplified>>,
        date: String,
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
        val t = getCurrentTime()
        val formattedDate = getFormattedDate(t)
        val outDir = Paths.get(".").resolve("data").resolve(formattedDate).resolve("combined")
        // create output directory if not exists
        if (!outDir.exists()) {
            outDir.createDirectories()
        }
        val path = outDir.resolve("combined_occupation_${formattedDate}.csv")
        val writer = path.toFile().bufferedWriter()
        writer.write(trains.joinToString { it })
        writer.newLine()
        writer.write(origins.joinToString { it ?: "" })
        writer.newLine()
        writer.write(destinations.joinToString { it ?: "" })
        writer.newLine()

        var curDate = firstDepartureDate
        while (curDate < lastDepartureDate) {
            val offers = trains.map { train ->
                val conn = connections[train]?.find {
                    it.departure == curDate
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

            writer.write("$curDate, ")
            writer.write(offers.joinToString { it })
            writer.newLine()

            curDate = curDate.plus(1, DateTimeUnit.DAY, getTimezone())
        }

        writer.flush()
        writer.close()
    }
}

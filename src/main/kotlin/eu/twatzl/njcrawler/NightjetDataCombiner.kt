package eu.twatzl.njcrawler

import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

fun main(vararg args: String) {

    val date = "2023-10-10"
    val dataPath = Path(".\\data\\$date")
    val csvFiles = dataPath.listDirectoryEntries("*.csv")

    val connections = mutableMapOf<String, List<OebbNightjetConnectionSimplified>>()

    csvFiles.forEach { csv ->
        val trainConnections = readNightjetCsvFile(csv)
        if (trainConnections.isNotEmpty()) {
            connections[trainConnections.first().trainId] = trainConnections
        } else {
            println("no connections found in file: ${csv.fileName}")
        }
    }

    val departureDates = connections.values.map { connectionList ->
        val departureTimes = connectionList.map { it.departure }
        Pair(departureTimes.minOf { it }, departureTimes.maxOf { it })
    }

    val firstDepartureDate = departureDates.minOf { it.first }.toLocalDate()
    val lastDepartureDate = departureDates.maxOf { it.second }.plusDays(1).toLocalDate()

    val trains = connections.keys.sorted()
    val origins = trains.map { connections[it]?.first()?.departureStationName }
    val destinations = trains.map { connections[it]?.first()?.arrivalStationName }

    val file = File("combined_$date.csv")
    val writer = file.bufferedWriter()


    writer.write(trains.joinToString { it })
    writer.newLine()
    writer.write(origins.joinToString { it ?: "" })
    writer.newLine()
    writer.write(destinations.joinToString { it ?: "" })
    writer.newLine()

    var curDate = firstDepartureDate

    while (curDate.isBefore(lastDepartureDate)) {
        val offers = trains.map { train ->
            val conn = connections[train]?.find { it.departure.toLocalDate().isEqual(curDate) }

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

        curDate = curDate.plusDays(1)
    }

    writer.flush()
    writer.close()
}
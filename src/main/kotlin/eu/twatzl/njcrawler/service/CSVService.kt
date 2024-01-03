package eu.twatzl.njcrawler.service

import eu.twatzl.njcrawler.model.SimplifiedConnection
import eu.twatzl.njcrawler.util.getCurrentTime
import kotlinx.datetime.Instant
import java.io.OutputStream
import java.nio.file.Path

class CSVService {
    fun writeCsv(outputStream: OutputStream, connections: List<SimplifiedConnection>) {
        val writer = outputStream.bufferedWriter()
        writer.write(""""trainId", "departureDate", "departure", "arrival", "departureStation", "arrivalStation", "seatingOffer", "couchetteOffer", "sleeperOffer"""")
        writer.newLine()
        connections.forEach {
            val seatingOffer = it.seatingOffer?.toString() ?: "x"
            val couchetteOffer = it.couchetteOffer?.toString() ?: "x"
            val sleeperOffer = it.sleeperOffer?.toString() ?: "x"

            writer.write(
                "${it.trainId}, ${it.departure}, ${it.departure}, ${it.arrival}, ${it.departureStationName}, ${it.arrivalStationName}, $seatingOffer, $couchetteOffer, $sleeperOffer"
            )
            writer.newLine()
        }
        writer.flush()
    }

    /**
     * converts CSV file to list of SimplifiedConnection
     *
     * line items are:
     * 0: trainId
     * 1: departureDate
     * 2: departure (Instant)
     * 3: arrival (Instant)
     * 4: departureStation (name)
     * 5: arrivalStation (name)
     * 6: seatingOffer (price)
     * 7: couchetteOffer (price)
     * 8: sleeperOffer (price)
     */
    fun readCsv(path: Path): List<SimplifiedConnection> {
        val reader = path.toFile().bufferedReader()
        reader.readLine() // read header
        return reader.lineSequence()
            .filter { it.isNotBlank() }
            .map {
                val splitLine = it.split(',', ignoreCase = false, limit = 9)
                SimplifiedConnection(
                    splitLine[0],
                    splitLine[4],
                    splitLine[5],
                    Instant.parse(splitLine[2].trim()),
                    Instant.parse(splitLine[3].trim()),
                    splitLine[6].toFloatOrNull(),
                    splitLine[7].toFloatOrNull(),
                    splitLine[8].toFloatOrNull(),
                    getCurrentTime(),
                )
            }.toList()
    }
}
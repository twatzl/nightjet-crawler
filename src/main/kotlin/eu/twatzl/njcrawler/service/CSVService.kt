package eu.twatzl.njcrawler.service

import eu.twatzl.njcrawler.model.SimplifiedConnection
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
                    splitLine[8].toFloatOrNull()
                )
            }.toList()
    }
}
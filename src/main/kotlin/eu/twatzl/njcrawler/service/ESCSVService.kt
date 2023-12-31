package eu.twatzl.njcrawler.service

import eu.twatzl.njcrawler.model.es.ESConnectionWithMetadata
import eu.twatzl.njcrawler.model.es.toSimplified
import java.io.FileOutputStream

class ESCSVService {
    fun writeCsv(outputStream: FileOutputStream, connections: List<ESConnectionWithMetadata>) {
        val writer = outputStream.bufferedWriter()
        writer.write(""""trainId", "departureDate", "departure", "arrival", "departureStation", "arrivalStation", "seatingOffer", "couchetteOffer", "sleeperOffer"""")
        writer.newLine()
        connections.forEach {
            val simplified = it.toSimplified()
            val seatingOffer = simplified.seatingOffer?.toString() ?: "x"
            val couchetteOffer = simplified.couchetteOffer?.toString() ?: "x"
            val sleeperOffer = simplified.sleeperOffer?.toString() ?: "x"

            writer.write(
                "${simplified.trainId}, ${simplified.departure}, ${simplified.departure}, ${simplified.arrival}, ${simplified.departureStationName}, ${simplified.arrivalStationName}, $seatingOffer, $couchetteOffer, $sleeperOffer"
            )
            writer.newLine()
        }
        writer.flush()
    }
}

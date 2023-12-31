package eu.twatzl.njcrawler.service

import eu.twatzl.njcrawler.model.es.ESConnectionSimplified
import java.io.FileOutputStream

class ESCSVService {
    fun writeCsv(outputStream: FileOutputStream, connections: List<ESConnectionSimplified>) {
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
}

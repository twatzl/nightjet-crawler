package eu.twatzl.njcrawler.service

import eu.twatzl.njcrawler.data.COUCHETTE_OFFER_KEY
import eu.twatzl.njcrawler.data.SEATING_OFFER_KEY
import eu.twatzl.njcrawler.data.SLEEPER_OFFER_KEY
import eu.twatzl.njcrawler.model.oebb.NightjetConnectionSimplified
import eu.twatzl.njcrawler.model.oebb.NightjetConnectionWithMetadata
import kotlinx.datetime.Instant
import java.io.OutputStream
import java.nio.file.Path

class NightjetCSVService {
    fun writeCsv(outputStream: OutputStream, connections: List<NightjetConnectionWithMetadata>) {
        val writer = outputStream.bufferedWriter()
        writer.write(""""trainId", "departureDate", "departure", "arrival", "departureStation", "arrivalStation", "seatingOffer", "couchetteOffer", "sleeperOffer"""")
        writer.newLine()
        connections.forEach {
            val seatingOffer = it.bestOffers[SEATING_OFFER_KEY]?.price?.toString() ?: "x"
            val couchetteOffer = it.bestOffers[COUCHETTE_OFFER_KEY]?.price?.toString() ?: "x"
            val sleeperOffer = it.bestOffers[SLEEPER_OFFER_KEY]?.price?.toString() ?: "x"

            writer.write(
                "${it.trainId}, ${it.departure}, ${it.departure}, ${it.arrival}, ${it.departureStation.name}, ${it.arrivalStation.name}, $seatingOffer, $couchetteOffer, $sleeperOffer"
            )
            writer.newLine()
        }
        writer.flush()
    }

    fun readCsv(path: Path): List<NightjetConnectionSimplified> {
        val reader = path.toFile().bufferedReader()
        reader.readLine() // read header
        return reader.lineSequence()
            .filter { it.isNotBlank() }
            .map {

                //${it.trainId}, ${it.departure.format(dateFormatter)}, ${it.departure}, ${it.arrival}, ${it.departureStation.name}, ${it.arrivalStation.name}, $seatingOffer, $couchetteOffer, $sleeperOffer
                val splitLine = it.split(',', ignoreCase = false, limit = 9)
                val trainId = splitLine[0]
                val departure = Instant.parse(splitLine[2].trim())
                val arrival = Instant.parse(splitLine[3].trim())
                val departureStationName = splitLine[4]
                val arrivalStationName = splitLine[5]
                val seatingOffer = splitLine[6].toFloatOrNull()
                val couchetteOffer = splitLine[7].toFloatOrNull()
                val sleeperOffer = splitLine[8].toFloatOrNull()
                NightjetConnectionSimplified(
                    trainId,
                    departureStationName,
                    arrivalStationName,
                    departure,
                    arrival,
                    seatingOffer,
                    couchetteOffer,
                    sleeperOffer
                )
            }.toList()
    }

}
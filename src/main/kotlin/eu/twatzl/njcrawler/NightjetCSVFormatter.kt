package eu.twatzl.njcrawler

import java.io.OutputStream
import java.nio.file.Path
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


private val seating = "se"
private val couchette = "le"
private val sleeper = "be"

fun OutputStream.writeCsv(connections: List<OebbNightjetConnectionWithMetadata>) {
    val dateFormatter = DateTimeFormatter.ISO_DATE

    val writer = bufferedWriter()
    writer.write(""""trainId", "departureDate", "departure", "arrival", "departureStation", "arrivalStation", "seatingOffer", "couchetteOffer", "sleeperOffer"""")
    writer.newLine()
    connections.forEach {
        val seatingOffer = it.bestOffers[seating]?.price?.toString() ?: "x"
        val couchetteOffer = it.bestOffers[couchette]?.price?.toString() ?: "x"
        val sleeperOffer = it.bestOffers[sleeper]?.price?.toString() ?: "x"

        writer.write("${it.trainId}, ${it.departure.format(dateFormatter)}, ${it.departure.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)}, ${it.arrival.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)}, ${it.departureStation.name}, ${it.arrivalStation.name}, $seatingOffer, $couchetteOffer, $sleeperOffer")
        writer.newLine()
    }
    writer.flush()
}

fun readNightjetCsvFile(path: Path): List<OebbNightjetConnectionSimplified> {
    val reader = path.toFile().bufferedReader()
    reader.readLine() // read header
    return reader.lineSequence()
        .filter { it.isNotBlank() }
        .map {

            //${it.trainId}, ${it.departure.format(dateFormatter)}, ${it.departure}, ${it.arrival}, ${it.departureStation.name}, ${it.arrivalStation.name}, $seatingOffer, $couchetteOffer, $sleeperOffer
            val splitLine = it.split(',', ignoreCase = false, limit = 9)
            val trainId = splitLine[0]
            val departure = OffsetDateTime.parse(splitLine[2].trim(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            val arrival = OffsetDateTime.parse(splitLine[3].trim(),  DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            val departureStationName = splitLine[4]
            val arrivalStationName = splitLine[5]
            val seatingOffer = splitLine[6].toFloatOrNull()
            val couchetteOffer = splitLine[7].toFloatOrNull()
            val sleeperOffer = splitLine[8].toFloatOrNull()
            OebbNightjetConnectionSimplified(
                trainId,
                departureStationName,
                arrivalStationName,
                departure,
                arrival,
                seatingOffer,
                couchetteOffer,
                sleeperOffer)
        }.toList()
}

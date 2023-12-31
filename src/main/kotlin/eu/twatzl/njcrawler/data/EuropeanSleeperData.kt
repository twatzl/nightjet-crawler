package eu.twatzl.njcrawler.data

import eu.twatzl.njcrawler.model.Station
import eu.twatzl.njcrawler.model.TrainConnection
import eu.twatzl.njcrawler.util.getCurrentTime
import eu.twatzl.njcrawler.util.getTimezone
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime

// station data can be retrieved by opening https://www.europeansleeper.eu/
// and inspecting the source code of the departure or arrival station input field

val esBerlinOst = Station("Berlin Ostbahnhof", "8010110")
val esBruxelles = Station("Brüssel-Süd", "8800104")
val esPraha = Station("Prag hl.n.", "5457076")

// TODO remove conditional assignments some time after March 25/26, 2024

var es452 = if (getCurrentTime().toLocalDateTime(getTimezone()) < LocalDateTime(2024, 3, 26, 0, 0, 0))
    TrainConnection("452", esBerlinOst, esBruxelles) // until March 24, 2024
else TrainConnection("452", esPraha, esBruxelles) // starting from March 26, 2024

val es453 = if (getCurrentTime().toLocalDateTime(getTimezone()) < LocalDateTime(2024, 3, 25, 0, 0, 0))
    TrainConnection("453", esBruxelles, esBerlinOst) // until March 22, 2024
else TrainConnection("453", esBruxelles, esPraha) // starting from March 25, 2024

val allEuropeanSleepers = listOf(es452, es453)
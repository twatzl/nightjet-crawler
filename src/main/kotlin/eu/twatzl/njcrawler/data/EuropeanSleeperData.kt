package eu.twatzl.njcrawler.data

import eu.twatzl.njcrawler.model.Station
import eu.twatzl.njcrawler.model.TrainConnection

// station data can be retrieved by opening https://www.europeansleeper.eu/
// and inspecting the source code of the departure or arrival station input field

val esBerlinOst = Station("Berlin Ostbahnhof", "8010110")
val esBruxelles = Station("Brüssel-Süd", "8800104")
val esPraha = Station("Prag hl.n.", "5457076") // starting from March 25, 2024

val es452 = TrainConnection("452", esBerlinOst, esBruxelles) // until March 24, 2024
// val es452 = TrainConnection("452", esPraha, esBruxelles) // starting from March 26, 2024

val es453 = TrainConnection("453", esBruxelles, esBerlinOst) // until March 22, 2024
// val es453 = TrainConnection("453", esBruxelles, esPraha) // starting from March 25, 2024

val allEuropeanSleepers = listOf(es452, es453)
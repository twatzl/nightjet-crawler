package eu.twatzl.njcrawler

import eu.twatzl.njcrawler.service.NightjetPersistenceService
import kotlin.io.path.Path


/**
 * reads multiple connection csv files and combines them into a single csv file
 */
fun main(vararg args: String) {
    val date = "2023-10-10"
    val dataPath = Path(".\\data\\offers\\$date")
    NightjetPersistenceService().loadAndCombineNightjetOccupationData(dataPath, date)
}


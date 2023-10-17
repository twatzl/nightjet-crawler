package eu.twatzl.njcrawler

import eu.twatzl.njcrawler.service.NightjetPersistenceService
import eu.twatzl.njcrawler.util.getCurrentTime
import eu.twatzl.njcrawler.util.getFormattedDate
import java.nio.file.Paths
import kotlin.io.path.Path


/**
 * reads multiple connection csv files and combines them into a single csv file
 */
fun main(vararg args: String) {
    val formattedDate = getFormattedDate(getCurrentTime())
    val dataPath = Paths.get(".").resolve("data").resolve(formattedDate).resolve("offers")
    NightjetPersistenceService().loadAndCombineNightjetOccupationData(dataPath)
}


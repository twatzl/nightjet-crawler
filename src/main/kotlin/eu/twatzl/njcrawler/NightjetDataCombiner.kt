package eu.twatzl.njcrawler

import eu.twatzl.njcrawler.service.NightjetPersistenceService
import java.nio.file.Paths

/**
 * Reads multiple connection csv files and combines them into a single csv file.
 *
 * This tool is mostly meant for manual use, e.g. for debugging purposes. The NjCrawlerApp main already combines the
 * results by default. Please adjust the date for which you want to combine the csv files before use.
 */
fun main(vararg args: String) {
    val date = "2023-10-17"
    val dataPath = Paths.get(".").resolve("data").resolve(date).resolve("offers")
    NightjetPersistenceService().loadAndCombineNightjetOccupationData(dataPath)
}


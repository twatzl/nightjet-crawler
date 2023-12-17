package eu.twatzl.njcrawler

import eu.twatzl.njcrawler.service.NightjetPersistenceService
import kotlin.io.path.Path

/**
 * Reads multiple connection csv files and combines them into a single csv file.
 *
 * This tool is mostly meant for manual use, e.g. for debugging purposes. The NjCrawlerApp main already combines the
 * results by default. Please adjust the timestamp for which you want to combine the csv files before use.
 */
fun main(vararg args: String) {
    val timestamp = "2023-10-18_00-16"
    val datestamp = timestamp.split('_')[0]
    val dataPath = Path(".").resolve("data").resolve(datestamp).resolve("offers")
    NightjetPersistenceService().loadAndCombineNightjetOccupationData(dataPath, timestamp, datestamp)
}


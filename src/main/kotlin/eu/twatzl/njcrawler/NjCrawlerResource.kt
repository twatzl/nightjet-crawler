package eu.twatzl.njcrawler

import eu.twatzl.njcrawler.data.Station
import eu.twatzl.njcrawler.data.allNightjets
import eu.twatzl.njcrawler.data.kiew
import eu.twatzl.njcrawler.data.wien
import io.smallrye.mutiny.TimeoutException
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.io.File
import java.nio.file.Path as JPath
import java.io.FileOutputStream
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import kotlin.io.path.createDirectories

data class StationQueryResult(val stationName: String, val result: List<String>?)

@Path("/api")
class NjCrawlerResource {

    @RestClient
    lateinit var stationsService: OebbStationsService

    @RestClient
    lateinit var tokenService: OebbAccessTokenService

    @RestClient
    lateinit var nightjetService: OebbNightjetBookingService

    @GET
    @Path("stations")
    @Produces(MediaType.TEXT_PLAIN)
    fun stationsByName(@QueryParam("name") name: String): List<String>? {

        val token = tokenService.getToken()

        return stationsService.getByName(token.access_token, name)
            ?.filterNotNull()
            ?.map { it ->
                val stationName = if (it.name.isEmpty()) it.meta else it.name
                "$stationName: ${it.number}"
            }
    }

    val stationsToCrawl = listOf(
        "Kiew",
    )

    @GET
    @Path("stationsBulk")
    @Produces(MediaType.TEXT_PLAIN)
    fun stationsBulk(): List<StationQueryResult> {
        val token = tokenService.getToken()

        return stationsToCrawl.map { name ->
            val queryResult = stationsService.getByName(token.access_token, name)
                ?.filterNotNull()
                ?.map { it ->
                    val stationName = if (it.name.isEmpty()) it.meta else it.name
                    "$stationName: ${it.number}"
                }

            StationQueryResult(name, queryResult)
        }
    }

    @GET
    @Path("nightjet")
    @Produces(MediaType.TEXT_PLAIN)
    fun nightjet(): File {
        val trainsPerRequest = 3 // 3 is what is on the website, it seems it does not work with more than 3
        val totalTrainsRequested = 45
        val startTime = LocalDateTime.now()
        val numRequests = totalTrainsRequested / trainsPerRequest

        val trainId = "D 40149"
        val fromStation = wien
        val toStation = kiew

        val offers = requestNightjetOffers(trainId, fromStation, toStation, startTime, numRequests, trainsPerRequest)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")
        val file = File("${LocalDateTime.now().format(formatter)}_${trainId}.csv")

        FileOutputStream(file).apply { writeCsv(offers) }

        return file
    }

    @GET
    @Path("nj490")
    @Produces(MediaType.TEXT_PLAIN)
    fun nj490(): String {
        val trainsPerRequest = 3 // 3 is what is on the website, it seems it does not work with more than 3
        val totalTrainsRequested = 90 // must be divisible by trainsPerRequest
        val startTime = LocalDateTime.of(2023,12,1,0,0,0)
        val numRequests = totalTrainsRequested / trainsPerRequest

        val path = JPath.of(".\\data\\${startTime.format(DateTimeFormatter.ISO_DATE)}")
        path.createDirectories()

        val searchList = eu.twatzl.njcrawler.data.nj490

        searchList.forEachIndexed{ idx, nj ->
            val trainId = nj.trainId
            val fromStation = nj.fromStation
            val toStation = nj.toStation

            val offers =
                requestNightjetOffers(trainId, fromStation, toStation, startTime, numRequests, trainsPerRequest)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")
            val file = path.resolve("${LocalDateTime.now().format(formatter)}_${trainId}.csv").toFile()

            val fos = FileOutputStream(file).apply { writeCsv(offers) }
            fos.flush()
            fos.close()

            println("($idx/${searchList.size}) Train $trainId done")
            Thread.sleep(10_000)
        }


        return "ok"
    }

    @GET
    @Path("nightjetBulk")
    @Produces(MediaType.TEXT_PLAIN)
    fun nightjetBulk(): String {
        val trainsPerRequest = 3 // 3 is what is on the website, it seems it does not work with more than 3
        val totalTrainsRequested = 21 // must be divisible by trainsPerRequest
        val startTime = LocalDateTime.now()
        val numRequests = totalTrainsRequested / trainsPerRequest

        val path = JPath.of(".\\data\\${startTime.format(DateTimeFormatter.ISO_DATE)}")
        path.createDirectories()

        val searchList = allNightjets

        searchList.forEachIndexed{ idx, nj ->
            val trainId = nj.trainId
            val fromStation = nj.fromStation
            val toStation = nj.toStation

            val offers =
                requestNightjetOffers(trainId, fromStation, toStation, startTime, numRequests, trainsPerRequest)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")
            val file = path.resolve("${LocalDateTime.now().format(formatter)}_${trainId}.csv").toFile()

            val fos = FileOutputStream(file).apply { writeCsv(offers) }
            fos.flush()
            fos.close()

            println("($idx/${searchList.size}) Train $trainId done")
            Thread.sleep(10_000)
        }


        return "ok"
    }

    private fun requestNightjetOffers(
        trainId: String,
        fromStation: Station,
        toStation: Station,
        startTime: LocalDateTime,
        numRequests: Int,
        trainsPerRequest: Int,
    ): MutableList<OebbNightjetConnectionWithMetadata> {
        var time = startTime
        val maxRequest = 3
        val zoneid = "europe/vienna"

        //val offers = mutableListOf<OebbNightjetConnectionWithMetadata>()
        // TODO: not every train runs every day, so with the overlap we get duplicate data. add that feature
        val offers = try {
            nightjetService.getOffer(
                trainId,
                fromStation.id,
                toStation.id,
                Timestamp.valueOf(time).time,
                numberResults = maxRequest,
            )?.filterNotNull()
                ?.map { it.addMetadata(trainId, fromStation, toStation, ZonedDateTime.now()) }
                ?.toMutableList() ?: mutableListOf()
        } catch (e: TimeoutException) {
            mutableListOf(
                getTimeoutErrorOffer(trainId, fromStation, toStation, startTime, zoneid)
            )
        }

        repeat(numRequests) { requestCount ->
            time = time.plusDays(trainsPerRequest.toLong())
            val timestamp = Timestamp.valueOf(time).time
            try {
                offers.addAll(
                    nightjetService.getOffer(
                        trainId,
                        fromStation.id,
                        toStation.id,
                        timestamp,
                        numberResults = maxRequest
                    )
                        ?.filterNotNull()
                        ?.map { it.addMetadata(trainId, fromStation, toStation, ZonedDateTime.now()) }
                        ?: emptyList())
            } catch (e: TimeoutException) {
                repeat(maxRequest) { count ->
                    offers.add(getTimeoutErrorOffer(trainId, fromStation, toStation, startTime.plusDays(count.toLong()), zoneid))
                }
            }
        }
        return offers
    }

    private fun getTimeoutErrorOffer(
        trainId: String,
        fromStation: Station,
        toStation: Station,
        startTime: LocalDateTime,
        zoneid: String,
    ) = OebbNightjetConnectionWithMetadata(
        trainId,
        fromStation,
        toStation,
        ZonedDateTime.of(startTime, ZoneId.of(zoneid)).toOffsetDateTime(),
        ZonedDateTime.of(startTime, ZoneId.of(zoneid)).plusDays(1).toOffsetDateTime(),
        bestOffers = mapOf(
            "timeout" to OebbNightjetOffer("timeout", 0.0f)
        ),
        ZonedDateTime.now(),
    )
}
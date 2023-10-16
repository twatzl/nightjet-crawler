package eu.twatzl.njcrawler

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import java.sql.Timestamp
import java.time.LocalDateTime
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.QueryParam

@RegisterRestClient
interface OebbNightjetBookingService {

    //destinations/offers/NJ%2040466/1190100/8396008/1680636420000?forward=true&max=3
    @Path("/destinations/offers/{trainId}/{fromStationId}/{toStationId}/{timestamp}")
    @ClientHeaderParam(name = "User-Agent", value = ["Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"])
    @GET
    fun getOffer(
        @PathParam("trainId") trainId: String,
        @PathParam("fromStationId") fromStationId: String,
        @PathParam("toStationId") toStationId: String,
        @PathParam("timestamp") timestamp: Long = Timestamp.valueOf(LocalDateTime.now()).time,
        @QueryParam("max") numberResults: Int = 3,
    ): Set<OebbNightjetConnection?>?

}
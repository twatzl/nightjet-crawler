package eu.twatzl.njcrawler

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("/anonymousToken")
@RegisterRestClient
interface OebbAccessTokenService {
    @GET
    fun getToken(): OebbAccessToken
}
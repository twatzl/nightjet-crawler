package eu.twatzl.njcrawler

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import org.jboss.resteasy.reactive.RestHeader
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.QueryParam

//@ClientHeaderParam(name = "AccessToken", value = ["eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODAyODE3MDQsImlhdCI6MTY4MDI4MTQwNCwiaXNzIjoicmVhbG1zL2N1c3RvbWVyIiwic3ViIjoiYW5vbnltLTY5Zjk2MDZkLTA5NzAtNDMiLCJ0eXAiOiJCZWFyZXIiLCJzZXNzaW9uX3N0YXRlIjoiZmYzZTZhZmEtY2QyOS00YThjLTg0ZmItNzg0ZDczZmUyZjdiIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiYW5vbnltb3VzIn0.tWTexkvl5RrwdfF6jNzjXcrMx1b-mLwau9F64-URAF0ZfDm5F19TKDWTexUt_1soU2jRQqP7uQOLaXKFSUwIFDHdl153abUJBH3f_Q3tcREO2Q1RmXIXpLP80fSKSPTQB8IbVWh0oXblejnjlHy4PjErR7WW7os03BriMi_6-ROTyU48rzsoroDZrrya-sdDiBTdsGCJR3w8ZZ2oN3rKJm-bkBKoUIK_E8n2zRVKgA_2yeO8CpVA7aXZ0j3kfhKxsnWC81clyKqJhCCP1Vya2euCyHZPWXVLV_tM5HSVjsYQXJTrdd8EFaikl9GyGvtkHc_IH6IFW_TDbeXAnazomZofom6fx66mO33TNvIqabiARmtM56tJ-tM__QjqirdHIUy6DImWLKKNm84sl3U7imaA3vpMUpjVd-03EQ8mkdAqj8jA3OFIOupgNV9P3jnS_EjG6IXeK_CYCqcAe0-6-Lj2_wGb1kL2_kkjYmGvUde6PeqqNHTq9pgDnVWgVR8IXacL3dUAtOeD4q_4pQ2SOsRVAQo74S-P9zrSBhnIopCn9Zh0IQpEql0kjEIEKjoJNrm_VLNVo--TQtiOnVgx5-wG6PEGnwlcFFLURPUVFl5XEoVooEokCkti3bMqbT2pbqNu7GdhNPkkrWTSqIFo-Mge1Jy7AE5g9MiqtLDzHNQ"])
@Path("/stations")
@RegisterRestClient
interface OebbStationsService {
    @GET
    fun getByName(
        @RestHeader("AccessToken") accessToken: String,
        @QueryParam("name") name: String,
        @QueryParam("count") count: Int = 15): Set<OebbStation?>?
}
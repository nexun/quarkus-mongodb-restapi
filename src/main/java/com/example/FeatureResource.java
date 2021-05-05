package com.example;

import io.vertx.core.json.JsonArray;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/crashes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FeatureResource {

    public static ArrayList<String> cords = new ArrayList<String>();

    @Inject
    FeatureService featureService;

    @GET
    public List<Features> list() {
        return featureService.list();
    }

    @POST
    @Path("/cords")
    public List<Object> polygon(JsonArray cords){
        return featureService.getPolygon(cords);
    }
   /*{0
:
-42.275390625
1
:
 geometry: {
       $geoWithin: { $polygon: [ [ -42.275390625 , -12.511665400971019 ], [ -42.275390625 , -12.511665400971019 ], [ -42.275390625 ,-12.511665400971019] ] }
     }
-12.511665400971019
     geometry: {
       $geoWithin: { $polygon: [ [ 5.4327392578125 , 47.24194882163242 ], [ 5.4327392578125 , 47.24194882163242 ], [ 5.4327392578125 ,47.24194882163242] ] }
     }
}*/
}
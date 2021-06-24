package com.bd2api;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import io.vertx.core.json.JsonArray;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/accidents")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccidentResource {

    public static ArrayList<Double> cords = new ArrayList<Double>();

    @Inject
    AccidentService accidentService;

    @GET
    @Path("/{start}-{end}")
    public List<Accident> list(@PathParam Long start, @PathParam Long end) {
        return accidentService.list(start, end);
    }

    @GET
    @Path("/analyze/{atribute}/{start}-{end}")
    public List<Object> analyze(@PathParam String atribute, @PathParam Long start, @PathParam Long end) {

        return accidentService.analyze(atribute, start, end);
    }

    @POST
    @Path("/polygon")
    public List<Object> polygon(JsonArray cords) {
        return accidentService.getPolygon(cords);
    }

    @POST
    @Path("/radio")
    public List<Object> radio(JsonArray cords) {
        return accidentService.getRadio(cords);
    }

}
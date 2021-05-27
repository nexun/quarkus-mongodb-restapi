package com.bd2api;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import io.vertx.core.json.JsonArray;

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
    public List<Accident> list() {
        return accidentService.list();
    }

    @GET
    @Path("/analyze")
    public List<Object> analyze() {
        return accidentService.analyze();
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
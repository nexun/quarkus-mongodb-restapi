package com.bd2api;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.bson.Document;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class AccidentService {

    @Inject MongoClient mongoClient;

    public List<Accident> list(){
        List<Accident> list = new ArrayList<>();

        try (MongoCursor<Document> cursor = getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Accident feature = new Accident();
                feature.setData((Document) document);
                list.add(feature);
            }
        }
        return list;
    }


public List<Object> getPolygon (JsonArray cords) {
        /*
         POST REQUEST BODY EXAMPLE
            [
                {"long": -122.66328563750216, "lat":38.332236587402136 },
                {"long":-122.36538569651253, "lat":37.95741805152669 },
                {"long":-121.9878627298206,  "lat":38.35485829578574 },
                {"long":-122.66328563750216, "lat":38.332236587402136 }
            ]
        */
        List<Object> list = new ArrayList<>();
        ArrayList<List<Double>> points = new ArrayList<List<Double>>();

            for (int i = 0; i < cords.size(); i++){
                JsonObject aux = cords.getJsonObject(i);
                points.add(Arrays.asList(aux.getDouble("long"),aux.getDouble("lat")));
            }
            MongoCursor cursor = getCollection().find((Filters.geoWithinPolygon("geometry", points))).iterator(); {
                while (cursor.hasNext()) {
                    Document document = (Document) cursor.next();
                    Accident feature = new Accident();
                    feature.setData(document);
                    list.add(feature);
                }
            }

    return list;


}
    public List<Object> getRadio (JsonArray cords) {
        /*
         POST REQUEST BODY EXAMPLE
         [
            {"long": -82.3508587993436, "lat":  34.6372515048226 , "distanceInRad":0.000007605572237155977 }
         ]
        */
        List<Object> list = new ArrayList<>();
        JsonObject aux = cords.getJsonObject(0);
        double lon = aux.getDouble("long");
        double lat = aux.getDouble("lat");
        double distanceInRad = aux.getDouble("distanceInRad") ;

        MongoCursor cursor =
                getCollection().find(Filters.geoWithinCenterSphere("geometry", lon, lat, distanceInRad)).iterator();

        while (cursor.hasNext()){
            Accident feature = new Accident();
            feature.setData((Document) cursor.next());
            list.add(feature);
        }

        return list;


    }

    private MongoCollection getCollection(){
        return mongoClient.getDatabase("BBDD2").getCollection("accidents");
    }
}
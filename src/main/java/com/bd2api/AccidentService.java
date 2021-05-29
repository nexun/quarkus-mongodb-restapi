package com.bd2api;

import com.mongodb.client.*;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.bson.Document;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class AccidentService {

    @Inject
    MongoClient mongoClient;

    public List<Accident> list() {
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

    public List<Object> getPolygon(JsonArray cords) {
        /*
         * POST REQUEST BODY EXAMPLE [ {"long": -122.66328563750216,
         * "lat":38.332236587402136 }, {"long":-122.36538569651253,
         * "lat":37.95741805152669 }, {"long":-121.9878627298206,
         * "lat":38.35485829578574 }, {"long":-122.66328563750216,
         * "lat":38.332236587402136 } ]
         */
        List<Object> list = new ArrayList<>();
        ArrayList<List<Double>> points = new ArrayList<List<Double>>();

        for (int i = 0; i < cords.size(); i++) {
            JsonObject aux = cords.getJsonObject(i);
            points.add(Arrays.asList(aux.getDouble("long"), aux.getDouble("lat")));
        }
        MongoCursor<Document> cursor = getCollection().find((Filters.geoWithinPolygon("geometry", points))).iterator();
        {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Accident feature = new Accident();
                feature.setData(document);
                list.add(feature);
            }
        }

        return list;

    }

    public List<Object> getRadio(JsonArray cords) {
        /*
         * POST REQUEST BODY EXAMPLE [ {"long": -82.3508587993436, "lat":
         * 34.6372515048226 , "distanceInRad":0.000007605572237155977 } ]
         */
        List<Object> list = new ArrayList<>();
        JsonObject aux = cords.getJsonObject(0);
        double lon = aux.getDouble("long");
        double lat = aux.getDouble("lat");
        double distanceInKm = aux.getDouble("distanceInKm");
        for (Document document : getCollection()
                .find(Filters.geoWithinCenterSphere("geometry", lon, lat, distanceInKm/6378.1))) {
            Accident feature = new Accident();
            feature.setData(document);
            list.add(feature);
        }

        return list;

    }

    /* this method returns the most common accident parameters */
    public List<Object> analyze() {

        List<Object> list = new ArrayList<>();
        MongoCollection<Document> cursor = getCollection();
        // ------------------ filter and convert data -----------------------------
        Document humidity = cursor
                .aggregate(Arrays.asList(Aggregates.group("$Humidity(%)", Accumulators.sum("count", 1)),
                        new Document("$sort", new Document("count", -1)), new Document("$limit", 1)))
                .allowDiskUse(true).iterator().next();
        Document pressure = cursor
                .aggregate(Arrays.asList(Aggregates.group("$Pressure(in)", Accumulators.sum("count", 1)),
                        new Document("$sort", new Document("count", -1)), new Document("$limit", 1)))
                .allowDiskUse(true).iterator().next();
        Document civilTwilight = cursor
                .aggregate(Arrays.asList(Aggregates.group("$Civil_Twilight", Accumulators.sum("count", 1)),
                        new Document("$sort", new Document("count", -1)), new Document("$limit", 1)))
                .allowDiskUse(true).iterator().next();
        Document city = cursor
                .aggregate(Arrays.asList(Aggregates.group("$City", Accumulators.sum("count", 1)),
                        new Document("$sort", new Document("count", -1)), new Document("$limit", 1)))
                .allowDiskUse(true).iterator().next();
        Document street = cursor
                .aggregate(Arrays.asList(Aggregates.group("$Street", Accumulators.sum("count", 1)),
                        new Document("$sort", new Document("count", -1)), new Document("$limit", 1)))
                .allowDiskUse(true).iterator().next();
        Document weatherCondition = cursor
                .aggregate(Arrays.asList(Aggregates.group("$Weather_Condition", Accumulators.sum("count", 1)),
                        new Document("$sort", new Document("count", -1)), new Document("$limit", 1)))
                .allowDiskUse(true).iterator().next();
        // ------------------ create statistics
        Statistic featureHumidity = new Statistic("Humidity(%)", humidity.getString("_id"), humidity.getInteger("count"),
                new Document());
        Statistic featurePressure = new Statistic("Pressure(in)", pressure.getString("_id"), pressure.getInteger("count"),
                new Document());
        Statistic featureCivilTwilight = new Statistic("Civil_Twilight", civilTwilight.getString("_id"),
                civilTwilight.getInteger("count"), new Document());
        Statistic featureCity = new Statistic("City", city.getString("_id"), city.getInteger("count"), new Document());
        Statistic featureStreet = new Statistic("Street", street.getString("_id"), street.getInteger("count"),
                new Document());
        Statistic featureWeatherCondition = new Statistic("Weather_Condition", weatherCondition.getString("_id"), weatherCondition.getInteger("count"),
                new Document());


        // ------------------push accidents --------------------
        list.add(featureHumidity);
        list.add(featurePressure);
        list.add(featureCivilTwilight);
        list.add(featureCity);
        list.add(featureStreet);
        list.add(featureWeatherCondition);

        return list;

    }

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("BBDD2").getCollection("accidents");
    }

}
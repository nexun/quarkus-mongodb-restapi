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

        public List<Accident> list(Long start, Long end) {
                MongoCollection<Document> collection = getCollection();
                List<Accident> list = new ArrayList<>();
                Long cantStart = start;
                Long cantEnd = end - start;
                MongoCursor<Document> cursor = collection.aggregate(
                                Arrays.asList(new Document("$skip", cantStart), new Document("$limit", cantEnd)))
                                .iterator();

                while (cursor.hasNext()) {
                        Document document = cursor.next();
                        Accident feature = new Accident();
                        feature.setData((Document) document);
                        list.add(feature);
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
                MongoCursor<Document> cursor = getCollection().find((Filters.geoWithinPolygon("geometry", points)))
                                .iterator();
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
                                .find(Filters.geoWithinCenterSphere("geometry", lon, lat, distanceInKm / 6378.1))) {
                        Accident feature = new Accident();
                        feature.setData(document);
                        list.add(feature);
                }

                return list;

        }

        public List<Object> analyze(String atribute, Long start, Long end) {
                String atributeGroup = atribute;
                Long cantStart = start;
                Long cantEnd = end - start;
                List<Object> list = new ArrayList<>();
                MongoCollection<Document> collection = getCollection();
                MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(new Document("$skip", cantStart),
                                new Document("$limit", cantEnd),
                                new Document("$match",
                                                new Document(atributeGroup,
                                                                new Document("$not", new Document("$size", 0L)))),
                                new Document("$unwind", new Document("path", "$" + atributeGroup)),
                                new Document("$group",
                                                new Document("_id", new Document("$toLower", "$" + atributeGroup))
                                                                .append("count", new Document("$sum", 1L))),
                                new Document("$match", new Document("count", new Document("$gte", 2L))),
                                new Document("$sort", new Document("count", -1L))

                )).iterator();
                while (cursor.hasNext()) {
                        Document document = cursor.next();
                        Statistic feature = new Statistic(atributeGroup, document.getString("_id"),
                                        document.getLong("count"), new Document());
                        list.add(feature);
                }
                return list;
        }

        private MongoCollection<Document> getCollection() {
                return mongoClient.getDatabase("BBDD2").getCollection("accidents");
        }

}
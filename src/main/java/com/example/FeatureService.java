package com.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.bson.Document;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class FeatureService {

    @Inject MongoClient mongoClient;

    public List<Features> list(){
        List<Features> list = new ArrayList<>();

        try (MongoCursor<Document> cursor = getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Features feature = new Features();
                feature.setData((Document) document);
                list.add(feature);
            }
        }
        return list;
    }


public List<Object> getPolygon (JsonArray cords) {
        List<Object> list = new ArrayList<>();
        ArrayList<List<Double>> points = new ArrayList<List<Double>>();
        for (int i = 0; i < cords.size(); i++){
                JsonObject aux = cords.getJsonObject(i);
                Double lat = new Double(aux.getString("lat"));
                Double lon = new Double(aux.getString("lon"));
                points.add(Arrays.asList(lon,lat));
        }
    MongoCursor cursor = getCollection().find((Filters.geoWithinPolygon("geometry", points))).iterator(); {
        while (cursor.hasNext()) {
            Document document = (Document) cursor.next();
            Features feature = new Features();
            feature.setData(document);
            list.add(feature);
        }
        System.out.print(list);
        return list;
    }


}

    private MongoCollection getCollection(){
        return mongoClient.getDatabase("BBDD2").getCollection("accidents");
    }
}
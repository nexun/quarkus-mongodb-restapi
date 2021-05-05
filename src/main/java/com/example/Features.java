package com.example;

import org.bson.Document;

public class Features {

    private Document data;
    private String id;

    public Features() {
    }

    public Features(Document data,  String id) {
        this.data = data;
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Document getData() {
        return data;
    }

    public void setData(Document data) {
        this.data = data;
    }
}
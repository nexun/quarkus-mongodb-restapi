package com.bd2api;

import org.bson.Document;

public class Accident {

    private Document data;

    public Accident() {
    }

    public Accident(Document data) {
        this.data = data;
    }

    public Document getData() {
        return data;
    }

    public void setData(Document data) {
        this.data = data;
    }
}
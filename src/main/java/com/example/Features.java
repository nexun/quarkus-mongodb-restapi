package com.example;

import org.bson.Document;

public class Features {

    private Document data;

    public Features() {
    }

    public Features(Document data,  String id) {
        this.data = data;
    }


    public Document getData() {
        return data;
    }

    public void setData(Document data) {
        this.data = data;
    }
}
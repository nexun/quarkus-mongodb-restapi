package com.bd2api;

import org.bson.Document;

public class Accident {

    private String parameter;
    private String value;
    private Integer occurrences;
    private Document data;

    public Accident() {
    }

   
    public Accident(String parameter, String value, Integer occurrences, Document data) {
        this.parameter = parameter;
        this.value = value;
        this.occurrences = occurrences;
        this.data = data;
    }


    public Integer getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(Integer integer) {
        this.occurrences = integer;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Document getData() {
        return data;
    }

    public void setData(Document data) {
        this.data = data;
    }
}
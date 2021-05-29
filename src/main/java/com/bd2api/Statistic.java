package com.bd2api;

import org.bson.Document;

public class Statistic {
    private String parameter;
    private String value;
    private Integer occurrences;


        public Statistic(String parameter, String value, Integer occurrences, Document data) {
            this.parameter = parameter;
            this.value = value;
            this.occurrences = occurrences;

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
}
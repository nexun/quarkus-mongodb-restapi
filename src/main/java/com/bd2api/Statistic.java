package com.bd2api;

import org.bson.Document;

public class Statistic {
    private String parameter;
    private String value;
    private Long occurrences;

    public Statistic(String parameter, String value, Long ocurrences, Document data) {
        this.parameter = parameter;
        this.value = value;
        this.occurrences = ocurrences;

    }

    public Long getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(Long ocurrences) {
        this.occurrences = ocurrences;
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
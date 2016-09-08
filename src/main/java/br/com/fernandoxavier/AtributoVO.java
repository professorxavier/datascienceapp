package br.uniriotec.ppgi.fernandoxavier;

import java.util.ArrayList;

public class AtributoVO {

    String attributeName="";
    int totalValues = 0;
    int missingValues = 0;

    public int getMissingValues() {
        return missingValues;
    }

    public void setMissingValues(int missingValues) {
        this.missingValues = missingValues;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public int getTotalValues() {
        return totalValues;
    }

    public void setTotalValues(int totalValues) {
        this.totalValues = totalValues;
    }



}

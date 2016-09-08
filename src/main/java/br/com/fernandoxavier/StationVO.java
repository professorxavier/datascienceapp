package br.uniriotec.ppgi.fernandoxavier;

import java.util.ArrayList;

public class StationVO {

    String stationName = "";
    String city = "";
    String state = "";
    double latitude;
    double longitude;
    double altitude;
    String header="";
    double coefficient=0;
    ArrayList<String> instances= new ArrayList<String>();
    private double mae;
    private double rmse;
    private String numInstances;

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void addInstance (String instance) {
        this.instances.add(instance);
    }

    public ArrayList<String> getInstances() {
        return instances;
    }

    public void setInstances(ArrayList<String> instances) {
        this.instances = instances;
    }

    @Override
    public String toString() {
        return getStationName() + " - " + getState() + " - "
                + getCity() + " - " + getLatitude() + " - "
                + getLongitude() + " - " + getAltitude();
    }

    public void setMae(double mae) {
        this.mae = mae;
    }

    public void setRmse(double rmse) {
        this.rmse = rmse;
    }

    public double getMae() {
        return mae;
    }

    public double getRmse() {
        return rmse;
    }

    public String getNumInstances() {
        return numInstances;
    }
}

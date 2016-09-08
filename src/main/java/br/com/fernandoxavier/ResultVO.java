package br.uniriotec.ppgi.fernandoxavier;

/**
 * Created by Dell on 17/02/2016.
 */
public class ResultVO {
    private double coefficient;
    private double mae;
    private double rmse;
    private String stationName;
    private double latitude;
    private double longitude;
    private String algorithm;
    private String model;
    private String experiment;
    private double numInstances;
    private double altitude;
    private double rrse;
    private double rae;
    private double maer;
    private double rmser;
	private double averageevp;

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public void setMae(double mae) {
        this.mae = mae;
    }

    public void setRmse(double rmse) {
        this.rmse = rmse;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public double getMae() {
        return mae;
    }

    public double getRmse() {
        return rmse;
    }

    public String getModel() {
        return model;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getStationName() {
        return stationName;
    }

    public String getExperiment() {
        return experiment;
    }

    public void setExperiment(String experiment) {
        this.experiment = experiment;
    }

    public void setNumInstances(double numInstances) {

        this.numInstances = numInstances;
    }

    public double getNumInstances() {
        return numInstances;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getRrse() {
        return rrse;
    }

    public double getRae() {
        return rae;
    }

    public void setRae(double rae) {
        this.rae = rae;
    }

    public void setRrse(double rrse) {
        this.rrse = rrse;
    }

    public double getMaer() {
        return maer;
    }

    public void setMaer(double maer) {
        this.maer = maer;
    }

    public double getRmser() {
        return rmser;
    }
	
    public void setRmser(double rmser) {
        this.rmser = rmser;
    }

    public double getAverageEvp() {
        return averageevp;
    }
	
    public void setAverageEvp(double averageevp) {
        this.averageevp = averageevp;
    }

	}

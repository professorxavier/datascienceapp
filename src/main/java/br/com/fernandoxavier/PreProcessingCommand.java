package br.uniriotec.ppgi.fernandoxavier;


import br.uniriotec.ppgi.fernandoxavier.utils.MongoUtil;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RFactor;
import org.rosuda.JRI.RVector;
import org.rosuda.JRI.Rengine;

import java.io.File;
import java.util.ArrayList;

public class PreProcessingCommand implements OperationCommand {

    ArrayList<StationVO> stations = new ArrayList<StationVO>();
    MongoClient mongoClient;
    MongoCollection mongoCollection;
    Rengine rengine;
    int from=2010;
    int to=2014;
    String experiment;

    public PreProcessingCommand() {

        rengine = new Rengine(new String[] { "--vanilla" }, false, null);
        mongoClient = new MongoClient();
        mongoCollection = mongoClient.getDatabase("test").getCollection("datasets");

    }


    public void execute() {
        System.out.println(getClass().getSimpleName() + " says: I receive the request and I will execute");

        getDatasetsFromDB();
        for (StationVO station: stations) {
            if (experiment.equals("origin"))
                calculatePenman(station.getStationName()+".csv", station.getLatitude(), station.getAltitude(), from, to);
            else if (experiment.equals("zeros"))
                calculatePenmanZeros(station.getStationName()+".csv", station.getLatitude(), station.getAltitude(), from, to);
			else if (experiment.equals("nebul"))
                calculatePenmanNebul(station.getStationName()+".csv", station.getLatitude(), station.getAltitude(), from, to);
			else if (experiment.equals("nebulumid"))
                calculatePenmanNebulUmid(station.getStationName()+".csv", station.getLatitude(), station.getAltitude(), from, to);
            else if (experiment.equals("insol"))
                calculatePenmanInsol(station.getStationName()+".csv", station.getLatitude(), station.getAltitude(), from, to);
            else if (experiment.equals("all"))
                calculatePenmanAllInstances(station.getStationName()+".csv", station.getLatitude(), station.getAltitude());

        }

        mongoClient.close();

    }

    public void calculatePenman(String dataset, double latitude, double altitude, int from, int to) {
        REXP result1 = rengine.eval(".libPaths('C:/Users/Dell/Documents/R/win-library/3.1')");
        REXP result2 = rengine.eval("library(SPEI)");
        REXP result3 = rengine.eval("setwd('C:/Users/Dell/Documents/minhas/carreira/academico/unirio/pesquisa/2016/DataScienceEvapotranspirationApp')");
        REXP result4 = rengine.eval("source('r/pmaux.R')");
        REXP years = rengine.eval("years<-"+from+":"+to);

        System.out.println("entering method for " + dataset);

        String stationname = dataset.replace(".csv","");
        System.out.println("Processing " + stationname + " - " + dataset
                + " - " + latitude + " - " + altitude);
        REXP result5 = rengine.eval("pm('files/datasets/semcabecalho/"+dataset+"',"+latitude+"," + altitude+",years)");
//        System.out.println("result " + result5);
        if (result5!=null) {
            RVector v = result5.asVector();

            RFactor datas = v.at(0).asFactor();
            double[] evp = v.at(1).asDoubleArray();

            for (int i=0; i<datas.size();i++) {
                Bson match = Projections.elemMatch("instances", Filters.eq("Data",datas.at(i)));
                double evpvalue = evp[i];
                UpdateResult updateResult = mongoCollection
                        .updateOne(Filters.and(new Document("stationname",stationname)
                                ,match),
                                new Document("$set", new Document("instances.$.evp",evpvalue)));
//                System.out.println("Resultado update" + updateResult.toString());

            }
        }
        System.out.println("Finished process for " + stationname);
        rengine.end();


    }

    public void calculatePenmanZeros(String dataset, double latitude, double altitude, int from, int to) {
        REXP result1 = rengine.eval(".libPaths('C:/Users/Dell/Documents/R/win-library/3.1')");
        REXP result2 = rengine.eval("library(SPEI)");
        REXP result3 = rengine.eval("setwd('C:/Users/Dell/Documents/minhas/carreira/academico/unirio/pesquisa/2016/DataScienceEvapotranspirationApp')");
        REXP result4 = rengine.eval("source('r/pmaux.R')");
        REXP years = rengine.eval("years<-"+from+":"+to); //todo change for parameter

        System.out.println("entering method for " + dataset);

        String stationname = dataset.replace(".csv","");
        System.out.println("Processing " + stationname + " - " + dataset
                + " - " + latitude + " - " + altitude);
        REXP result5 = rengine.eval("pmFillZeros('files/datasets/semcabecalho/"+dataset+"',"+latitude+"," + altitude+",years)");
//        System.out.println("result " + result5);
        if (result5!=null) {
            RVector v = result5.asVector();

            RFactor datas = v.at(0).asFactor();
            double[] evp = v.at(1).asDoubleArray();

            for (int i=0; i<datas.size();i++) {
                Bson match = Projections.elemMatch("instances", Filters.eq("Data",datas.at(i)));
                double evpvalue = evp[i];
                UpdateResult updateResult = mongoCollection
                        .updateOne(Filters.and(new Document("stationname",stationname)
                                ,match),
                                new Document("$set", new Document("instances.$.evp",evpvalue)));
//                System.out.println("Resultado update" + updateResult.toString());

            }
        }
        System.out.println("Finished process for " + stationname);
        rengine.end();


    }

	    public void calculatePenmanNebul(String dataset, double latitude, double altitude, int from, int to) {
        REXP result1 = rengine.eval(".libPaths('C:/Users/Dell/Documents/R/win-library/3.1')");
        REXP result2 = rengine.eval("library(SPEI)");
        REXP result3 = rengine.eval("setwd('C:/Users/Dell/Documents/minhas/carreira/academico/unirio/pesquisa/2016/DataScienceEvapotranspirationApp')");
        REXP result4 = rengine.eval("source('r/pmaux.R')");
        REXP years = rengine.eval("years<-"+from+":"+to); //todo change for parameter

        System.out.println("entering method for " + dataset);

        String stationname = dataset.replace(".csv","");
        System.out.println("Processing " + stationname + " - " + dataset
                + " - " + latitude + " - " + altitude);
        REXP result5 = rengine.eval("pmNebul('files/datasets/semcabecalho/"+dataset+"',"+latitude+"," + altitude+",years)");
//        System.out.println("result " + result5);
        if (result5!=null) {
            RVector v = result5.asVector();

            RFactor datas = v.at(0).asFactor();
            double[] evp = v.at(1).asDoubleArray();

            for (int i=0; i<datas.size();i++) {
                Bson match = Projections.elemMatch("instances", Filters.eq("Data",datas.at(i)));
                double evpvalue = evp[i];
                UpdateResult updateResult = mongoCollection
                        .updateOne(Filters.and(new Document("stationname",stationname)
                                ,match),
                                new Document("$set", new Document("instances.$.evp",evpvalue)));
//                System.out.println("Resultado update" + updateResult.toString());

            }
        }
        System.out.println("Finished process for " + stationname);
        rengine.end();


    }

	    public void calculatePenmanNebulUmid(String dataset, double latitude, double altitude, int from, int to) {
        REXP result1 = rengine.eval(".libPaths('C:/Users/Dell/Documents/R/win-library/3.1')");
        REXP result2 = rengine.eval("library(SPEI)");
        REXP result3 = rengine.eval("setwd('C:/Users/Dell/Documents/minhas/carreira/academico/unirio/pesquisa/2016/DataScienceEvapotranspirationApp')");
        REXP result4 = rengine.eval("source('r/pmaux.R')");
        REXP years = rengine.eval("years<-"+from+":"+to); //todo change for parameter

        System.out.println("entering method for " + dataset);

        String stationname = dataset.replace(".csv","");
        System.out.println("Processing " + stationname + " - " + dataset
                + " - " + latitude + " - " + altitude);
        REXP result5 = rengine.eval("pmNebulUmid('files/datasets/semcabecalho/"+dataset+"',"+latitude+"," + altitude+",years)");
//        System.out.println("result " + result5);
        if (result5!=null) {
            RVector v = result5.asVector();

            RFactor datas = v.at(0).asFactor();
            double[] evp = v.at(1).asDoubleArray();

            for (int i=0; i<datas.size();i++) {
                Bson match = Projections.elemMatch("instances", Filters.eq("Data",datas.at(i)));
                double evpvalue = evp[i];
                UpdateResult updateResult = mongoCollection
                        .updateOne(Filters.and(new Document("stationname",stationname)
                                ,match),
                                new Document("$set", new Document("instances.$.evp",evpvalue)));
//                System.out.println("Resultado update" + updateResult.toString());

            }
        }
        System.out.println("Finished process for " + stationname);
        rengine.end();


    }

	
		public void calculatePenmanInsol(String dataset, double latitude, double altitude, int from, int to) {
        REXP result1 = rengine.eval(".libPaths('C:/Users/Dell/Documents/R/win-library/3.1')");
        REXP result2 = rengine.eval("library(SPEI)");
        REXP result3 = rengine.eval("setwd('C:/Users/Dell/Documents/minhas/carreira/academico/unirio/pesquisa/2016/DataScienceEvapotranspirationApp')");
        REXP result4 = rengine.eval("source('r/pmaux.R')");
        REXP years = rengine.eval("years<-"+from+":"+to); //todo change for parameter

        System.out.println("entering method for " + dataset);

        String stationname = dataset.replace(".csv","");
        System.out.println("Processing " + stationname + " - " + dataset
                + " - " + latitude + " - " + altitude);
        REXP result5 = rengine.eval("pmInsol('files/datasets/semcabecalho/"+dataset+"',"+latitude+"," + altitude+",years)");
//        System.out.println("result " + result5);
        if (result5!=null) {
            RVector v = result5.asVector();

            RFactor datas = v.at(0).asFactor();
            double[] evp = v.at(1).asDoubleArray();

            for (int i=0; i<datas.size();i++) {
                Bson match = Projections.elemMatch("instances", Filters.eq("Data",datas.at(i)));
                double evpvalue = evp[i];
                UpdateResult updateResult = mongoCollection
                        .updateOne(Filters.and(new Document("stationname",stationname)
                                ,match),
                                new Document("$set", new Document("instances.$.evp",evpvalue)));
//                System.out.println("Resultado update" + updateResult.toString());

            }
        }
        System.out.println("Finished process for " + stationname);
        rengine.end();


    }

	
    /*
    Calculate PM for all possible instances (no NA)
     */
    public void calculatePenmanAllInstances(String dataset, double latitude, double altitude) {
        REXP result1 = rengine.eval(".libPaths('C:/Users/Dell/Documents/R/win-library/3.1')");
        REXP result2 = rengine.eval("library(SPEI)");
        REXP result3 = rengine.eval("setwd('C:/Users/Dell/Documents/minhas/carreira/academico/unirio/pesquisa/2016/DataScienceEvapotranspirationApp')");
        REXP result4 = rengine.eval("source('r/pmaux.R')");

        System.out.println("entering method for " + dataset);

        String stationname = dataset.replace(".csv","");
        System.out.println("Processing " + stationname + " - " + dataset
                + " - " + latitude + " - " + altitude);
        REXP result5 = rengine.eval("pmAll('files/datasets/semcabecalho/"+dataset+"',"+latitude+"," + altitude +")");
//        System.out.println("result " + result5);
        if (result5!=null) {
            RVector v = result5.asVector();

            RFactor datas = v.at(0).asFactor();
            double[] evp = v.at(1).asDoubleArray();

            for (int i=0; i<datas.size();i++) {
                Bson match = Projections.elemMatch("instances", Filters.eq("Data",datas.at(i)));
                double evpvalue = evp[i];
                UpdateResult updateResult = mongoCollection
                        .updateOne(Filters.and(new Document("stationname",stationname)
                                ,match),
                                new Document("$set", new Document("instances.$.evp",evpvalue)));
                System.out.println("Resultado update" + updateResult.toString());

            }
        }
        System.out.println("Finished process for " + stationname);
        rengine.end();


    }


    public void undo() {
        System.out.println("I need to undo the operations");
    }

    public int getDatasetsFromDB() {
        String dbName="test"; //TODO catch from properties
        String stationsCollection = "stations"; //TODO catch from properties
        String datasetsCollection = "datasets"; //TODO catch from properties
        MongoUtil mongoUtil = new MongoUtil();
        mongoUtil.init(dbName, stationsCollection);
        setStations(mongoUtil.loadStations(datasetsCollection));

        return stations.size();
    }

    public int getDatasetsFromDB(String stationname) {
        String dbName="test"; //TODO catch from properties
        String stationsCollection = "stations"; //TODO catch from properties
        String datasetsCollection = "datasets"; //TODO catch from properties
        MongoUtil mongoUtil = new MongoUtil();
        mongoUtil.init(dbName, stationsCollection);
        setStations(mongoUtil.loadStations(datasetsCollection, stationname));

        return stations.size();
    }
    public ArrayList<StationVO> getStations() {
        return stations;
    }

    public void setStations(ArrayList<StationVO> stations) {
        this.stations = stations;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void setExperiment(String experiment) {
        this.experiment = experiment;
    }
}

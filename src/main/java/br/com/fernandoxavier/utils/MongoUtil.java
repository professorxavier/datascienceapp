package br.uniriotec.ppgi.fernandoxavier.utils;

import br.uniriotec.ppgi.fernandoxavier.ResultVO;
import br.uniriotec.ppgi.fernandoxavier.StationVO;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Aggregates.*;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Filters;
import org.bson.BSON;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.print.Doc;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Accumulators.addToSet;
import static com.mongodb.client.model.Accumulators.sum;

public class MongoUtil {
    private MongoDatabase DB;
    private MongoCollection collection;
    private MongoClient mongoClient;

    public void setDB(MongoDatabase DB) {
        this.DB = DB;
    }

    public MongoDatabase getDB() {
        return DB;
    }

    public void setCollection(MongoCollection collection) {
        this.collection = collection;
    }


    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public boolean dropCollection(String collectionName) {
        try {
            getDB().getCollection(collectionName).drop();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public long countDocuments(String collectionName) {
        return getDB().getCollection(collectionName).count();
    }

    public boolean init(String dbName, String collectionName) {

        try {
            mongoClient = new MongoClient();
            this.setDB(mongoClient.getDatabase(dbName));
            this.setCollection(getDB().getCollection(collectionName));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertStations(ArrayList<StationVO> stations) {

        try {
            for (StationVO station : stations) {
                Document document = new Document("name", station.getStationName());
                document.append("state", station.getState());
                document.append("city", station.getCity());
                document.append("latitude", station.getLatitude());
                document.append("longitude", station.getLongitude());
                document.append("altitude", station.getAltitude());
                collection.insertOne(document);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertDatasets(ArrayList<StationVO> stations) {

        try {
            for (StationVO station : stations) {
                Document document = new Document("stationname", station.getStationName());
                document.append("header", station.getHeader());
                ArrayList<String> instances = station.getInstances();
                ArrayList<Document> docs = new ArrayList<Document>();
                String[] header = station.getHeader().split(";");
                int nmumcolumns = header.length;
                ArrayList<Document> instDocs = new ArrayList<Document>();
                for (String instance: instances) {
                    String[] fields = instance.split(";");
                    Document doc = new Document();
                    for (int i=0;i<fields.length;i++) {
                        doc.append(header[i],fields[i]);
                    }
                    if (fields.length<2) {
                        System.out.println(instance);
                        System.out.println("station " + station.getStationName());
                    }
                   instDocs.add(doc);
                   /* Document docInstance = new Document("dateMeasure",fields[1]);
                    docInstance.append("values", instance);
                    docs.add(docInstance); */
                }
                document.append("instances", instDocs);
                collection.insertOne(document);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<StationVO> loadStations(String datasetsCollectionName) {
        ArrayList<StationVO> stations = new ArrayList<StationVO>();
        ArrayList<Document> stationsDocs = (ArrayList<Document>) collection.find().into(new ArrayList<Document>());
        for (Document stationDoc: stationsDocs) {
            StationVO stationVO = new StationVO();
            stationVO.setStationName(stationDoc.getString("name"));
            stationVO.setState(stationDoc.getString("state"));
            stationVO.setCity(stationDoc.getString("city"));
            stationVO.setLatitude(stationDoc.getDouble("latitude"));
            stationVO.setAltitude(stationDoc.getDouble("altitude"));
            stationVO.setLongitude(stationDoc.getDouble("longitude"));

            ArrayList<String> instances = loadInstances(datasetsCollectionName, stationVO.getStationName());
            stationVO.setInstances(instances);
            System.out.println(String.format("Found %d instances for Station %s",
                    instances.size(),
                    stationVO.getStationName()));

            stations.add(stationVO);

        }
        System.out.println(String.format("%d stations loaded", stations.size()));
        return stations;
    }

    public ArrayList<StationVO> loadStations(String datasetsCollectionName, String stationname) {
        ArrayList<StationVO> stations = new ArrayList<StationVO>();
        ArrayList<Document> stationsDocs = (ArrayList<Document>) collection
                .find(new Document("name", stationname))
                .into(new ArrayList<Document>());
        for (Document stationDoc: stationsDocs) {
            StationVO stationVO = new StationVO();
            stationVO.setStationName(stationDoc.getString("name"));
            stationVO.setState(stationDoc.getString("state"));
            stationVO.setCity(stationDoc.getString("city"));
            stationVO.setLatitude(stationDoc.getDouble("latitude"));
            stationVO.setAltitude(stationDoc.getDouble("altitude"));
            stationVO.setLongitude(stationDoc.getDouble("longitude"));

            ArrayList<String> instances = loadInstances(datasetsCollectionName, stationVO.getStationName());
            stationVO.setHeader(loadHeader(datasetsCollectionName, stationVO.getStationName()));
            stationVO.setInstances(instances);
            System.out.println(String.format("Found %d instances for Station %s",
                    instances.size(),
                    stationVO.getStationName()));

            stations.add(stationVO);

        }
        System.out.println(String.format("%d stations loaded", stations.size()));
        return stations;
    }
	
	   public ArrayList<ResultVO> loadResults(String experiment) {
			ArrayList<ResultVO> results = new ArrayList<ResultVO>();
			ArrayList<Document> docs = (ArrayList<Document>) collection
					.find(new Document("experiment", experiment))
					.into(new ArrayList<Document>());
			for (Document doc: docs) {
				ResultVO resultVO = new ResultVO();
				resultVO.setCoefficient(doc.getDouble("coefficient"));
				resultVO.setMae(doc.getDouble("mae"));
				resultVO.setRmse(doc.getDouble("rmse"));
				resultVO.setMaer(doc.getDouble("maer"));
				resultVO.setRmser(doc.getDouble("rmser"));
				resultVO.setRae(doc.getDouble("rae"));
				resultVO.setRrse(doc.getDouble("rrse"));
				resultVO.setStationName(doc.getString("stationname"));
				resultVO.setLatitude(doc.getDouble("latitude"));
				resultVO.setLongitude(doc.getDouble("longitude"));
				//resultVO.setAltitude(doc.getString("");
				resultVO.setAlgorithm("MP5");
				resultVO.setModel(doc.getString("model"));
				resultVO.setExperiment(experiment);
				resultVO.setNumInstances(doc.getDouble("instances"));
				results.add(resultVO);
			}
			return results;
	   
	   }

    public StationVO loadSingleStation(String stationname) {
        StationVO stationVO = new StationVO();
        ArrayList<Document> stationsDocs = (ArrayList<Document>) getMongoClient().getDatabase("test").getCollection("stations")
                .find(new Document("name", stationname))
                .into(new ArrayList<Document>());
        for (Document stationDoc: stationsDocs) {
            stationVO.setStationName(stationname);
            stationVO.setState(stationDoc.getString("state"));
            stationVO.setCity(stationDoc.getString("city"));
            stationVO.setLatitude(stationDoc.getDouble("latitude"));
            stationVO.setAltitude(stationDoc.getDouble("altitude"));
            stationVO.setLongitude(stationDoc.getDouble("longitude"));

        }
        return stationVO;
    }


    private String loadHeader(String datasetsCollectionName, String stationName) {
        MongoCollection datasets = getDB().getCollection(datasetsCollectionName);
        ArrayList<Document> results = (ArrayList<Document>) datasets.find(new Document("stationname", stationName))
                .projection(new Document("header",1)).into(new ArrayList<Document>());
        String header="";
        for (Document result: results) {
            header = (String) result.get("header");
        }
        return header;
    }

    private ArrayList<String> loadInstances(String datasetsCollectionName, String stationName) {
        ArrayList<String> instances = new ArrayList<String>();

        MongoCollection datasets = getDB().getCollection(datasetsCollectionName);
        ArrayList<Document> results = (ArrayList<Document>) datasets.find(new Document("stationname", stationName))
                .projection(new Document("instances",1)).into(new ArrayList<Document>());
        for (Document result: results) {
            instances = (ArrayList<String>) result.get("instances");
        }
        return instances;
    }

    public ArrayList<Document> getInstancesWithEVP(String stationname) {

        Bson match = Aggregates.match(new Document("stationname",stationname));
        Bson un = Aggregates.unwind("$instances");
        Bson evpOK =  Aggregates.match(Filters.exists("instances.evp",true));
        ArrayList<Bson> matches = new ArrayList<Bson>();
        matches.add(match);
        matches.add(un);
        matches.add(evpOK);
        ArrayList<Document> results = (ArrayList<Document>) collection
                .aggregate(matches).into(new ArrayList<Document>());
        return results;

    }


    public ArrayList<Document> getStationsWithEVP() {

        ArrayList<Document> results = (ArrayList<Document>)
                collection.find(new Document("instances.evp",new Document("$exists",true)))
                        .projection(new Document("stationname",1).append("_id",0))
                        .into(new ArrayList<Document>());
        return results;

    }

        public void closeClient() {
        mongoClient.close();
    }

    public ArrayList<Document> getResults(String algorithm) {
        ArrayList<Document> docs =(ArrayList<Document>)
                collection.find().into(new ArrayList<Document>());
        return docs;
    }

    public ArrayList<Document> getCountResultsByStation() {
		Bson group = Aggregates.group("$stationname", sum("count", 1));
        ArrayList<Bson> matches = new ArrayList<Bson>();
        matches.add(group);
        ArrayList<Document> docs = (ArrayList<Document>) collection
                .aggregate(matches).into(new ArrayList<Document>());
        return docs;
    }

    public ArrayList<Document> loadMissingDataInformation() {
        ArrayList<StationVO> stations = new ArrayList<StationVO>();
        ArrayList<Document> missingDataDocs = (ArrayList<Document>) collection.find().into(new ArrayList<Document>());
        return missingDataDocs;
    }

    public ArrayList<Document> getAllResults() {
        ArrayList<Document> results = (ArrayList<Document>) collection.find().into(new ArrayList<Document>());
        return results;
    }

    public ArrayList<Document> getCoefficients() {
        //Bson group = Aggregates.group("$stationname", addToSet("exp", "$experiment"),addToSet("coef","$coefficient"));
        Bson group = Aggregates.group("$stationname",
                addToSet("list",new Document("exp","$experiment").append("coef","$coefficient")));
        ArrayList<Bson> matches = new ArrayList<Bson>();
        matches.add(group);
        ArrayList<Document> docs = (ArrayList<Document>) collection
                .aggregate(matches).into(new ArrayList<Document>());
        return docs;
    }
}

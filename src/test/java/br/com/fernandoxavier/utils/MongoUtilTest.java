package br.uniriotec.ppgi.fernandoxavier.utils;

import br.uniriotec.ppgi.fernandoxavier.StationVO;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.bson.Document;
import org.bson.conversions.Bson;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.trees.M5P;
import weka.classifiers.trees.m5.RuleNode;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MongoUtilTest extends TestCase {

    MongoUtil mongoUtil;

    public MongoUtilTest() {
        mongoUtil = new MongoUtil();

    }

    public static Test suite()
    {
        return new TestSuite( MongoUtilTest.class );
    }

    public void testInsertStations() {
        try {
            ArrayList<StationVO> stations = new ArrayList<StationVO>();

            //creating data test
            StationVO station = new StationVO();
            station.setStationName("ac_cruzeirodosul");
            station.setAltitude(170d);
            station.setLongitude(-72.66d);
            station.setLatitude(-7.6d);
            station.setCity("cruzeirodosul");
            station.setState("ac");
            station.setHeader("name");
            station.addInstance("name1");
            station.addInstance("name2");
            stations.add(station);

            String collectionName = "stationstest";
            assertTrue("Error in init mongoutil", mongoUtil.init("test", collectionName));
            assertTrue("Error in inserting data", mongoUtil.insertStations(stations));
            assertTrue("Data inserted with error",mongoUtil.countDocuments(collectionName)==1);
            assertTrue("Error in drop collection",mongoUtil.dropCollection(collectionName));
            assertTrue("Still exists documents in collection",mongoUtil.countDocuments(collectionName)==0);


        } catch (Exception e) {
            fail("Erro na conexao com o mongodb " + e.getMessage());
        }
    }

    public void testLoadStations() {
        try {
            String dbName="test";
            String stationsCollection = "stations";
            String datasetsCollection = "datasets";
            mongoUtil.init(dbName, stationsCollection);
            ArrayList<StationVO> stations = mongoUtil.loadStations(datasetsCollection);
            assertTrue("Error in loading stations", stations.size()>0);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Error " + e.getMessage());
        }
    }

    public void testgetInstancesWithEVP() {
        try {
            String dbName="test";
            String stationsCollection = "stations";
            String datasetsCollection = "datasets";
            mongoUtil.init(dbName, datasetsCollection);
            ArrayList<Document> stations = mongoUtil.getInstancesWithEVP("ac_cruzeirodosul");
            System.out.println(stations.size());
            assertTrue("Error in loading stations", stations.size()==0);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Error " + e.getMessage());
        }
    }

}

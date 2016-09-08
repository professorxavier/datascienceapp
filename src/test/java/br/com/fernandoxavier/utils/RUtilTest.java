package br.uniriotec.ppgi.fernandoxavier.utils;

import br.uniriotec.ppgi.fernandoxavier.StationVO;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.rosuda.JRI.*;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

public class RUtilTest extends TestCase {

    public RUtilTest() {
    }

    public static Test suite()
    {
        return new TestSuite( RUtilTest.class );
    }

//    public void testRCalculation() {
//        REXP result = rengine.eval("1+1");
//        System.out.println(result.toString());
//        assertEquals("Resultados diferentes", 2.0d, result.asDouble());
//        result = rengine.eval("1+4");
//        System.out.println(result.toString());
//        assertTrue("Resultados iguais", result.asDouble()!=4.0d);
//    }

/*
    public void testUsePenman() {
        RUtil rUtil = new RUtil();
       // rUtil.init();
        //rengine.eval(".libPaths('C:/Users/Dell/Documents/R/win-library/3.1')");
        //rengine.eval("library(SPEI)");
        REXP result = rUtil.eval("penman(c(20,21,22,20,21,22,20,21,22,20,21,22)," +
                "c(27,28,29,21,23,24,28,29,20,23,23,23),c(1,1,1,1,1,1,1,1,1,1,1,1)," +
                "NA,-7.6,NA,c(3,3,3,3,4,4,4,4,5,5,5,5),7.83,NA,NA,87.09,NA,NA,170)");
        System.out.println(result.toString());

    }
*/
/*
    public void testProcessAllDatasets() {

        Rengine rengine = new Rengine(new String[] { "--vanilla" }, false, null);
        REXP result1 = rengine.eval(".libPaths('C:/Users/Dell/Documents/R/win-library/3.1')");
        System.out.println(result1.toString());
        REXP result2 = rengine.eval("library(SPEI)");
        System.out.println(result2.toString());
        REXP result3 = rengine.eval("setwd('C:/Users/Dell/Documents/minhas/carreira/academico/unirio/pesquisa/2016/DataScienceEvapotranspirationApp')");
        System.out.println(result3.toString());
        REXP result4 = rengine.eval("source('r/pmaux.R')");
        System.out.println(result4.toString());
        String FILES_DATASETS_SEMCABECALHO = "files/datasets/semcabecalho/";
        File dir = new File(FILES_DATASETS_SEMCABECALHO);
        File[] f = dir.listFiles();
        for (int i=0;i<f.length-1;i++) {
            String dataset = f[i].getName();
            String status = dataset;
            REXP result5 = rengine.eval("pm('files/datasets/semcabecalho/"+dataset+"')");
            if (result5!=null) {
                System.out.println(status + ";processado");

                RList list = result5.asList();
                String[] keys = list.keys();
                for (String key : keys) {
                    System.out.println("key->" + key + "::value->" + list.at(key));
                }

            } else {
            }
        }
    }
*/

    /*
    public void testOwnFunction() {

        Rengine rengine = new Rengine(new String[] { "--vanilla" }, false, null);
        REXP result1 = rengine.eval(".libPaths('C:/Users/Dell/Documents/R/win-library/3.1')");
        REXP result2 = rengine.eval("library(SPEI)");
        REXP result3 = rengine.eval("setwd('C:/Users/Dell/Documents/minhas/carreira/academico/unirio/pesquisa/2016/DataScienceEvapotranspirationApp')");
        REXP result4 = rengine.eval("source('r/pmaux.R')");
        String dataset = "ac_cruzeirodosul.csv";
        String stationname = dataset.replace(".csv","");
        REXP result5 = rengine.eval("pm('files/datasets/semcabecalho/"+dataset+"')");
        if (result5!=null) {
            RVector v = result5.asVector();
            MongoClient mongoClient = new MongoClient();
            MongoCollection mongoCollection = mongoClient.getDatabase("test").getCollection("datasets");

            RFactor datas = v.at(0).asFactor();
            double[] evp = v.at(1).asDoubleArray();

            for (int i=0; i<datas.size();i++) {
                Bson match = Projections.elemMatch("instances", Filters.eq("dateMeasure",datas.at(i)));

                FindIterable<Document> iterable = mongoCollection.find(new Document("stationname",stationname)
                        .append("instances.dateMeasure",datas.at(i))).projection(match);

                for (Document document: iterable) {
                    System.out.println(document);
                    ArrayList<Document> valores = (ArrayList<Document>) document.get("instances");
                    for (Document valor: valores) {
                        String values = valor.get("values").toString();
                        System.out.println("Valor antigo " + values);
                        System.out.println("Novo valor " + values + ";" + evp[i]);
                        mongoCollection.updateOne(Filters.and(new Document("stationname",stationname)
                                .append("instances.dateMeasure",datas.at(i)),match),
                                new Document("$push", new Document("instances.$.valuesevp",values + ";" + evp[i])));

                    }
                }

            }
        }
    }

*/

}

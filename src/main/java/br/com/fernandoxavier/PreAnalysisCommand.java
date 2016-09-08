package br.uniriotec.ppgi.fernandoxavier;


import br.uniriotec.ppgi.fernandoxavier.utils.ReportUtil;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

import java.io.File;

public class PreAnalysisCommand implements OperationCommand {

    private final String FILES_DATASETS_SEMCABECALHO = "files/datasets/semcabecalho/";
    public void execute() {
        System.out.println(getClass().getSimpleName() + " says: I receive the request and I will execute");
    }

    public void undo() {
        System.out.println("I need to undo the operations");
    }

    public void checkAvailabiltyData(String years) {

        Rengine rengine = new Rengine(new String[] { "--vanilla" }, false, null);
        REXP result1 = rengine.eval(".libPaths('C:/Users/Dell/Documents/R/win-library/3.1')");
        REXP result2 = rengine.eval("setwd('C:/Users/Dell/Documents/minhas/carreira/academico/unirio/pesquisa/2016/DataScienceEvapotranspirationApp')");
        REXP result3 = rengine.eval("source('r/stats.R')");

        String filter = "c("+years+")";
        MongoClient mongoClient = new MongoClient();
       // mongoClient.getDatabase("test").getCollection("missingstats").drop();
        MongoCollection mongoCollection = mongoClient.getDatabase("test").getCollection("missingstats");
          //todo catch from properties
        File dir = new File(FILES_DATASETS_SEMCABECALHO);
        File[] f = dir.listFiles();
        for (int i=0;i<f.length-1;i++) {
            String dataset = f[i].getName();
            String stationname = dataset.replace(".csv", "");
            String file = "'" + FILES_DATASETS_SEMCABECALHO + dataset + "'";
            REXP result5 = rengine.eval("dt<-statsDataset(" + file + "," + filter + ")");
            REXP result6 = rengine.eval("names(dt)");
            System.out.println("Analysing "+ stationname);
            if (result5 != null) {
                String[] names = result6.asStringArray();
                double[] values = result5.asDoubleArray();
                Document doc = new Document("stationname", stationname)
                        .append("period", years);
                for (int j = 0; j < names.length; j++) {
                    doc.append(names[j], values[j]);
                }
                mongoCollection.insertOne(doc);
            } else {
                System.out.println("Nao gerou resultados");
            }
        }
        //mongoClient.close();
    }

    public void generateMissingDataReport() {

        ReportUtil.exportMissingStatsToLatex();

    }

}

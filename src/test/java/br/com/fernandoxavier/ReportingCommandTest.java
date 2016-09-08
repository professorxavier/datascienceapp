package br.uniriotec.ppgi.fernandoxavier;

import br.uniriotec.ppgi.fernandoxavier.utils.MongoUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.bson.Document;
import br.uniriotec.ppgi.fernandoxavier.utils.ReportUtil;
import org.junit.Ignore;
import br.uniriotec.ppgi.fernandoxavier.ResultVO;

import java.util.ArrayList;

public class ReportingCommandTest extends TestCase {

    ReportingCommand command;
    MongoUtil mongoUtil;

    public ReportingCommandTest() {
        command = new ReportingCommand();
        mongoUtil = new MongoUtil();
    }

    public static Test suite()
    {
        return new TestSuite( ReportingCommandTest.class );
    }

	    public void testGetExperimentResults() {

			try {
				String collectionName = "results";
				assertTrue("Error in init mongoutil", mongoUtil.init("test", collectionName));
				String exp = "mp5_dados_2010_2014_cjtominimo_precip_insolacao";
				ArrayList<ResultVO>  results = mongoUtil.loadResults("mp5_dados_2010_2014_cjtominimo_precip_insolacao");
				ReportingCommand.generateLatexTable(results, exp);
				assertTrue(true);
				mongoUtil.closeClient();

			} catch (Exception e) {
				e.printStackTrace();
				fail("error processing machine learning");
			}
		}
	
	@Ignore
    public void testExportResults() {

        try {
            String collectionName = "results";
            assertTrue("Error in init mongoutil", mongoUtil.init("test", collectionName));
            StringBuffer output = new StringBuffer();
            output.append("<tr><td>stationname</td><td>coefficient</td><td>mae</td><td>rmse</td><td>model</td></tr>");
            ArrayList<Document> results = mongoUtil.getResults("MP5");
            for (Document doc: results) {
                String stationName = doc.get("stationname").toString();
                String coefficient = doc.get("coefficient").toString();
                String mae = doc.get("mae").toString();
                String rmse = doc.get("rmse").toString();
                String model = doc.get("model").toString();
                String line ="<tr>" +
                        "<td>"+stationName+"</td>" +
                        "<td>"+coefficient+"</td>" +
                        "<td>"+mae+"</td>" +
                        "<td>"+rmse+"</td>" +
                        "<td>"+model+"</td>" +
                        "</tr>";
                output.append(line);

            }
            assertTrue(true);
            mongoUtil.closeClient();

            System.out.println(output.toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail("error processing machine learning");
        }


    }

	@Ignore
	    public void testCountResultsByStation() {

        try {
            String collectionName = "results";
            assertTrue("Error in init mongoutil", mongoUtil.init("test", collectionName));
            StringBuffer output = new StringBuffer();
            output.append("<tr><td>stationname</td><td>count</td></tr>");
            ArrayList<Document> results = mongoUtil.getCountResultsByStation();
            for (Document doc: results) {
                String stationName = doc.get("_id").toString();
                String count = doc.get("count").toString();
                String line = stationName+";" + count + "\n" 
                        ;
                output.append(line);

            }
            assertTrue(true);
            mongoUtil.closeClient();
			ReportUtil.exportToFile("files\\reports\\resultsStationsCount.csv",output);
            System.out.println(output.toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail("error processing machine learning");
        }


    }

	@Ignore
    public void testGetAllResults() {

        try {
            String collectionName = "results";
            assertTrue("Error in init mongoutil", command.mongoUtil.init("test", collectionName));
            assertTrue(true);
            command.generateAllResultsInCsv("files\\reports\\summaries\\allResults.csv");
            command.mongoUtil.closeClient();
        } catch (Exception e) {
            e.printStackTrace();
            fail("error processing machine learning");
        }


    }

	@Ignore
    public void testGetCoefficients() {

        try {
            String collectionName = "results";
            assertTrue("Error in init mongoutil", command.mongoUtil.init("test", collectionName));
            assertTrue(true);
            command.generateCoefficentsInCsv("files\\reports\\summaries\\allCoefficients.csv");
            command.mongoUtil.closeClient();
        } catch (Exception e) {
            e.printStackTrace();
            fail("error processing machine learning");
        }


    }




}

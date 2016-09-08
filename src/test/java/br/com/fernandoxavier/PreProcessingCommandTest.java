package br.uniriotec.ppgi.fernandoxavier;

import br.uniriotec.ppgi.fernandoxavier.utils.MongoUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.REXP;

import java.util.ArrayList;

public class PreProcessingCommandTest extends TestCase {

    PreProcessingCommand command;

    public PreProcessingCommandTest() {
        command = new PreProcessingCommand();

    }

    public static Test suite()
    {
        return new TestSuite( PreProcessingCommandTest.class );
    }

/*
    public void testDataframe() {
        try {
            command.getDatasetsFromDB("ac_cruzeirodosul");
            ArrayList<StationVO> stations = command.getStations();
            for(StationVO station: stations) {
                double altitude = station.getAltitude();
                double latitude = station.getLatitude();
                String year="0";
                String[] columns = station.getHeader().split(";");
                StringBuffer tempMin = new StringBuffer();
                StringBuffer tempMax = new StringBuffer();
                StringBuffer umid = new StringBuffer();
                StringBuffer vento = new StringBuffer();
                StringBuffer insol = new StringBuffer();
                StringBuffer nebul = new StringBuffer();


                for (String instance: station.getInstances()) {
                  //  System.out.println(instance);
                    String[] measures = instance.split(";",columns.length);
                    String[] dateMeasure = measures[1].split("/");
                   // System.out.println(measures[1]);
                    if (!dateMeasure[2].equals(year)) {
                        System.out.println("MIN: "+ tempMin);
                        System.out.println("MAX: " + tempMax);
                        System.out.println("VEN: " + vento);
                        System.out.println("INS: " + insol);
                        System.out.println("UMI: " + umid);
                        System.out.println("NEB: " + nebul);



                        tempMin = new StringBuffer();
                        tempMax = new StringBuffer();
                        vento = new StringBuffer();
                        insol = new StringBuffer();
                        umid = new StringBuffer();
                        nebul = new StringBuffer();
                        year = dateMeasure[2];
                    }
                    tempMin.append(measures[17]+",");
                    tempMax.append(measures[15]+",");
                    vento.append(measures[4]+",");
                    insol.append(measures[9]+",");
                    nebul.append(measures[10]+",");
                    umid.append(measures[18]+",");

                }
            }
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error " + e.getMessage());
        }
    }
*/

/*

    public void testExecuteOrigin() {
        try {
            command.setFrom(2010);
            command.setTo(2014);
            command.setExperiment("origin");
            command.execute();
            MongoUtil mongoUtil = new MongoUtil();
            mongoUtil.init("test", "stations");
            System.out.println("Foram lidas " + mongoUtil.getStationsWithEVP().size());
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error " + e.getMessage());
        }
    }
    */

/*
    public void testExecuteZeros() {
        try {
            command.setFrom(2000);
            command.setTo(2014);
            command.setExperiment("zeros");
            command.execute();
            MongoUtil mongoUtil = new MongoUtil();
            mongoUtil.init("test", "datasets");
            System.out.println("Foram lidas " + mongoUtil.getStationsWithEVP().size());
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error " + e.getMessage());
        }
    }

	    public void testExecuteNebulUmid() {
        try {
            command.setFrom(2010);
            command.setTo(2014);
            command.setExperiment("nebulumid");
            command.execute();
            MongoUtil mongoUtil = new MongoUtil();
            mongoUtil.init("test", "datasets");
            System.out.println("Foram lidas " + mongoUtil.getStationsWithEVP().size());
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error " + e.getMessage());
        }
    }
*/
	    public void testExecuteNebul() {
        try {
            command.setFrom(2010);
            command.setTo(2014);
            command.setExperiment("nebul");
            command.execute();
            MongoUtil mongoUtil = new MongoUtil();
            mongoUtil.init("test", "datasets");
            System.out.println("Foram lidas " + mongoUtil.getStationsWithEVP().size());
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error " + e.getMessage());
        }
    }

	/*
	    public void testExecuteInsol() {
        try {
            command.setFrom(2010);
            command.setTo(2014);
            command.setExperiment("insol");
            command.execute();
            MongoUtil mongoUtil = new MongoUtil();
            mongoUtil.init("test", "datasets");
            System.out.println("Foram lidas " + mongoUtil.getStationsWithEVP().size());
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error " + e.getMessage());
        }
    }

*/
/*
    public void testExecuteAll() {
        try {
            command.setExperiment("all");
            command.execute();
            MongoUtil mongoUtil = new MongoUtil();
            mongoUtil.init("test", "stations");
            System.out.println("Foram lidas " + mongoUtil.getStationsWithEVP().size());
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error " + e.getMessage());
        }
    }
*/

/*
    public void testGetDatasets() {
        assertTrue("Data not found", command.getDatasetsFromDB()>0);
    }


    public void testCalculatePenman() {

        try {
            command.calculatePenman("ac_riobranco.csv",-9.96d,160, 2000, 2009);
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("error in penman estimation");
        }

    }

    public void testCalculatePenmanAll() {

        try {
            command.calculatePenmanAllInstances("ac_cruzeirodosul.csv",-7.6d,170);
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("error in penman estimation");
        }

    }
*/

}

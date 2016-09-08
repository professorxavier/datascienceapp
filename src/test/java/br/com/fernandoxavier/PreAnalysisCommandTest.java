package br.uniriotec.ppgi.fernandoxavier;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.bson.Document;
import org.junit.Ignore;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RVector;
import org.rosuda.JRI.Rengine;

import java.io.File;
import java.lang.annotation.Documented;
import java.util.Enumeration;

public class PreAnalysisCommandTest extends TestCase {

    PreAnalysisCommand command;

    public PreAnalysisCommandTest() {
        command = new PreAnalysisCommand();

    }

    public static Test suite()
    {
        return new TestSuite( PreAnalysisCommandTest.class );
    }


    @Ignore
    public void testExecute() {
        try {
            command.execute();
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testExtractInformations() {
//        String years = "'2006','2007','2008','2009','2010','2011','2012','2013','2014'";
        String years = "'2008','2009','2010','2011','2012','2013','2014'";
       // command.checkAvailabiltyData(years);
//        years = "'2007','2008','2009','2010','2011','2012','2013','2014'";
//        command.checkAvailabiltyData(years);
 //       years = "'2006','2007','2008','2009','2010','2011','2012','2013','2014'";
 //      command.checkAvailabiltyData(years);
//        years = "'2005','2006','2007','2008','2009','2010','2011','2012','2013','2014'";
 //       command.checkAvailabiltyData(years);
 //       years = "'2004','2005','2006','2007','2008','2009','2010','2011','2012','2013','2014'";
 //       command.checkAvailabiltyData(years);
//        years = "'2003','2004','2005','2006','2007','2008','2009','2010','2011','2012','2013','2014'";
//        command.checkAvailabiltyData(years);
//        years = "'2002','2003','2004','2005','2006','2007','2008','2009','2010','2011','2012','2013','2014'";
//        command.checkAvailabiltyData(years);
 //       years = "'2001','2002','2003','2004','2005','2006','2007','2008','2009','2010','2011','2012','2013','2014'";
 //       command.checkAvailabiltyData(years);
      //  years = "'2000','2001','2002','2003','2004','2005','2006','2007','2008','2009','2010','2011','2012','2013','2014'";
	    years = "'2010','2011','2012','2013','2014'";
        command.checkAvailabiltyData(years);
		command.generateMissingDataReport();

    }
}

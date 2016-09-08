package br.uniriotec.ppgi.fernandoxavier;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.bson.Document;

public class DatasetLoadCommandTest extends TestCase {

    DatasetLoadCommand command;

    public DatasetLoadCommandTest() {
        command = new DatasetLoadCommand();

    }

    public static Test suite()
    {
        return new TestSuite( DatasetLoadCommandTest.class );
    }

    public void testExecute() {
        try {
            command.execute();
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error " + e.getMessage());
        }
    }
}

package br.uniriotec.ppgi.fernandoxavier;

import com.mongodb.client.MongoCollection;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBTest extends TestCase {

    MongoClient mongoClient;

    public MongoDBTest() {
        mongoClient = new MongoClient();

    }

    public static Test suite()
    {
        return new TestSuite( MongoDBTest.class );
    }

    public void testMongoDB() {
        try {
            MongoDatabase db = mongoClient.getDatabase("test");
            db.createCollection("testDriver");
            MongoCollection mongoCollection = db.getCollection("testDriver");
            mongoCollection.insertOne(new Document("name", "Fernando"));
            mongoCollection.drop();
            assertTrue(true);
        } catch (Exception e) {
            fail("Erro na conexao com o mongodb " + e.getMessage());
        }
    }
}

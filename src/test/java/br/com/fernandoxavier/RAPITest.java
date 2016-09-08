package br.uniriotec.ppgi.fernandoxavier;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

public class RAPITest extends TestCase {

    Rengine rengine;

    public RAPITest() {
        System.out.println("PATH " + System.getProperty("java.library.path"));
        rengine = new Rengine(new String[] { "--vanilla" }, false, null);

    }

    public static Test suite()
    {
        return new TestSuite( RAPITest.class );
    }

    public void testAPI() {
        REXP result = rengine.eval("1+1");
        System.out.println(result.toString());
        assertEquals("Resultados diferentes", 2.0d, result.asDouble());
        result = rengine.eval("1+4");
        System.out.println(result.toString());
     //   assertEquals("Resultados diferentes", 4.0d, result.asDouble());
        assertTrue("Resultados iguais", result.asDouble()!=4.0d);
    }
}

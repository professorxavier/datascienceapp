package br.uniriotec.ppgi.fernandoxavier.utils;

import br.uniriotec.ppgi.fernandoxavier.StationVO;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Ignore;
import br.uniriotec.ppgi.fernandoxavier.ResultVO;


import java.util.ArrayList;

public class ReportUtilTest extends TestCase {

    ReportUtil reportUtil;

    public ReportUtilTest() {
        reportUtil = new ReportUtil();

    }

    public static Test suite()
    {
        return new TestSuite( ReportUtilTest.class );
    }

	@Ignore
    public void testPrintKml() {
        try {
            ArrayList<ResultVO> stations = new ArrayList<ResultVO>();

            //creating data test
            ResultVO station = new ResultVO();
            station.setStationName("ac_cruzeirodosul");
            station.setAltitude(170d);
            station.setLongitude(-72.66d);
            station.setLatitude(-7.6d);
            stations.add(station);

            ReportUtil.pointsHeatMap(stations, "coefficient", "unittest");


        } catch (Exception e) {
            fail("Erro  " + e.getMessage());
        }
    }

	@Ignore
    public void testExportMissingStatsToLatex() {
        ReportUtil.exportMissingStatsToLatex();
    }

    public void testExportMissingStatsToCSV() {
        ReportUtil.exportMissingStatsToCSV();
    }
	


}

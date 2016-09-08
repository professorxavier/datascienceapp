package br.uniriotec.ppgi.fernandoxavier.utils;

import br.uniriotec.ppgi.fernandoxavier.ResultVO;
import br.uniriotec.ppgi.fernandoxavier.StationVO;
import org.bson.Document;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ReportUtil {

    public static void generateKML(ArrayList<StationVO> stationsVO) {

        /*<Placemark id="2012 Jan 15 13:40:16.40 UTC">
                <name>M 5.9 - 2012 Jan 15, SOUTH SHETLAND ISLANDS</name>
                <magnitude>5.9</magnitude>
                <Point>
                    <coordinates>-56.072,-60.975,0</coordinates>
                </Point>
            </Placemark>*/

        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://earth.google.com/kml/2.0\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
                "    <Document>\n" +
                "        <name>Stations</name>\n" +
                "        <atom:author>\n" +
                "            <atom:name>Fernando Xavier</atom:name>\n" +
                "        </atom:author>\n" +
                "        <atom:link href=\"http://earthquake.usgs.gov\"/>\n" +
                "        <Folder>";
        String footer = " </Folder>\n" +
                "    </Document>\n" +
                "</kml>";
        StringBuffer kml = new StringBuffer();
        for (StationVO station: stationsVO) {

            String template = "<Placemark id=\"%s\">\n" +
                    "                <name>%s</name>\n" +
                    "                <magnitude>%sf</magnitude>\n" +
                    "                <Point>\n" +
                    "                    <coordinates>%s,%s,0</coordinates>\n" +
                    "                </Point>\n" +
                    "            </Placemark>";
            kml.append(String.format(template,station.getStationName(),
                    station.getStationName(),station.getCoefficient(),station.getLongitude(),
                    station.getLatitude()));
        }

        System.out.println(header+kml.toString()+footer);

    }

    public static void pointsHeatMap(ArrayList<ResultVO> resultsVO, String type, String experiment) {

        StringBuffer kml = new StringBuffer();
        for (ResultVO station: resultsVO) {

            if (type.equals("coefficient")) {
                String template = "{lat: %s, lng:%s, count: %s},";
                kml.append(String.format(template, station.getLatitude(),
                        station.getLongitude(), station.getCoefficient()));
            } else if (type.equals("mae")) {
                String template = "{lat: %s, lng:%s, count: %s},";
                kml.append(String.format(template, station.getLatitude(),
                        station.getLongitude(), station.getMae()));
            } else if (type.equals("rmse")) {
                String template = "{lat: %s, lng:%s, count: %s},";
                kml.append(String.format(template, station.getLatitude(),
                        station.getLongitude(), station.getRmse()));
            }
        }

        exportToFile("files\\reports\\maps\\"+ type+"_"+experiment +".txt", kml);

        System.out.println(kml.toString());

    }

    public static void exportToFile(String filename, StringBuffer content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(content.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportMissingStatsToLatex() {

        MongoUtil mongoUtil = new MongoUtil();
        mongoUtil.init("test","missingstats");
        ArrayList<Document> documents = mongoUtil.loadMissingDataInformation();
        StringBuffer table = new StringBuffer();
        String[] attributes = {"stationname","VelocidadeVentoMedia","VelocidadeVentoMaximaMedia","EvaporacaoPiche",
                "EvapoBHPotencial","EvapoBHReal","InsolacaoTotal","NebulosidadeMedia","NumDiasPrecipitacao",
                "PrecipitacaoTotal","PressaoNivelMarMedia","PressaoMedia","TempMaximaMedia",
                "TempCompensadaMedia","TempMinimaMedia","UmidadeRelativaMedia","VisibilidadeMedia"};

        printHeader(attributes);

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        for (Document doc: documents) {
            for (int i=0; i<attributes.length;i++) {
                String attribute = attributes[i];
                Object value= doc.get(attribute);
                if (value==null)
                    value="0";
//                System.out.println(value);
                if (!attribute.equals("stationname")) {
                    double doubleValue = Double.valueOf(value.toString());
                    value = df.format(doubleValue * 100);
                }
                if (i==attributes.length-1)
                    table.append(value);
                else
                    table.append(value + " & ");
            }
            table.append(" \\\\ \\hline \n");
        }
		exportToFile("missing.txt", table);

//        System.out.println(table);
//.toString().replace(",",".")

        mongoUtil.closeClient();

    }

    public static void exportMissingStatsToCSV() {

        MongoUtil mongoUtil = new MongoUtil();
        mongoUtil.init("test","missingstats");
        ArrayList<Document> documents = mongoUtil.loadMissingDataInformation();
        StringBuffer table = new StringBuffer();
        String[] attributes = {"stationname","VelocidadeVentoMedia","VelocidadeVentoMaximaMedia","EvaporacaoPiche",
                "EvapoBHPotencial","EvapoBHReal","InsolacaoTotal","NebulosidadeMedia","NumDiasPrecipitacao",
                "PrecipitacaoTotal","PressaoNivelMarMedia","PressaoMedia","TempMaximaMedia",
                "TempCompensadaMedia","TempMinimaMedia","UmidadeRelativaMedia","VisibilidadeMedia"};
		table.append("stationname;VelocidadeVentoMedia;VelocidadeVentoMaximaMedia;EvaporacaoPiche;                EvapoBHPotencial;EvapoBHReal;InsolacaoTotal;NebulosidadeMedia;NumDiasPrecipitacao;                PrecipitacaoTotal;PressaoNivelMarMedia;PressaoMedia;TempMaximaMedia;                TempCompensadaMedia;TempMinimaMedia;UmidadeRelativaMedia;VisibilidadeMedia");

        printHeaderCSV(attributes);

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        for (Document doc: documents) {
            for (int i=0; i<attributes.length;i++) {
                String attribute = attributes[i];
                Object value= doc.get(attribute);
                if (value==null)
                    value="0";
                if (!attribute.equals("stationname")) {
                    double doubleValue = Double.valueOf(value.toString());
                    value = df.format(doubleValue * 100);
                }
                if (i==attributes.length-1)
                    table.append(value);
                else
                    table.append(value + ";");
            }
            table.append("\n");
        }

        System.out.println(table.toString().replace(",","."));
		exportToFile("missing.csv",table);


        mongoUtil.closeClient();

    }

    private static void printHeader(String[] attributes) {
        StringBuffer header = new StringBuffer();

        for (int i=0; i<attributes.length;i++) {
            String attribute = attributes[i];
            if (i==attributes.length-1)
                header.append("\\textbf{"+attribute + "} ");
            else
                header.append("\\textbf{"+attribute + "} & ");
        }

        header.append("\\\\ \\hline");
        System.out.println(header.toString());
    }

    private static void printHeaderCSV(String[] attributes) {
        StringBuffer header = new StringBuffer();

        for (int i=0; i<attributes.length;i++) {
            String attribute = attributes[i];
            if (i==attributes.length-1)
                header.append(attribute);
            else
                header.append(attribute + ";");
        }

        header.append("\\\\ \\hline");
        System.out.println(header.toString());
    }


    public static void generateResultsTableLatex(ArrayList<ResultVO> resultsVO, String nameExperiment) {

        StringBuffer results = new StringBuffer();
        String fileToExport = "files\\reports\\results_"+ nameExperiment +".txt";
        for (ResultVO resultVO: resultsVO) {

            String resultLine = resultVO.getStationName().replace("_","\\_") +
                    " & " + Double.toString(resultVO.getCoefficient()).substring(0,8) +
                    " & " + Double.toString(resultVO.getMae()).substring(0,8) +
                    " & " + Double.toString(resultVO.getRmse()).substring(0,8) +
                    " & " + Double.toString(resultVO.getMaer()).substring(0,8) +
                    " & " + Double.toString(resultVO.getRmser()).substring(0,8) +
                   // " & " + resultVO.getRae() +
                   // " & " + resultVO.getRrse() +
                   // " & " + resultVO.getNumInstances() +
                    " & " + resultVO.getModel().replace("\n","") +
                    " \\\\ \\hline" + "\n";
            results.append(resultLine);
        }

        exportToFile(fileToExport, results);

    }

    public static void generateResultsinCsvFile(ArrayList<ResultVO> resultsVO, String nameExperiment) {
        StringBuffer results = new StringBuffer();
        String fileToExport = "files\\reports\\results_"+ nameExperiment +".csv";
        results.append("station;coefficient;mae;rmse;maer;rmser;rae;rrse;numinstances;latitude;longitude;altitude;model\n");
        for (ResultVO resultVO: resultsVO) {

            String resultLine = resultVO.getStationName() +
                    ";" + resultVO.getCoefficient() +
                    ";" + resultVO.getMae() +
                    ";" + resultVO.getRmse() +
                    ";" + resultVO.getMaer() +
                    ";" + resultVO.getRmser() +
                    ";" + resultVO.getRae() +
                    ";" + resultVO.getRrse() +
                    ";" + resultVO.getAverageEvp() +
                    ";" + resultVO.getNumInstances() +
                    ";" + resultVO.getLatitude() +
                    ";" + resultVO.getLongitude() +
                    ";" + resultVO.getAltitude() +
                    ";" + resultVO.getModel().replace("\n","") +
                    "\n";
            results.append(resultLine);
        }

        exportToFile(fileToExport, results);
    }

    public static void generateAllResultsinCsvFile(String filename, ArrayList<Document> results) {
        StringBuffer lines = new StringBuffer();

         for (Document result: results) {

            String resultLine = result.get("stationname") +
                    ";" + result.get("experiment") +
                    ";" + result.get("coefficient") +
                    ";" + result.get("rmse") +
                    ";" + result.get("mae") +
                    ";" + result.get("rmser") +
                    ";" + result.get("maer") +
                    ";" + result.get("rrse") +
                    ";" + result.get("rae") +
                    ";" + result.get("instances") +
                    ";" + result.get("latitude") +
                    ";" + result.get("longitude") +
                    ";" + result.get("model").toString().replace("\n","") +
                    "\n";
            lines.append(resultLine);
        }

        exportToFile(filename, lines);

    }

    public static void generateCoefficientsListinCsv(String filename, ArrayList<Document> results) {
        StringBuffer lines = new StringBuffer();
        lines.append("station;mp5_dados_all;mp5_dados_2000_2014_zeros;mp5_dados_2010_2014\n");

        for (Document result: results) {

            String resultLine = result.get("_id").toString()+";%s;%s;%s";
            ArrayList<Document> lists = (ArrayList<Document>) result.get("list");
            String val1="0";
            String val2="0";
            String val3="0";
            for (Document doc: lists) {
                if (doc.get("exp").toString().equals("mp5_dados_all")) {
                    val1 = doc.get("coef").toString();
                } else if (doc.get("exp").toString().equals("mp5_dados_2000_2014_zeros")) {
                    val2 = doc.get("coef").toString();
                } else if (doc.get("exp").toString().equals("mp5_dados_2010_2014")) {
                    val3 = doc.get("coef").toString();
                }
            }

            resultLine += "\n";
            resultLine = String.format(resultLine, val1, val2, val3);
            lines.append(resultLine);
        }

        exportToFile(filename, lines);
    }
}

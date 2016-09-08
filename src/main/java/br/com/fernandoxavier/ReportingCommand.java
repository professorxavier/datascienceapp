package br.uniriotec.ppgi.fernandoxavier;


import br.uniriotec.ppgi.fernandoxavier.utils.MongoUtil;
import br.uniriotec.ppgi.fernandoxavier.utils.ReportUtil;
import org.bson.Document;

import java.util.ArrayList;

public class ReportingCommand implements OperationCommand {

    public MongoUtil mongoUtil;

    public ReportingCommand() {
        mongoUtil = new MongoUtil();

    }

    public void execute() {
        System.out.println(getClass().getSimpleName() + " says: I receive the request and I will execute");
    }

    public static void generatePointsToHeatMap(ArrayList<ResultVO> resultsVO, String nameExperiment) {
        ReportUtil.pointsHeatMap(resultsVO,"coefficient",nameExperiment);
        ReportUtil.pointsHeatMap(resultsVO,"mae",nameExperiment);
        ReportUtil.pointsHeatMap(resultsVO,"rmse",nameExperiment);
    }

    public void undo() {
        System.out.println("I need to undo the operations");
    }

    public static void generateLatexTable(ArrayList<ResultVO> resultsVO, String nameExperiment) {

        ReportUtil.generateResultsTableLatex(resultsVO, nameExperiment);

    }

    public static void generateResultsInCsv(ArrayList<ResultVO> resultsVO, String nameExperiment) {
        ReportUtil.generateResultsinCsvFile(resultsVO, nameExperiment);

    }

    public void generateAllResultsInCsv(String filename) {
        ArrayList<Document> results = mongoUtil.getAllResults();

        ReportUtil.generateAllResultsinCsvFile(filename,results);

    }

    public void generateCoefficentsInCsv(String filename) {
        ArrayList<Document> results = mongoUtil.getCoefficients();

        ReportUtil.generateCoefficientsListinCsv(filename,results);

    }


}

package br.uniriotec.ppgi.fernandoxavier;

import br.uniriotec.ppgi.fernandoxavier.utils.MongoUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.bson.Document;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.trees.M5P;
import weka.classifiers.trees.m5.RuleNode;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ProcessingCommandTest extends TestCase {

    ProcessingCommand command;
    MongoUtil mongoUtil;

    public ProcessingCommandTest() {
        command = new ProcessingCommand();
        mongoUtil = new MongoUtil();


    }

    public static Test suite()
    {
        return new TestSuite( ProcessingCommandTest.class );
    }

    public void testprocessMachineLearningForOne() {

        try {

            String stationName = "ac_riobranco";
            assertTrue("Error in init mongoutil", mongoUtil.init("test", "datasets"));
            ArrayList<ResultVO> resultsVO = new ArrayList<ResultVO>();
            ResultVO resultVO = command.processMachineLearning(stationName, "");
            StationVO stationVO = mongoUtil.loadSingleStation(stationName);
            resultsVO.add(resultVO);

            ReportingCommand.generatePointsToHeatMap(resultsVO, "unittest");
            mongoUtil.closeClient();

            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("error processing machine learning");
        }

    }

    public void testprocessOtherAlgorithmForOne() {

        try {

            String stationName = "ac_riobranco";
            String algorithm = "REPTree";
            assertTrue("Error in init mongoutil", mongoUtil.init("test", "datasets"));
            ArrayList<StationVO> stationsVO = new ArrayList<StationVO>();

            ResultVO resultVO = command.processMachineLearning(stationName, "", algorithm);
           /* StationVO stationVO = mongoUtil.loadSingleStation(stationName);
            stationVO.setCoefficient(resultVO.getCoefficient());
            stationVO.setMae(resultVO.getMae());
            stationVO.setRmse(resultVO.getRmse());
            stationsVO.add(stationVO);
            */

//            ReportingCommand.generatePointsToHeatMap(stationsVO);
            mongoUtil.closeClient();

            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("error processing machine learning");
        }

    }


    public void testprocessMachineLearningForAll() {

        try {
            String collectionName = "datasets";
            ArrayList<ResultVO> resultsVO = new ArrayList<ResultVO>();
            assertTrue("Error in init mongoutil", mongoUtil.init("test", collectionName));
            ArrayList<Document> stations = mongoUtil.getStationsWithEVP();
 //           String nameExperiment="mp5_dados_2010_2014_nebul";
//          String nameExperiment="mp5_dados_2010_2014_nebulumid";
//            String nameExperiment="mp5_dados_2010_2014_cjtominimo";
//         String nameExperiment="mp5_dados_2010_2014_cjtominimo_insol";
//            String nameExperiment="mp5_dados_2010_2014_cjtominimo_precip_apenas";
          String nameExperiment="mp5_dados_2010_2014_cjtominimo_precip_insolacao";
            for (Document doc: stations) {
                String stationName = doc.get("stationname").toString();


                ResultVO resultVO =  command
                        .processMachineLearning(stationName, nameExperiment);

               resultsVO.add(resultVO);

            }
            assertTrue(true);
            mongoUtil.closeClient();


            ReportingCommand.generatePointsToHeatMap(resultsVO, nameExperiment);
            ReportingCommand.generateResultsInCsv(resultsVO, nameExperiment);
            ReportingCommand.generateLatexTable(resultsVO,nameExperiment);

        } catch (Exception e) {
            e.printStackTrace();
            fail("error processing machine learning");
        }

    }

    public void testCountDatasetsEVP() {
        String collectionName = "datasets";
        assertTrue("Error in init mongoutil", mongoUtil.init("test", collectionName));
        ArrayList<Attribute> attrs = new ArrayList<Attribute>();
        ArrayList<Document> results = mongoUtil.getInstancesWithEVP("ac_cruzeirodosul");

        //0 Estacao;1 Data;2 Hora;3 DirecaoVento;4 VelocidadeVentoMedia; 5 VelocidadeVentoMaximaMedia;
        //6 EvaporacaoPiche;7 EvapoBHPotencial;8 EvapoBHReal;9 InsolacaoTotal;10 NebulosidadeMedia;
        //11 NumDiasPrecipitacao; 12 PrecipitacaoTotal;13 PressaoNivelMarMedia;14 PressaoMedia;15 TempMaximaMedia;
        // 16 TempCompensadaMedia;17 TempMinimaMedia;18 UmidadeRelativaMedia;19 VisibilidadeMedia;
        //Integer[] ignore = {0,1,2,3,5,6,19};
        Integer[] ignore = {0,1,2,3,5,6,7,8,11,13,14,19};
        String header = results.get(0).getString("header");
        System.out.println(header);
        String[] cols = header.split(";");
        Attribute attr = null;
        for (int i=0;i<cols.length;i++) {
            if (Arrays.asList(ignore).contains(i))
                continue;
            attr = new Attribute(cols[i]);
            attrs.add(attr);
        }
        attr = new Attribute("evp");
        attrs.add(attr);

        Instances race = new Instances("race", attrs, 0);

        for (Document result: results) {

            Document doc = (Document) result.get("instances");
            System.out.println(doc.get("Data"));
            Instance inst = new DenseInstance(attrs.size());
            inst.setDataset(race);
            for (int i = 0; i < attrs.size(); i++) {
                String coluna = attrs.get(i).name();
                Object valor = doc.get(coluna);
                System.out.println( coluna + " - " + valor);
                double data = 0;
                if (valor!=null) {
                    if (!valor.toString().trim().equals(""))
                        data = Double.valueOf(valor.toString());
                }
                inst.setValue(i, data);
            }
            race.add(inst);
        }
        try {
            if (race.classIndex() == -1)
                race.setClassIndex(race.numAttributes() - 1);
            System.out.println("Atributos " + race.numAttributes());
            //for (int i=0;i<race.numAttributes();i++) {
            //   System.out.println(race.attribute(i).name());
            //   System.out.println(race.attribute(i).numValues());
            //}

            M5P tree = new M5P();         // new instance of tree
            tree.buildClassifier(race);   // build classifier
            Evaluation eval = new Evaluation(race);
            eval.crossValidateModel(tree, race, 2, new Random(1));
            System.out.println(eval.toSummaryString());
            RuleNode rules = tree.getM5RootNode();
            ArrayList<Prediction> predictions = eval.predictions();
            for (Prediction p: predictions) {
                System.out.println(p.actual()+";"+p.predicted());
            }
            System.out.println(rules.printLeafModels());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}

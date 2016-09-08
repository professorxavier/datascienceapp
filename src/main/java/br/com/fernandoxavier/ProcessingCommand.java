package br.uniriotec.ppgi.fernandoxavier;


import br.uniriotec.ppgi.fernandoxavier.utils.MongoUtil;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.M5P;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.m5.RuleNode;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.functions.*;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ProcessingCommand implements OperationCommand {

    MongoUtil mongoUtil;

    public ProcessingCommand() {
        mongoUtil = new MongoUtil();

    }

    public void execute() {
        System.out.println(getClass().getSimpleName() + " says: I receive the request and I will execute");
    }

    public ResultVO processMachineLearning(String stationname, String experiment) {
        ArrayList<Attribute> attrs = new ArrayList<Attribute>();
        ArrayList<Document> instances = getInstances(stationname);

        //0 Estacao;1 Data;2 Hora;3 DirecaoVento;4 VelocidadeVentoMedia; 5 VelocidadeVentoMaximaMedia;
        //6 EvaporacaoPiche;7 EvapoBHPotencial;8 EvapoBHReal;9 InsolacaoTotal;10 NebulosidadeMedia;
        //11 NumDiasPrecipitacao; 12 PrecipitacaoTotal;13 PressaoNivelMarMedia;14 PressaoMedia;15 TempMaximaMedia;
        // 16 TempCompensadaMedia;17 TempMinimaMedia;18 UmidadeRelativaMedia;19 VisibilidadeMedia;
        //Integer[] ignore = {0,1,2,3,5,6,19};
 //       Integer[] ignore = {0,1,2,3,5,6,7,8,9,11,12,13,14,16,18,19}; //com cjto minimo
//        Integer[] ignore = {0,1,2,3,5,6,7,8,11,12,13,14,16,18,19}; //com insolacao
 //       Integer[] ignore = {0,1,2,3,5,6,7,8,9,11,13,14,16,18,19}; //com precipitacao
        Integer[] ignore = {0,1,2,3,5,6,7,8,11,13,14,16,18,19}; //com precipitacao e insolacao
        String header = instances.get(0).getString("header");
        // System.out.println(header);
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

        for (Document result: instances) {

            Document doc = (Document) result.get("instances");
            Instance inst = new DenseInstance(attrs.size());
            inst.setDataset(race);
            for (int i = 0; i < attrs.size(); i++) {
                String coluna = attrs.get(i).name();
                Object valor = doc.get(coluna);
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

            M5P tree = new M5P();         // new instance of tree
            tree.buildClassifier(race);   // build classifier
            Evaluation eval = new Evaluation(race);
            eval.crossValidateModel(tree, race, 10, new Random(1));

            RuleNode rules = tree.getM5RootNode();
            ArrayList<Prediction> predictions = eval.predictions();

            ResultVO resultVO = getResultVO(stationname, experiment, eval, rules, "MP5", predictions);

            saveResults(stationname, predictions,eval.toSummaryString(), rules, resultVO, experiment, eval);

            return resultVO;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultVO processMachineLearning(String stationname, String experiment, String algorithm) {
        ArrayList<Attribute> attrs = new ArrayList<Attribute>();
        ArrayList<Document> instances = getInstances(stationname);

        //0 Estacao;1 Data;2 Hora;3 DirecaoVento;4 VelocidadeVentoMedia; 5 VelocidadeVentoMaximaMedia;
        //6 EvaporacaoPiche;7 EvapoBHPotencial;8 EvapoBHReal;9 InsolacaoTotal;10 NebulosidadeMedia;
        //11 NumDiasPrecipitacao; 12 PrecipitacaoTotal;13 PressaoNivelMarMedia;14 PressaoMedia;15 TempMaximaMedia;
        // 16 TempCompensadaMedia;17 TempMinimaMedia;18 UmidadeRelativaMedia;19 VisibilidadeMedia;
        //Integer[] ignore = {0,1,2,3,5,6,19};
        Integer[] ignore = {0,1,2,3,5,6,7,8,11,13,14,19};
        String header = instances.get(0).getString("header");
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

        for (Document result: instances) {

            Document doc = (Document) result.get("instances");
            Instance inst = new DenseInstance(attrs.size());
            inst.setDataset(race);
            for (int i = 0; i < attrs.size(); i++) {
                String coluna = attrs.get(i).name();
                Object valor = doc.get(coluna);
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

            REPTree classifier = (REPTree) getClassifier(algorithm);
            classifier.buildClassifier(race);   // build classifier


            //executing machine learning
            Evaluation eval = new Evaluation(race);
            eval.crossValidateModel(classifier, race, 2, new Random(1));



            //getting results
            System.out.println(classifier.toString());
            System.out.println(classifier.toSource("evp"));
/*            RuleNode rules =
            ArrayList<Prediction> predictions = eval.predictions();

            //saving results
            ResultVO resultVO = getResultVO(stationname, experiment, eval, rules, "MP5");
            saveResults(stationname, predictions,eval.toSummaryString(), rules, resultVO);

            return resultVO;
            */
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private AbstractClassifier getClassifier(String algorithm) {
        AbstractClassifier classifier = null;

        if (algorithm.equals("MultilayerPerceptron")) {
            classifier = new MultilayerPerceptron();
        }
        if (algorithm.equals("REPTree")) {
            classifier = new REPTree();

        }
        if (algorithm.equals("DecisionStump")) {
            classifier = new DecisionStump();
        }
        if (algorithm.equals("Bagging")) {
            classifier = new Bagging();
        }
        if (algorithm.equals("GaussianProcesses")) {
            classifier = new GaussianProcesses();
        }

        return classifier;
    }


    private ArrayList<Document> getInstances(String stationname) {
        String collectionName = "datasets";
        mongoUtil.init("test", collectionName); //todo paramter this
        return mongoUtil.getInstancesWithEVP(stationname);
    }

    private ResultVO getResultVO(String stationname, String experiment, Evaluation eval, RuleNode rules,
                                 String algorithm, ArrayList<Prediction> predictions) throws Exception {

        double valuesAverage = getValuesAverage(predictions, eval.numInstances());
		double predictionAverage = getPredictionAverage(predictions, eval.numInstances());
        StationVO stationVO = mongoUtil.loadSingleStation(stationname);
        ResultVO resultVO = new ResultVO();
        resultVO.setCoefficient(eval.correlationCoefficient());
        resultVO.setMae(eval.meanAbsoluteError());
        resultVO.setRmse(eval.rootMeanSquaredError());
        resultVO.setMaer(eval.meanAbsoluteError()/valuesAverage);
        resultVO.setRmser(eval.rootMeanSquaredError()/valuesAverage);
        resultVO.setRae(eval.relativeAbsoluteError());
        resultVO.setRrse(eval.rootRelativeSquaredError());
        resultVO.setStationName(stationVO.getStationName());
        resultVO.setLatitude(stationVO.getLatitude());
        resultVO.setLongitude(stationVO.getLongitude());
        resultVO.setAltitude(stationVO.getAltitude());
        resultVO.setAlgorithm(algorithm);
        resultVO.setModel(rules.printLeafModels());
        resultVO.setExperiment(experiment);
        resultVO.setNumInstances(eval.numInstances());
		resultVO.setAverageEvp(predictionAverage);
        return resultVO;
    }

	private double getPredictionAverage(ArrayList<Prediction> predictions, double instances) {

        double totalActual = 0.0;
        double averageActual = 0.0;
        for (Prediction p: predictions) {
            totalActual += p.predicted();
        }

        averageActual = totalActual/instances;
        return averageActual;
    }

	
    private double getValuesAverage(ArrayList<Prediction> predictions, double instances) {

        double totalActual = 0.0;
        double averageActual = 0.0;
        for (Prediction p: predictions) {
            totalActual += p.actual();
        }

        averageActual = totalActual/instances;
        return averageActual;
    }

    private void saveResults(String stationname, ArrayList<Prediction> predictions, String summary, RuleNode rules,
                             ResultVO resultVO, String experiment, Evaluation eval) {

        try
        {
            //saving predictions
            BufferedWriter writer = new BufferedWriter(new FileWriter("files\\reports\\"+stationname+"_predictions.csv"));
            writer.write("real;estimado\n");
            for (Prediction p: predictions) {
                System.out.println(p.actual()+";"+p.predicted());
                writer.write(p.actual()+";"+p.predicted()+"\n");
            }
            writer.flush();
            writer.close();
            writer = new BufferedWriter(new FileWriter("files\\reports\\"+stationname+"_summary.txt"));
            writer.write(summary);
            writer.newLine();
            writer.write(rules.printLeafModels());
            writer.flush();
            writer.close();
            System.out.println(rules.printLeafModels());

            MongoCollection collection = mongoUtil.getMongoClient().getDatabase("test").getCollection("results");
            Document doc = new Document();
            doc.append("stationname",stationname);
            doc.append("experiment",experiment);
            doc.append("latitude",resultVO.getLatitude());
            doc.append("longitude",resultVO.getLongitude());
            doc.append("coefficient",resultVO.getCoefficient());
            doc.append("mae",resultVO.getMae());
            doc.append("rmse",resultVO.getRmse());
            doc.append("rae", resultVO.getRae());
            doc.append("rrse", resultVO.getRrse());
            doc.append("maer", resultVO.getMaer());
            doc.append("rmser", resultVO.getRmser());
            doc.append("instances", resultVO.getNumInstances());
            doc.append("model",resultVO.getModel());
            doc.append("algorithm",resultVO.getAlgorithm());
            collection.insertOne(doc);

            mongoUtil.closeClient();

        } catch(IOException e)
        {
            e.printStackTrace();
        }


    }


    public void undo() {
        System.out.println("I need to undo the operations");
    }

}

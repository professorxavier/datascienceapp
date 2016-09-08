package br.uniriotec.ppgi.fernandoxavier;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.M5P;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.experiment.InstanceQuery;

import java.util.ArrayList;
import java.util.Random;

public class WekaTest extends TestCase {

    public WekaTest() {

    }

    public static Test suite()
    {
        return new TestSuite( WekaTest.class );
    }

    public void testWeka()  {
        try {
            ArrayList<Attribute> attrs = new ArrayList<Attribute>();
            Attribute length = new Attribute("length");
            Attribute weight = new Attribute("weight");
            Attribute position = new Attribute("position");
            attrs.add(length);
            attrs.add(weight);
            attrs.add(position);
            Instances race = new Instances("race", attrs, 10);

            for (int i = 0; i < 10; i++) {
                Instance inst = new DenseInstance(3);
                inst.setValue(0, 5.3 + new Random(1).nextDouble());
                inst.setValue(1, 300 + i);
                inst.setValue(2, 40 + new Random(i).nextDouble());
                race.add(inst);
            }
            if (race.classIndex() == -1)
                race.setClassIndex(race.numAttributes() - 1);
            M5P tree = new M5P();         // new instance of tree
            tree.buildClassifier(race);   // build classifier
            Evaluation eval = new Evaluation(race);
            eval.crossValidateModel(tree, race, 2, new Random(1));
            System.out.println(eval.toSummaryString());
            System.out.println(tree.getM5RootNode().printLeafModels());
            assertTrue(true);
        } catch (Exception e) {
            fail("Erro ao rodar " + e.getMessage());
        }

    }

    public void testWekaEvp()  {
        try {
            ArrayList<Attribute> attrs = new ArrayList<Attribute>();
            String[] cols = {"tempmax", "tempmin", "windspeed", "evp"};
            for (String col: cols) {
                Attribute attr = new Attribute(col);
                attrs.add(attr);
            }
            Instances race = new Instances("race", attrs, 10);

            for (int i = 0; i < 10; i++) {
                Instance inst = new DenseInstance(4);
                inst.setValue(0, 27 + new Random(1).nextDouble());
                inst.setValue(1, 23 + new Random(1).nextDouble());
                inst.setValue(2, 2 + new Random(i).nextDouble());
                inst.setValue(3, 100+i);

                race.add(inst);
            }
            if (race.classIndex() == -1)
                race.setClassIndex(race.numAttributes() - 1);
            M5P tree = new M5P();         // new instance of tree
            tree.buildClassifier(race);   // build classifier
            Evaluation eval = new Evaluation(race);
            eval.crossValidateModel(tree, race, 2, new Random(1));
            System.out.println(eval.toSummaryString());
            System.out.println(tree.getM5RootNode().printLeafModels());
            assertTrue(true);
        } catch (Exception e) {
            fail("Erro ao rodar " + e.getMessage());
        }

    }

}

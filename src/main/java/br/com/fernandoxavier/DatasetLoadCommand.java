package br.uniriotec.ppgi.fernandoxavier;


import br.uniriotec.ppgi.fernandoxavier.utils.InmetDataset;
import br.uniriotec.ppgi.fernandoxavier.utils.MongoUtil;

import java.io.File;
import java.util.ArrayList;

public class DatasetLoadCommand implements OperationCommand {

    public static final String DATASET_FOLDER="C:\\Users\\Dell\\Documents\\minhas\\carreira\\academico\\unirio\\pesquisa\\2016\\DataScienceEvapotranspirationApp\\files\\datasets\\originais\\"; //TODO catch from properties
    private ArrayList<StationVO> stations;

    public void execute() {
        System.out.println(getClass().getSimpleName() + " says: I receive the request and I will execute");

        loadDatasetsFiles();
        persistStations(stations);
        System.out.println(String.format("Foram lidos %d datasets ",stations.size()));

    }

    private boolean persistStations(ArrayList<StationVO> stations) {
        try {
            MongoUtil mongoUtil = new MongoUtil();
            mongoUtil.init("test", "stations"); //TODO catch from properties
            mongoUtil.insertStations(stations);
            mongoUtil.closeClient();

            MongoUtil mongoDataset = new MongoUtil();
            mongoDataset.init("test", "datasets"); //TODO catch from properties
            mongoDataset.insertDatasets(stations);
            mongoUtil.closeClient();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void loadDatasetsFiles() {
        stations = new ArrayList<StationVO>();
        File dir = new File(DATASET_FOLDER);
        File[] files = dir.listFiles();
        try {
            for (File file : files) {
                StationVO stationVO = InmetDataset.loadFile(file.getParent(), file.getName());
                stations.add(stationVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void undo() {
        System.out.println("I need to undo the operations");
    }

}

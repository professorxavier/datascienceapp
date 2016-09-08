package br.uniriotec.ppgi.fernandoxavier.utils;


import br.uniriotec.ppgi.fernandoxavier.StationVO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InmetDataset {


    public static final String DATASET_FOLDER="C:\\Users\\Dell\\Documents\\minhas\\carreira\\academico\\unirio\\pesquisa\\2016\\DataScienceEvapotranspirationApp\\files\\datasets\\originais\\";

    public static void main(String[] args) {

        loadFile(DATASET_FOLDER, "ac_cruzeirodosul.csv");
    }

    public static StationVO loadFile(String path, String filename) {


        BufferedReader br = null;
        String line;
        StationVO station = new StationVO();

        try {

            br = new BufferedReader(new FileReader(path+"\\"+filename));

            String[] stationLocation = filename.split("_");
            station.setStationName(filename.replace(".csv",""));
            station.setState(stationLocation[0]);
            station.setCity(stationLocation[1].replace(".csv",""));
            for (int i=1;i<17;i++) {
                line = br.readLine();
//                System.out.println(line);
                if ((i>=5) && (i<=7)) {
                    String[] stationData = line.split(":");
                    if (i==5)
                        station.setLatitude(Double.valueOf(stationData[1].trim()));
                    else if(i==6)
                        station.setLongitude(Double.valueOf(stationData[1].trim()));
                    else if (i==7)
                        station.setAltitude(Double.valueOf(stationData[1].trim()));
                }
                if (i==16)
                    station.setHeader(line);

            }
            System.out.println("Dados: " + station.toString());

            //adding instances to VO
            while ((line = br.readLine()) != null) {

                if (!line.trim().equals(""))
                    station.addInstance(line);

            }
            System.out.println("Numero de Instancias: "+ station.getInstances().size());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return station;
    }

}

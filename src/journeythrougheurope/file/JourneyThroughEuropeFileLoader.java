/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import journeythrougheurope.application.Main.JourneyThroughEuropePropertyType;
import journeythrougheurope.game.JourneyThroughEuropeCity;
import journeythrougheurope.reader.XMLCityReader;
import properties_manager.PropertiesManager;

/**
 *
 * @author Karl
 */
public class JourneyThroughEuropeFileLoader {

    private static final String CITY_DATA_FILE = "data/cities.txt";
    private static final String CITY_NEIGHBORS_FILE = "data/cityneighbors.xml";
    private static final double CONVERSION_FACTOR = .60;
    private HashMap<String, JourneyThroughEuropeCity> cityHashMap;
    private static final double GRID_1_WIDTH = 2000;
    private static final double GRID_1_HEIGHT = 2569;
    private static final double GRID_2_HEIGHT = 2579;
    private static final double GRID_3_WIDTH = 1973;

    public static String loadTextFile(String textFile) throws IOException {
        // ADD THE PATH TO THE FILE
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        textFile = props.getProperty(JourneyThroughEuropePropertyType.DATA_PATH) + textFile;

        // WE'LL ADD ALL THE CONTENTS OF THE TEXT FILE TO THIS STRING
        String textToReturn = "";

        // OPEN A STREAM TO READ THE TEXT FILE
        FileReader fr = new FileReader(textFile);
        BufferedReader reader = new BufferedReader(fr);

        // READ THE FILE, ONE LINE OF TEXT AT A TIME
        String inputLine = reader.readLine();
        while (inputLine != null) {
            // APPEND EACH LINE TO THE STRING
            textToReturn += inputLine + "\n";

            // READ THE NEXT LINE
            inputLine = reader.readLine();
        }

        // RETURN THE TEXT
        return textToReturn;
    }

    public static ArrayList<JourneyThroughEuropeCity> loadMapGridData(int mapGrid, XMLCityReader cities) {
        ArrayList<JourneyThroughEuropeCity> cityData = new ArrayList<>();
        try {
            Scanner fileScan = new Scanner(new File(CITY_DATA_FILE));

            while (fileScan.hasNext()) {

                String cityName = fileScan.next();
                String cardColor = fileScan.next();
                int gridLocation = fileScan.nextInt();
                double gridX = 0;
                double gridY = 0;
                switch (gridLocation) {
                    case 1:
                        gridX = fileScan.nextDouble();
                        gridY = fileScan.nextDouble() * CONVERSION_FACTOR;
                        break;
                    case 2:
                        gridX = fileScan.nextDouble() + GRID_1_WIDTH;
                        gridY = fileScan.nextDouble();
                        break;
                    case 3:
                        gridX = fileScan.nextDouble();
                        gridY = fileScan.nextDouble() + GRID_1_HEIGHT;
                        break;
                    case 4:
                        gridX = fileScan.nextDouble() + GRID_3_WIDTH;
                        gridY = fileScan.nextDouble() + GRID_2_HEIGHT;
                        break;
                }

                if (gridLocation == (mapGrid + 1)) {
                    JourneyThroughEuropeCity cityTemp = cities.getCity(cityName);
                    cityTemp.setCardColor(cardColor);
                    cityTemp.setGridLocation(gridLocation);
                    cityTemp.setGridX(gridX * CONVERSION_FACTOR);
                    cityTemp.setGridY(gridY * CONVERSION_FACTOR);
                    cityTemp.setFront(cityName);
                    cityData.add(cityTemp);

                    System.out.println(cityTemp.toString() + "\n");
                    System.out.println(cities.getCity(cityName).toString() + "\n");
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JourneyThroughEuropeFileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cityData;
    }

}

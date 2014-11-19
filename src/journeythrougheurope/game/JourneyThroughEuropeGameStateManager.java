/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.game;

import journeythrougheurope.thread.Deck;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import journeythrougheurope.file.JourneyThroughEuropeFileLoader;
import journeythrougheurope.reader.XMLCityReader;
import journeythrougheurope.ui.JourneyThroughEuropeUI;

/**
 *
 * @author Karl
 */
public class JourneyThroughEuropeGameStateManager {

    public enum JourneyThroughEuropeGameState {

        GAME_NOT_STARTED, GAME_IN_PROGRESS, GAME_OVER,
    }

    private final String XML_CITY_FILE_PATH = "data/cityneighbors.xml";
    private JourneyThroughEuropeUI ui;
    private JourneyThroughEuropeGameState currentGameState;
    private ArrayList<JourneyThroughEuropeGameData> gamesHistory;
    private JourneyThroughEuropeGameData gameInProgress;
    private Deck deck;

    private final int MAX_GRIDS = 4;
    private final int DEFAULT_GRID = 0;

    private XMLCityReader cities;
    private HashMap<String, JourneyThroughEuropeCity> cityHashMap;
    private ArrayList<JourneyThroughEuropeCity> currentGrid;
    private ArrayList<ArrayList<JourneyThroughEuropeCity>> grids;

    // private
    public JourneyThroughEuropeGameStateManager(JourneyThroughEuropeUI ui) {
        this.ui = ui;
        buildHashMap();
        initGrids();
        initDeck();
        currentGrid = grids.get(DEFAULT_GRID);
        currentGameState = JourneyThroughEuropeGameState.GAME_NOT_STARTED;
        gamesHistory = new ArrayList<JourneyThroughEuropeGameData>();
        gameInProgress = null;
    }

    private void buildHashMap() {
        cities = new XMLCityReader(XML_CITY_FILE_PATH);
        cities.buildCityHashMap();
        cityHashMap = cities.getCityHashMap();
    }

    private void initDeck() {
        deck = new Deck();
        ArrayList<String> redCities = new ArrayList<String>();
        ArrayList<String> greenCities = new ArrayList<String>();
        ArrayList<String> yellowCities = new ArrayList<String>();

        for (int i = 0; i < MAX_GRIDS; i++) {
            for (int j = 0; j < grids.get(i).size(); j++) {
                switch (grids.get(i).get(j).getCardColor().toUpperCase().trim()) {
                    case "RED":
                        redCities.add(grids.get(i).get(j).getCityName());
                        break;
                    case "GREEN":
                        greenCities.add(grids.get(i).get(j).getCityName());
                        break;
                    case "YELLOW":
                        yellowCities.add(grids.get(i).get(j).getCityName());
                        break;
                }
            }
        }

        deck.setRedDeck(redCities);
        deck.setGreenDeck(greenCities);
        deck.setYellowDeck(yellowCities);
    }

    private void initGrids() {
        grids = new ArrayList<ArrayList<JourneyThroughEuropeCity>>();
        for (int i = 0; i < MAX_GRIDS; i++) {
            grids.add(JourneyThroughEuropeFileLoader.loadMapGridData(i, cities));
        }
    }

    public JourneyThroughEuropeGameData getGameInProgess() {
        return gameInProgress;
    }

    public int getGamesPlayed() {
        return gamesHistory.size();
    }

    public Iterator<JourneyThroughEuropeGameData> getGamesHistoryIterator() {
        return gamesHistory.iterator();
    }

    public boolean isGameNotStarted() {
        return currentGameState == JourneyThroughEuropeGameState.GAME_NOT_STARTED;
    }

    public boolean isGameOver() {
        return currentGameState == JourneyThroughEuropeGameState.GAME_OVER;
    }

    public boolean isGameInProgress() {
        return currentGameState == JourneyThroughEuropeGameState.GAME_IN_PROGRESS;
    }

    public void processGridChangeRequest(int gridState) {

        switch (gridState) {
            case 1:
                currentGrid = grids.get(0);
                break;
            case 2:
                currentGrid = grids.get(1);
                break;
            case 3:
                currentGrid = grids.get(2);
                break;
            case 4:
                currentGrid = grids.get(3);
                break;
        }
    }

    public ArrayList<JourneyThroughEuropeCity> getCurrentGridData() {
        return currentGrid;
    }

    public void startNewGame() {
        if (!isGameNotStarted() && (!gamesHistory.contains(gameInProgress))) {
            gamesHistory.add(gameInProgress);
        }

        if (isGameInProgress() && !gameInProgress.isWon()) {
            // QUIT THE GAME, WHICH SETS THE END TIME
            gameInProgress.giveUp();
            //ui.getDocManager().updateStatsDoc();
        }

        // AND NOW MAKE A NEW GAME
        makeNewGame();

        // AND MAKE SURE THE UI REFLECTS A NEW GAME
        //ui.resetUI();
    }

    public void makeNewGame() {

        //ui.getDocManager().updateStatsDoc();
        gameInProgress = new JourneyThroughEuropeGameData(ui);
        gameInProgress.startGame();

        // THE GAME IS OFFICIALLY UNDERWAY
        currentGameState = JourneyThroughEuropeGameState.GAME_IN_PROGRESS;
    }

    public Deck getDeck() {
        return deck;
    }
    
    public JourneyThroughEuropeCity processGetCityRequest(String cityName)
    {
        JourneyThroughEuropeCity temp = cityHashMap.get(cityName.toUpperCase().trim());
        if (temp == null) {
            System.out.println("The requested city is not contained in this hashmap.\t" + cityName);
            throw new NullPointerException("The requested city is not contained in this hashmap.\t" + cityName);
        }
        return temp;
    }
    
    public void processStartTurnRequest()
    {
        if(isGameInProgress())
        {
            gameInProgress.startTurn();
        }
    }
    
    public void processIncrementPlayerRequest()
    {
        if(isGameInProgress())
        {
            gameInProgress.incrementPlayer();
        }
    }
    
    public void processDieRollRequest(int die)
    {
        if(isGameInProgress())
        {
            gameInProgress.updateDieRolledRequest(die);
        }
    }
}

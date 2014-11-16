/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.game;

import journeythrougheurope.thread.Deck;
import java.util.ArrayList;
import java.util.Iterator;
import journeythrougheurope.file.JourneyThroughEuropeFileLoader;
import journeythrougheurope.ui.JourneyThroughEuropeUI;

/**
 *
 * @author Karl
 */
public class JourneyThroughEuropeGameStateManager {

    public enum JourneyThroughEuropeGameState {

        GAME_NOT_STARTED, GAME_IN_PROGRESS, GAME_OVER,
    }

    private JourneyThroughEuropeUI ui;
    private JourneyThroughEuropeGameState currentGameState;
    private ArrayList<JourneyThroughEuropeGameData> gamesHistory;
    private JourneyThroughEuropeGameData gameInProgress;
    private Deck deck;

    private final int MAX_GRIDS = 4;
    private final int DEFAULT_GRID = 0;
    private ArrayList<JourneyThroughEuropeCity> currentGrid;
    private ArrayList<ArrayList<JourneyThroughEuropeCity>> grids;

    public JourneyThroughEuropeGameStateManager(JourneyThroughEuropeUI ui) {
        this.ui = ui;
        initGrids();
        initDeck();
        currentGrid = grids.get(DEFAULT_GRID);
        currentGameState = JourneyThroughEuropeGameState.GAME_NOT_STARTED;
        gamesHistory = new ArrayList<JourneyThroughEuropeGameData>();
        gameInProgress = null;

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
            grids.add(JourneyThroughEuropeFileLoader.loadMapGridData(i));

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

    public void processGridChangeRequest(JourneyThroughEuropeUI.JourneyThroughEuropeUIState gridState) {

        switch (gridState) {
            case GRID1_IMAGE_STATE:
                currentGrid = grids.get(0);
                break;
            case GRID2_IMAGE_STATE:
                currentGrid = grids.get(1);
                break;
            case GRID3_IMAGE_STATE:
                currentGrid = grids.get(2);
                break;
            case GRID4_IMAGE_STATE:
                currentGrid = grids.get(3);
                break;
        }
    }

    public ArrayList<JourneyThroughEuropeCity> getCurrentGridData() {
        return currentGrid;
    }

    public void makeNewGame() {

        //gamesHistory =  
        //ui.getDocManager().updateStatsDoc();
        // gameInProgress = new SokobanGameData(ui, level);
        //gameInProgress.startGame();
        // THE GAME IS OFFICIALLY UNDERWAY
        currentGameState = JourneyThroughEuropeGameState.GAME_IN_PROGRESS;
    }

}

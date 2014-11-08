/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.game;

import java.util.ArrayList;
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

    private final int MAX_GRIDS = 4;
    private final int DEFAULT_GRID = 0;
    private ArrayList<JourneyThroughEuropeCity> currentGrid;
    private ArrayList<ArrayList<JourneyThroughEuropeCity>> grids;

    public JourneyThroughEuropeGameStateManager(JourneyThroughEuropeUI ui) {
        this.ui = ui;
        initGrids();
        currentGrid = grids.get(DEFAULT_GRID);
    }

    private void initGrids() {
        grids = new ArrayList<ArrayList<JourneyThroughEuropeCity>>();
        for (int i = 1; i <= MAX_GRIDS; i++) {
            grids.add(JourneyThroughEuropeFileLoader.loadMapGridData(i));
            
        }
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
    
    public ArrayList<JourneyThroughEuropeCity> getCurrentGridData()
    {
        return currentGrid;
    }
}

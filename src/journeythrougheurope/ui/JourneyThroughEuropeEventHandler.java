/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.ui;

import javafx.stage.Stage;

/**
 *
 * @author Karl
 */
public class JourneyThroughEuropeEventHandler {

    private JourneyThroughEuropeUI ui;

    public JourneyThroughEuropeEventHandler(JourneyThroughEuropeUI ui) {
        this.ui = ui;
    }

    public void respondToSwitchScreenRequest(JourneyThroughEuropeUI.JourneyThroughEuropeUIState uiState) {
       ui.changeWorkspace(uiState);
    }

    public void respondToStartGameRequest() {

    }

    public void respondToLoadGameRequest() {

    }

    public void respondToExitGameRequest() {

    }

    public void respondToRollRequest() {

    }

    public void respondToStartRequest() {
      respondToSwitchScreenRequest(JourneyThroughEuropeUI.JourneyThroughEuropeUIState.GAME_SETUP_STATE);
    }

    public void respondToChangeMapGridRequest() {

    }

    public void respondToShowWinDialogRequest(Stage primaryStage) {

    }
    
    public void respondToChangeNumberOfPlayersRequest(int numPlayers)
    {
        ui.disablePlayerGridPanes();
        ui.enablePlayerGridPanes(numPlayers);
    }
}

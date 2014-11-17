/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.ui;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import journeythrougheurope.application.Main.JourneyThroughEuropePropertyType;
import journeythrougheurope.thread.CardThread;
import journeythrougheurope.thread.GameRenderer;
import properties_manager.PropertiesManager;

/**
 *
 * @author Karl
 */
public class JourneyThroughEuropeEventHandler {

    private JourneyThroughEuropeUI ui;
    private CardThread test;

    public JourneyThroughEuropeEventHandler(JourneyThroughEuropeUI ui) {
        this.ui = ui;
    }

    public void respondToSwitchScreenRequest(JourneyThroughEuropeUI.JourneyThroughEuropeUIState uiState) {
        ui.changeWorkspace(uiState);
    }

    public void respondToStartGameRequest(JourneyThroughEuropeUI.JourneyThroughEuropeUIState uiState) {
        ui.changeWorkspace(uiState);

        ArrayList<PlayerManager> players = ui.getPlayers();
        int maxPlayers = players.size();
        for (int i = 0; i < maxPlayers; i++) {
            if (i < ui.getNumPlayers()) {
                if (players.get(i).getPlayerName().equals("")) {
                    players.get(i).setPlayerName("Player " + (i + 1));
                }
            } else {
                players.remove(players.size() - 1);
            }

        }
        ui.setCurrentPlayer(0);

        CardThread test = new CardThread(ui);
        ui.testClick(test.getCardRenderer());
        test.startCardThread();
    }

    public void respondToLoadGameRequest() {

    }

    public void respondToGameHistoryRequest(JourneyThroughEuropeUI.JourneyThroughEuropeUIState uiState) {
        ui.changeWorkspace(uiState);
    }

    public void respondToBackRequest(JourneyThroughEuropeUI.JourneyThroughEuropeUIState uiState) {
        ui.changeWorkspace(uiState);
    }

    public void respondToExitGameRequest(Stage primaryStage) {
        String options[] = new String[]{
            "Yes", "No"
        };
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        options[0] = props.getProperty(JourneyThroughEuropePropertyType.DEFAULT_YES_TEXT);
        options[1] = props.getProperty(JourneyThroughEuropePropertyType.DEFAULT_NO_TEXT);
        String verifyReset = props.getProperty(JourneyThroughEuropePropertyType.DEFAULT_EXIT_TEXT);

        // NOW WE'LL CHECK TO SEE IF LANGUAGE SPECIFIC VALUES HAVE BEEN SET
        if (props.getProperty(JourneyThroughEuropePropertyType.YES_TEXT) != null) {
            options[0] = props.getProperty(JourneyThroughEuropePropertyType.YES_TEXT);
            options[1] = props.getProperty(JourneyThroughEuropePropertyType.NO_TEXT);
            verifyReset = props.getProperty(JourneyThroughEuropePropertyType.EXIT_REQUEST_TEXT);
        }

        // FIRST MAKE SURE THE USER REALLY WANTS TO EXIT
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Exit");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        BorderPane resetPane = new BorderPane();

        HBox optionPane = new HBox();
        Button btnYes = new Button(options[0]);
        Button btnNo = new Button(options[1]);

        optionPane.setPadding(new Insets(2, 2, 10, 2));
        optionPane.setSpacing(5.0);
        optionPane.getChildren().addAll(btnYes, btnNo);
        optionPane.setAlignment(Pos.TOP_CENTER);

        Label resetLabel = new Label("Are you sure you want to exit?");
        VBox resetLabelPane = new VBox();
        resetLabelPane.setPadding(new Insets(10, 2, 5, 2));
        resetLabelPane.getChildren().addAll(resetLabel);
        resetLabelPane.setAlignment(Pos.BOTTOM_CENTER);

        VBox container = new VBox();
        container.getChildren().addAll(resetLabelPane, optionPane);
        container.setAlignment(Pos.CENTER);

        resetPane.setCenter(container);

        Scene scene = new Scene(resetPane);

        dialogStage.setWidth(280);
        dialogStage.setHeight(110);
        dialogStage.setResizable(false);
        dialogStage.setScene(scene);
        dialogStage.show();

        // WHAT'S THE USER'S DECISION?
        btnYes.setOnAction(buttonEvent -> {
            System.exit(0);
        });

        btnNo.setOnAction(buttonEvent -> {
            // Closes the stage when the user's decision is "No".
            dialogStage.close();
        });

    }

    public void respondToRollRequest() {
        int roll = (int) ((Math.random() * 6) + 1);
        System.out.println(roll);
        switch (roll) {
            case 1:
                ui.changeDieImage(JourneyThroughEuropeUI.JourneyThroughEuropeUIState.DIE1_IMAGE_STATE);
                break;
            case 2:
                ui.changeDieImage(JourneyThroughEuropeUI.JourneyThroughEuropeUIState.DIE2_IMAGE_STATE);
                break;
            case 3:
                ui.changeDieImage(JourneyThroughEuropeUI.JourneyThroughEuropeUIState.DIE3_IMAGE_STATE);
                break;
            case 4:
                ui.changeDieImage(JourneyThroughEuropeUI.JourneyThroughEuropeUIState.DIE4_IMAGE_STATE);
                break;
            case 5:
                ui.changeDieImage(JourneyThroughEuropeUI.JourneyThroughEuropeUIState.DIE5_IMAGE_STATE);
                break;
            case 6:
                ui.changeDieImage(JourneyThroughEuropeUI.JourneyThroughEuropeUIState.DIE6_IMAGE_STATE);
                break;
        }
    }

    public void respondToGameSetupRequest() {
        respondToSwitchScreenRequest(JourneyThroughEuropeUI.JourneyThroughEuropeUIState.GAME_SETUP_STATE);
    }

    public void respondToChangeGridRequest(JourneyThroughEuropeUI.JourneyThroughEuropeUIState gridState) {
        ui.getGSM().processGridChangeRequest(gridState);
        ui.changeGridImage(gridState);
        ui.getGameRenderer().updateCityData();
    }

    public void respondToShowWinDialogRequest(Stage primaryStage) {

    }

    public void respondToChangeNumberOfPlayersRequest(int numPlayers) {
        ui.disablePlayerGridPanes();
        ui.enablePlayerGridPanes(numPlayers);
    }
}

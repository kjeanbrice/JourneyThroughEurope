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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import journeythrougheurope.application.Main.JourneyThroughEuropePropertyType;
import journeythrougheurope.file.JourneyThroughEuropeFileLoader;
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

    public void respondToStartGameRequest(JourneyThroughEuropeUI.JourneyThroughEuropeUIState uiState, int maxCards) {
        ui.changeWorkspace(uiState);

        ArrayList<PlayerManager> players = ui.getPlayers();
        int maxPlayers = players.size();
        for (int i = 0; i < maxPlayers; i++) {
            if (i < ui.getNumPlayers()) {
                if (players.get(i).getPlayerName().equals("")) {
                    players.get(i).setPlayerName("Player " + (i + 1));
                }
                players.get(i).setHomeImage(ui.initFlagImage(i));
                players.get(i).setPlayerImage(ui.initPlayerImage(i));
            } else {
                players.remove(players.size() - 1);
            }

        }
        ui.setCurrentPlayer(0);
        ui.getGSM().startNewGame(maxCards);
    }

    public void respondToLoadGameRequest() {
        ArrayList<PlayerManager> players = JourneyThroughEuropeFileLoader.loadFile(ui);
        ui.setPlayers(players);
        ui.setNumPlayers(players.size());
        ui.setNumCards(JourneyThroughEuropeFileLoader.NUM_CARDS);
        JourneyThroughEuropeFileLoader.NUM_CARDS = 0;

        respondToStartGameRequest(JourneyThroughEuropeUI.JourneyThroughEuropeUIState.PLAY_GAME_STATE,ui.getNumCards());
        if (ui.getGSM().isGameInProgress()) {
            ui.getGSM().getGameInProgess().setCurrentPlayer(JourneyThroughEuropeFileLoader.CURRENT_PLAYER);
            JourneyThroughEuropeFileLoader.CURRENT_PLAYER = 0;
        }

        for (int i = 0; i < players.size(); i++) {
            ui.getDocumentManager().addGameResultToStatsPage(players);
        }

    }

    public void respondToSaveGameRequest(ArrayList<PlayerManager> players, Stage primaryStage) {
        if (ui.getGSM().isGameInProgress()) {
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Save Successful");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            BorderPane winPane = new BorderPane();

            Button btnOK = new Button("OK");
            HBox optionPane = new HBox();

            optionPane.setPadding(new Insets(2, 2, 10, 2));
            optionPane.setSpacing(5.0);
            optionPane.getChildren().add(btnOK);
            optionPane.setAlignment(Pos.TOP_CENTER);

            Label saveLabel = new Label("");
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String saveImageName = props.getProperty(JourneyThroughEuropePropertyType.SAVE_IMG_NAME);
            Image saveImage = new Image("file:images/" + saveImageName);
            ImageView trophyView = new ImageView(saveImage);
            saveLabel.setGraphic(trophyView);

            Label winningLabel = new Label("Your game has saved succesfully.");
            winningLabel.setStyle("-fx-font-size: 13px;"
                    + " -fx-font-family: \"Trebuchet MS\";"
                    + " -fx-font-weight: bold;");

            HBox winLabelPane = new HBox();
            winLabelPane.setSpacing(5);
            winLabelPane.setPadding(new Insets(10, 2, 5, 2));
            winLabelPane.getChildren().addAll(saveLabel, winningLabel);
            winLabelPane.setAlignment(Pos.BOTTOM_CENTER);

            VBox container = new VBox();
            container.getChildren().addAll(winLabelPane, optionPane);
            container.setAlignment(Pos.CENTER);

            winPane.setCenter(container);

            Scene scene = new Scene(winPane);

            dialogStage.setWidth(280);
            dialogStage.setHeight(110);
            dialogStage.setResizable(false);
            dialogStage.setScene(scene);
            dialogStage.show();

            btnOK.setOnAction(buttonEvent -> {

                dialogStage.close();
            });

            JourneyThroughEuropeFileLoader.saveFile(ui,players, ui.getGSM().getGameInProgess().getCurrentPlayer());
        }
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

        btnYes.setStyle("-fx-font-size: 13px;"
                + " -fx-font-family: \"Trebuchet MS\";"
                + " -fx-font-weight: bold;");

        btnNo.setStyle("-fx-font-size: 13px;"
                + " -fx-font-family: \"Trebuchet MS\";"
                + " -fx-font-weight: bold;");

        optionPane.setPadding(new Insets(2, 2, 10, 2));
        optionPane.setSpacing(5.0);
        optionPane.getChildren().addAll(btnYes, btnNo);
        optionPane.setAlignment(Pos.TOP_CENTER);

        Label exitLabel = new Label("Are you sure you want to exit?");
        exitLabel.setStyle("-fx-font-size: 13px;"
                + " -fx-font-family: \"Trebuchet MS\";"
                + " -fx-font-weight: bold;");

        Label exitImageLabel = new Label("");
        props = PropertiesManager.getPropertiesManager();
        String exitImageName = props.getProperty(JourneyThroughEuropePropertyType.EXIT_ICON_IMG_NAME);
        Image saveImage = new Image("file:images/" + exitImageName);
        ImageView exitView = new ImageView(saveImage);
        exitLabel.setGraphic(exitView);

        HBox resetLabelPane = new HBox();
        resetLabelPane.setPadding(new Insets(10, 2, 5, 2));
        resetLabelPane.getChildren().addAll(exitImageLabel, exitLabel);
        resetLabelPane.setAlignment(Pos.BOTTOM_CENTER);

        VBox container = new VBox();
        container.getChildren().addAll(resetLabelPane, optionPane);
        container.setAlignment(Pos.CENTER);

        resetPane.setCenter(container);

        Scene scene = new Scene(resetPane);

        dialogStage.setWidth(320);
        dialogStage.setHeight(120);
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
        switch (roll) {
            case 1:
                ui.setDieImage(1);
                if (ui.getGSM().isGameInProgress()) {
                    ui.getGSM().processRollRequest(1);
                }

                break;
            case 2:
                ui.setDieImage(2);
                if (ui.getGSM().isGameInProgress()) {
                    ui.getGSM().processRollRequest(2);
                }
                break;
            case 3:
                ui.setDieImage(3);
                if (ui.getGSM().isGameInProgress()) {
                    ui.getGSM().processRollRequest(3);
                }
                break;
            case 4:
                ui.setDieImage(4);
                if (ui.getGSM().isGameInProgress()) {
                    ui.getGSM().processRollRequest(4);
                }
                break;
            case 5:
                ui.setDieImage(5);
                if (ui.getGSM().isGameInProgress()) {
                    ui.getGSM().processRollRequest(5);
                }
                break;
            case 6:
                ui.setDieImage(6);
                if (ui.getGSM().isGameInProgress()) {
                    ui.getGSM().processRollRequest(6);
                }
                break;
        }
    }

    public void respondToGameSetupRequest() {
        respondToSwitchScreenRequest(JourneyThroughEuropeUI.JourneyThroughEuropeUIState.VIEW_GAME_SETUP_STATE);
    }

    public void respondToChangeGridRequest(int gridState) {
        ui.getGSM().processGridChangeRequest(gridState);
        ui.changeGridImage(gridState);
    }

    public void respondToChangeNumberOfPlayersRequest(int numPlayers) {
        ui.disablePlayerGridPanes();
        ui.enablePlayerGridPanes(numPlayers);
    }

    public void respondToExitRequest(Stage primaryStage) {
        // ENGLIS IS THE DEFAULT
        String options[] = new String[]{
            "Yes", "No"
        };
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        options[0] = props.getProperty(JourneyThroughEuropePropertyType.DEFAULT_YES_TEXT);
        options[1] = props.getProperty(JourneyThroughEuropePropertyType.DEFAULT_NO_TEXT);
        String verifyExit = props.getProperty(JourneyThroughEuropePropertyType.DEFAULT_EXIT_TEXT);
        String exitText = props.getProperty(JourneyThroughEuropePropertyType.EXIT_TEXT);

        // NOW WE'LL CHECK TO SEE IF LANGUAGE SPECIFIC VALUES HAVE BEEN SET
        if (props.getProperty(JourneyThroughEuropePropertyType.YES_TEXT) != null) {
            options[0] = props.getProperty(JourneyThroughEuropePropertyType.YES_TEXT);
            options[1] = props.getProperty(JourneyThroughEuropePropertyType.NO_TEXT);
            verifyExit = props.getProperty(JourneyThroughEuropePropertyType.EXIT_REQUEST_TEXT);
            exitText = props.getProperty(JourneyThroughEuropePropertyType.EXIT_TEXT);
        }

        // FIRST MAKE SURE THE USER REALLY WANTS TO EXIT
        Stage dialogStage = new Stage();
        dialogStage.setTitle(exitText);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        BorderPane exitPane = new BorderPane();

        HBox optionPane = new HBox();
        Button btnYes = new Button(options[0]);
        Button btnNo = new Button(options[1]);

        btnYes.setStyle("-fx-font-size: 13px;"
                + " -fx-font-family: \"Trebuchet MS\";"
                + " -fx-font-weight: bold;");

        btnNo.setStyle("-fx-font-size: 13px;"
                + " -fx-font-family: \"Trebuchet MS\";"
                + " -fx-font-weight: bold;");

        optionPane.setPadding(new Insets(2, 2, 10, 2));
        optionPane.setSpacing(5.0);
        optionPane.getChildren().addAll(btnYes, btnNo);
        optionPane.setAlignment(Pos.TOP_CENTER);

        Label exitLabel = new Label(verifyExit);
        exitLabel.setStyle("-fx-font-size: 13px;"
                + " -fx-font-family: \"Trebuchet MS\";"
                + " -fx-font-weight: bold;");

        Label exitImageLabel = new Label("");
        props = PropertiesManager.getPropertiesManager();
        String exitImageName = props.getProperty(JourneyThroughEuropePropertyType.EXIT_ICON_IMG_NAME);
        Image saveImage = new Image("file:images/" + exitImageName);
        ImageView exitView = new ImageView(saveImage);
        exitLabel.setGraphic(exitView);

        HBox exitLabelPane = new HBox();
        exitLabelPane.setPadding(new Insets(10, 2, 2, 2));
        exitLabelPane.getChildren().addAll(exitImageLabel, exitLabel);
        exitLabelPane.setAlignment(Pos.BOTTOM_CENTER);

        VBox container = new VBox();
        container.getChildren().addAll(exitLabelPane, optionPane);
        container.setAlignment(Pos.CENTER);

        exitPane.setCenter(container);

        Scene scene = new Scene(exitPane);

        dialogStage.setWidth(500);
        dialogStage.setHeight(110);
        dialogStage.setResizable(false);
        dialogStage.setScene(scene);
        dialogStage.show();

        // WHAT'S THE USER'S DECISION?
        btnYes.setOnAction(buttonEvent -> {
            // YES, LET'S EXIT
            System.exit(0);
        });

        btnNo.setOnAction(buttonEvent -> {
            // Closes the stage when the user's decision is "No".
            dialogStage.close();
        });

    }

    public void showWinDialog(Stage primaryStage, String playerName) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Congratulations!");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        BorderPane winPane = new BorderPane();

        Button btnOK = new Button("OK");
        btnOK.setStyle("-fx-font-size: 13px;"
                    + " -fx-font-family: \"Trebuchet MS\";"
                    + " -fx-font-weight: bold;");
        
        HBox optionPane = new HBox();
        optionPane.getChildren().add(btnOK);
        optionPane.setAlignment(Pos.TOP_CENTER);

        Label trophyLabel = new Label("");
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String trophyImageName = props.getProperty(JourneyThroughEuropePropertyType.TROPHY_IMG_NAME);
        Image trophyImage = new Image("file:images/" + trophyImageName);
        ImageView trophyView = new ImageView(trophyImage);
        trophyLabel.setGraphic(trophyView);

        Label winningLabel = new Label("Congratulations " + playerName + "! You Won!");
        winningLabel.setStyle("-fx-font-size: 13px;"
                    + " -fx-font-family: \"Trebuchet MS\";"
                    + " -fx-font-weight: bold;");

        HBox winLabelPane = new HBox();
        winLabelPane.setSpacing(20);
        winLabelPane.setPadding(new Insets(10, 20, 5, 5));
        winLabelPane.getChildren().addAll(trophyLabel, winningLabel);
        winLabelPane.setAlignment(Pos.CENTER);

        VBox container = new VBox();
        container.getChildren().addAll(winLabelPane, optionPane);
        container.setAlignment(Pos.CENTER);

        winPane.setCenter(container);

        Scene scene = new Scene(winPane);

        dialogStage.setWidth(340);
        dialogStage.setHeight(140);
        dialogStage.setResizable(false);
        dialogStage.setScene(scene);
        dialogStage.show();

        btnOK.setOnAction(buttonEvent -> {

            dialogStage.close();
            ui.changeWorkspace(JourneyThroughEuropeUI.JourneyThroughEuropeUIState.SPLASH_SCREEN_STATE);
            ui.resetGame();
            ui.resetRollImage();
            ui.updateMovesRemaining("Moves Remaining: 0");
        });
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.ui;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import journeythrougheurope.application.Main;
import journeythrougheurope.application.Main.JourneyThroughEuropePropertyType;
import journeythrougheurope.game.JourneyThroughEuropeGameStateManager;
import properties_manager.PropertiesManager;

/**
 *
 * @author Karl
 */
public class JourneyThroughEuropeUI extends Pane {

    private Stage primaryStage;
    private BorderPane mainPane;
    private StackPane workspace;

    private int paneWidth;
    private int paneHeight;
    private Insets marginlessInsets;
    private String ImgPath = "file:images/";

    private JourneyThroughEuropeDocumentManager docManager;
    private JourneyThroughEuropeErrorHandler errorHandler;
    private JourneyThroughEuropeEventHandler eventHandler;
    private JourneyThroughEuropeGameStateManager gsm;

    //Splash Screen
    private ImageView splashScreenImageView;
    private StackPane splashScreenPane;
    private Label splashScreenImageLabel;

    private Button btnStart;
    private Button btnLoad;
    private Button btnAbout;
    private Button btnExit;

    //Game Setup Screen
    private ComboBox playerSelectionComboBox;
    private HBox northPanel;
    private VBox centerPanel;
    private Button btnGo;

    //Containers
    private BorderPane splashScreenContainer;
    private BorderPane gameSetupScreenContainer;

    public enum JourneyThroughEuropeUIState {

        SPLASH_SCREEN_STATE, PLAY_GAME_STATE, VIEW_STATS_STATE, VIEW_ABOUT_STATE, GAME_SETUP_STATE
    }

    public JourneyThroughEuropeUI() {
        docManager = new JourneyThroughEuropeDocumentManager(this);
        errorHandler = new JourneyThroughEuropeErrorHandler(primaryStage);
        eventHandler = new JourneyThroughEuropeEventHandler(this);
        gsm = new JourneyThroughEuropeGameStateManager(this);
    }

    public BorderPane getMainPane() {
        return this.mainPane;
    }

    public void setStage(Stage stage) {
        primaryStage = stage;
    }

    public JourneyThroughEuropeDocumentManager getDocumentManager() {
        return docManager;
    }

    public JourneyThroughEuropeErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public JourneyThroughEuropeEventHandler getEventHandler() {
        return eventHandler;
    }

    public JourneyThroughEuropeGameStateManager getGSM() {
        return gsm;
    }

    public void initJourneyThroughEuropeGame() {
        initContainers();
        initWorkspace();
        initMainPane();
        initSplashScreen();
        initGameSetupScreen();
        initAboutScreen();
        initStatsScreen();
        initGameScreen();
        changeWorkspace(JourneyThroughEuropeUIState.GAME_SETUP_STATE);
    }

    private void initMainPane() {
        marginlessInsets = new Insets(5, 5, 5, 5);
        mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color:brown");
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        paneWidth = Integer.parseInt(props
                .getProperty(JourneyThroughEuropePropertyType.WINDOW_WIDTH));
        paneHeight = Integer.parseInt(props
                .getProperty(JourneyThroughEuropePropertyType.WINDOW_HEIGHT));
        mainPane.resize(paneWidth, paneHeight);
        mainPane.setPadding(marginlessInsets);
    }

    private void initWorkspace() {
        workspace = new StackPane();
        workspace.setAlignment(Pos.CENTER);
    }

    public Image loadImage(String imageName) {
        Image img = new Image(ImgPath + imageName);
        return img;
    }

    public void initContainers() {
        splashScreenContainer = new BorderPane();

        gameSetupScreenContainer = new BorderPane();
        gameSetupScreenContainer.setStyle("-fx-background-color:sandybrown");

        /*
         gameScreenContainer = new BorderPane();
         gameScreenContainer.setStyle("-fx-background-color:white");

         statsScreenContainer = new BorderPane();
         statsScreenContainer.setStyle("-fx-background-color:white");

         helpScreenContainer = new BorderPane();
         helpScreenContainer.setStyle("-fx-background-color:white");
         */
    }

    public void initSplashScreen() {

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String splashScreenImagePath = props.getProperty(JourneyThroughEuropePropertyType.SPLASH_SCREEN_IMAGE_NAME);

        splashScreenPane = new StackPane();

        Image splashScreenImage = loadImage(splashScreenImagePath);
        splashScreenImageView = new ImageView(splashScreenImage);

        splashScreenImageLabel = new Label();
        splashScreenImageLabel.setGraphic(splashScreenImageView);

        btnStart = new Button("Start");
        btnLoad = new Button("Load");
        btnAbout = new Button("About");
        btnExit = new Button("Exit");

        btnStart.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToStartRequest();
            }
        });

        btnLoad.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToLoadGameRequest();
            }
        });

        btnAbout.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToSwitchScreenRequest();
            }
        });

        btnExit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToExitGameRequest();
            }
        });

        VBox container = new VBox();

        container.getChildren().add(btnStart);
        container.getChildren().add(btnLoad);
        container.getChildren().add(btnAbout);
        container.getChildren().add(btnExit);
        container.setAlignment(Pos.BOTTOM_CENTER);
        container.setSpacing(3.0);
        container.setPadding(marginlessInsets);

        splashScreenPane.getChildren().add(splashScreenImageLabel);
        splashScreenPane.getChildren().add(container);
        splashScreenPane.setAlignment(Pos.CENTER);
        splashScreenContainer.setCenter(splashScreenPane);
        workspace.getChildren().add(splashScreenContainer);
    }

    private void initAboutScreen() {

    }

    private void initGameSetupScreen() {

        northPanel = new HBox();
        playerSelectionComboBox = new ComboBox();
        playerSelectionComboBox.getItems().addAll("1", "2", "3", "4", "5", "6");
        playerSelectionComboBox.setValue("1");

        Label lblPlayerSelection = new Label("Number of Players :");

        btnGo = new Button("GO!");
        btnGo.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToStartGameRequest();
            }
        });

        northPanel.setSpacing(10);
        northPanel.setPadding(new Insets(20, 20, 20, 20));
        northPanel.setAlignment(Pos.CENTER_LEFT);
        northPanel.getChildren().add(lblPlayerSelection);
        northPanel.getChildren().add(playerSelectionComboBox);
        northPanel.getChildren().add(btnGo);

        gameSetupScreenContainer.setTop(northPanel);
        workspace.getChildren().add(gameSetupScreenContainer);
    }

    private void initGameScreen() {

    }

    private void initStatsScreen() {

    }

    public void changeWorkspace(JourneyThroughEuropeUIState uiScreen) {
        disableAllPanes();

        switch (uiScreen) {
            case SPLASH_SCREEN_STATE:
                splashScreenContainer.setVisible(true);
                break;
            case PLAY_GAME_STATE:
                //gameScreenContainer.setVisible(true);
                break;
            case VIEW_STATS_STATE:
                //statsScreenContainer.setVisible(true);
                break;
            case VIEW_ABOUT_STATE:
                //helpScreenContainer.setVisible(true);
                break;
            case GAME_SETUP_STATE:
                gameSetupScreenContainer.setVisible(true);
                break;
            default:
        }
        mainPane.setCenter(workspace);
    }

    public void disableAllPanes() {
        splashScreenContainer.setVisible(false);
        gameSetupScreenContainer.setVisible(false);
        //gameScreenContainer.setVisible(false);
        //statsScreenContainer.setVisible(false);
        //helpScreenContainer.setVisible(false);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.ui;

import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import journeythrougheurope.application.Main.JourneyThroughEuropePropertyType;
import journeythrougheurope.file.JourneyThroughEuropeFileLoader;
import journeythrougheurope.thread.GameRenderer;
import journeythrougheurope.game.JourneyThroughEuropeGameStateManager;
import journeythrougheurope.thread.CardRenderer;
import properties_manager.PropertiesManager;

/**
 *
 * @author Karl
 */
public class JourneyThroughEuropeUI extends Pane {

    public final int GRID1_IMAGE_STATE = 1;
    public final int GRID2_IMAGE_STATE = 2;
    public final int GRID3_IMAGE_STATE = 3;
    public final int GRID4_IMAGE_STATE = 4;

    private GameRenderer gameRenderer;
    private CardRenderer cardRenderer;
    private GameMouseHandler mouseHandler;

    private final int MAX_PLAYERS = 6;
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
    private FlowPane centerPanel;
    private VBox playerGridPanes[];
    private StackPane playerGridContainers[];
    private int numPlayers;
    private Button btnGo;
    private ArrayList<PlayerManager> playersManager;

    //Game Screen
    private VBox leftPanel;
    private StackPane gamePanel;
    private Pane gameCanvas;
    private Pane cardCanvas;
    private ScrollPane gameGridScrollPane;
    private Image gameGridImage;
    private ImageView gameGridImageView;
    private Label gameGridImageLabels[];
    private ImageView[] gameGridImageViews;
    private StackPane gameAndCardPanel;
    private int currentGrid;

    private Button playerName;
    private StackPane cardPanel;

    private VBox rightPanel;
    private Button btnGameHistory;
    private Button btnMovesRemaining;
    private Button btnAboutPlay;
    private VBox gameButtonsPanel;

    private Button gridButtons[];

    private VBox diePanel;
    private Button btnDie;

    //Stats Screen
    private HBox gameHistoryToolbar;
    private Button btnGame;
    private JScrollPane gameHistoryScrollPane;
    private JEditorPane gameHistoryPane;
    private WebView gameHistoryWebView;
    private WebEngine gameHistoryWebEngine;
    private ScrollPane gameHistoryScrollPaneFX;

    //About Screen
    private JEditorPane aboutPane;
    private JScrollPane aboutScrollPane;
    private BorderPane aboutPanel;
    private Button btnBack;

    //Containers
    private BorderPane splashScreenContainer;
    private BorderPane gameSetupScreenContainer;
    private BorderPane gameScreenContainer;
    private BorderPane gameHistoryScreenContainer;
    private BorderPane aboutMenuScreenContainer;
    private BorderPane aboutGameScreenContainer;

    public enum JourneyThroughEuropeUIState {

        SPLASH_SCREEN_STATE, PLAY_GAME_STATE, VIEW_GAME_HISTORY_STATE, VIEW_ABOUT_MENU_STATE, VIEW_ABOUT_GAME_STATE, GAME_SETUP_STATE,
        DIE1_IMAGE_STATE, DIE2_IMAGE_STATE, DIE3_IMAGE_STATE, DIE4_IMAGE_STATE, DIE5_IMAGE_STATE, DIE6_IMAGE_STATE,DEFAULT_DIE_IMAGE_NAME
    }

    public JourneyThroughEuropeUI() {
        docManager = new JourneyThroughEuropeDocumentManager(this);
        errorHandler = new JourneyThroughEuropeErrorHandler(primaryStage);
        eventHandler = new JourneyThroughEuropeEventHandler(this);
        gsm = new JourneyThroughEuropeGameStateManager(this);
    }

    public BorderPane getMainPane() {
        return mainPane;
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
        initGameSetupScreen();
        initGameHistoryScreen();
        initGameScreen();
        initAboutMenuScreen();
        initAboutGameScreen();
        initSplashScreen();
        mainPane.setCenter(workspace);
        changeWorkspace(JourneyThroughEuropeUIState.SPLASH_SCREEN_STATE);
    }

    private void initMainPane() {
        marginlessInsets = new Insets(3, 3, 3, 3);
        mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color:0010aa");
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

        gameScreenContainer = new BorderPane();
        gameScreenContainer.setStyle("-fx-background-color:white");

        gameHistoryScreenContainer = new BorderPane();
        gameHistoryScreenContainer.setStyle("-fx-background-color:white");

        aboutMenuScreenContainer = new BorderPane();
        aboutMenuScreenContainer.setStyle("-fx-background-color:white");

        aboutGameScreenContainer = new BorderPane();
        aboutGameScreenContainer.setStyle("-fx-background-color:white");

    }

    public void initSplashScreen() {

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String splashScreenImagePath = props.getProperty(JourneyThroughEuropePropertyType.SPLASH_SCREEN_IMAGE_NAME);

        splashScreenPane = new StackPane();

        Image splashScreenImage = loadImage(splashScreenImagePath);
        splashScreenImageView = new ImageView(splashScreenImage);

        splashScreenImageLabel = new Label();
        splashScreenImageLabel.setGraphic(splashScreenImageView);

        Circle circle = new Circle(25);

        btnStart = initButton(JourneyThroughEuropePropertyType.START_IMAGE_NAME);
        btnStart.setStyle("-fx-effect: dropshadow( three-pass-box , blue , 10 , 0 , 0 , 0 );");
        btnStart.setShape(circle);

        btnLoad = initButton(JourneyThroughEuropePropertyType.LOAD_IMG_NAME);
        btnLoad.setStyle("-fx-effect: dropshadow( three-pass-box , blue , 10 , 0 , 0 , 0 );");
        btnLoad.setShape(circle);

        btnAbout = initButton(JourneyThroughEuropePropertyType.ABOUT_SPLASH_IMG_NAME);
        btnAbout.setStyle("-fx-effect: dropshadow( three-pass-box , blue , 10 , 0 , 0 , 0 );");
        btnAbout.setShape(circle);

        btnExit = initButton(JourneyThroughEuropePropertyType.EXIT_IMG_NAME);
        btnExit.setStyle("-fx-effect: dropshadow( three-pass-box , blue , 10 , 0 , 0 , 0 );");
        btnExit.setShape(circle);

        btnStart.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToGameSetupRequest();
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
                eventHandler.respondToSwitchScreenRequest(JourneyThroughEuropeUI.JourneyThroughEuropeUIState.VIEW_ABOUT_MENU_STATE);
            }
        });

        btnExit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToExitGameRequest(primaryStage);
            }
        });

        HBox container = new HBox();

        container.getChildren().add(btnStart);
        container.getChildren().add(btnLoad);
        container.getChildren().add(btnAbout);
        container.getChildren().add(btnExit);
        container.setAlignment(Pos.BOTTOM_CENTER);
        container.setSpacing(5.0);
        container.setPadding(new Insets(0, 0, 30, 0));

        splashScreenPane.getChildren().add(splashScreenImageLabel);
        splashScreenPane.getChildren().add(container);
        splashScreenPane.setAlignment(Pos.CENTER);
        splashScreenContainer.setCenter(splashScreenPane);
        workspace.getChildren().add(splashScreenContainer);
    }

    private void initAboutMenuScreen() {
       
        aboutPane = new JEditorPane();
        aboutPane.setEditable(false);

        
        aboutPane.setContentType("text/html");

       
        loadPage(aboutPane, JourneyThroughEuropePropertyType.ABOUT_FILE_NAME);

        aboutScrollPane = new JScrollPane(aboutPane);
        SwingNode aboutSwingNode = new SwingNode();
        aboutSwingNode.setContent(aboutScrollPane);

        btnBack = this.initButton(JourneyThroughEuropePropertyType.BACK_IMG_NAME);
        btnBack.setShape(new Circle(1));
        btnBack.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 0 , 0 , 0 );");

        // WE'LL PUT THE HOME BUTTON IN A TOOLBAR IN THE NORTH OF THIS SCREEN,
        // UNDER THE NORTH TOOLBAR THAT'S SHARED BETWEEN THE THREE SCREENS
        FlowPane aboutToolbar = new FlowPane();
        aboutToolbar.setPadding(new Insets(5, 5, 5, 5));
        aboutPanel = new BorderPane();
        aboutSwingNode.setStyle("-fx-background-color: #FFFFFF");
        aboutPanel.setCenter(aboutSwingNode);
        aboutToolbar.getChildren().add(btnBack);
        aboutToolbar.setStyle("-fx-background-color: #FFFFFF");

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                eventHandler.respondToBackRequest(JourneyThroughEuropeUI.JourneyThroughEuropeUIState.SPLASH_SCREEN_STATE);
            }
        });

        // LET OUR HELP SCREEN JOURNEY AROUND THE WEB VIA HYPERLINK
        //aboutPane.addHyperlinkListener(new HelpHyperlinkListener(this));
        aboutMenuScreenContainer.setTop(aboutToolbar);
        aboutMenuScreenContainer.setCenter(aboutPanel);
        workspace.getChildren().add(aboutMenuScreenContainer);
    }

    private void initAboutGameScreen() {

        // WE'LL DISPLAY ALL HELP INFORMATION USING HTML
        JEditorPane aboutPane = new JEditorPane();
        aboutPane.setEditable(false);

        // NOW LOAD THE HELP HTML
        aboutPane.setContentType("text/html");

        // LOAD THE HELP PAGE
        loadPage(aboutPane, JourneyThroughEuropePropertyType.ABOUT_FILE_NAME);

        JScrollPane aboutScrollPane = new JScrollPane(aboutPane);
        SwingNode aboutSwingNode = new SwingNode();
        aboutSwingNode.setContent(aboutScrollPane);

        Button btnGameAbout = this.initButton(JourneyThroughEuropePropertyType.GAME_IMG_NAME);
        btnGameAbout.setShape(new Circle(1));
        btnGameAbout.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 0 , 0 , 0 );");

        btnGameAbout.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToGameHistoryRequest(JourneyThroughEuropeUIState.PLAY_GAME_STATE);
            }
        });

        FlowPane aboutToolbar = new FlowPane();
        aboutToolbar.setPadding(new Insets(5, 5, 5, 5));
        BorderPane aboutPanel = new BorderPane();
        aboutSwingNode.setStyle("-fx-background-color: #FFFFFF");
        aboutPanel.setCenter(aboutSwingNode);
        aboutToolbar.getChildren().add(btnGameAbout);
        aboutToolbar.setStyle("-fx-background-color: #FFFFFF");

        //aboutPane.addHyperlinkListener(new HelpHyperlinkListener(this));
        aboutGameScreenContainer.setTop(aboutToolbar);
        aboutGameScreenContainer.setCenter(aboutPanel);
        workspace.getChildren().add(aboutGameScreenContainer);
    }

    private void initGameSetupScreen() {

        northPanel = new HBox();
        centerPanel = new FlowPane();
        numPlayers = 1;
        playersManager = new ArrayList<PlayerManager>();

        playerSelectionComboBox = new ComboBox();
        playerSelectionComboBox.getItems().addAll("1", "2", "3", "4", "5", "6");
        playerSelectionComboBox.setValue("1");
        playerSelectionComboBox.setShape(new Circle(1));
        playerSelectionComboBox.setStyle("-fx-font-size: 16px;");

        playerSelectionComboBox.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                numPlayers = Integer.parseInt(playerSelectionComboBox.getValue().toString());
                eventHandler.respondToChangeNumberOfPlayersRequest(numPlayers);
            }
        });

        Label lblPlayerSelection = this.initLabel(JourneyThroughEuropePropertyType.NUM_PLAYERS_IMG_NAME);

        btnGo = this.initButton(JourneyThroughEuropePropertyType.GO_IMG_NAME);
        btnGo.setShape(new Circle(1));
        btnGo.setStyle("-fx-effect: dropshadow( three-pass-box , sandybrown , 10 , 0 , 0 , 0 );");
        btnGo.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToStartGameRequest(JourneyThroughEuropeUIState.PLAY_GAME_STATE);
            }
        });

        northPanel.setSpacing(10);
        northPanel.setPadding(new Insets(10, 0, 10, 50));
        northPanel.setAlignment(Pos.CENTER_LEFT);
        northPanel.getChildren().add(lblPlayerSelection);
        northPanel.getChildren().add(playerSelectionComboBox);
        northPanel.getChildren().add(btnGo);

        centerPanel.setPadding(marginlessInsets);
        centerPanel.setAlignment(Pos.CENTER);
        centerPanel.setVgap(5.0);
        centerPanel.setHgap(5.0);

        playerGridPanes = new VBox[MAX_PLAYERS];
        playerGridContainers = new StackPane[MAX_PLAYERS];

        for (int i = 0; i < playerGridPanes.length; i++) {
            playerGridPanes[i] = setupPlayerGridPane(i);
            playerGridContainers[i] = setupPlayerGridContainers();
            playerGridContainers[i].getChildren().add(playerGridPanes[i]);
            playersManager.get(i).setPlayerName("Player " + (i + 1));
            centerPanel.getChildren().add(playerGridContainers[i]);
        }

        disablePlayerGridPanes();
        enablePlayerGridPanes(1);
        gameSetupScreenContainer.setTop(northPanel);
        gameSetupScreenContainer.setCenter(centerPanel);
        workspace.getChildren().add(gameSetupScreenContainer);
    }

    private void initGameScreen() {

        currentGrid = -1;

        gameGridImageViews = new ImageView[4];
        gameGridImageViews[0] = initImageView(JourneyThroughEuropePropertyType.MAP_IMAGE_NAME);
        gameGridImageViews[1] = initImageView(JourneyThroughEuropePropertyType.MAP_IMAGE_NAME);
        gameGridImageViews[2] = initImageView(JourneyThroughEuropePropertyType.MAP_IMAGE_NAME);
        gameGridImageViews[3] = initImageView(JourneyThroughEuropePropertyType.MAP_IMAGE_NAME);

        gameGridImageLabels = new Label[4];
        gameGridImageLabels[0] = new Label();
        gameGridImageLabels[0].setGraphic(gameGridImageViews[0]);
       
        gameGridImageLabels[1] = new Label();
        gameGridImageLabels[1].setGraphic(gameGridImageViews[1]);
        
        gameGridImageLabels[2] = new Label();
        gameGridImageLabels[2].setGraphic(gameGridImageViews[2]);
        
        gameGridImageLabels[3] = new Label();
        gameGridImageLabels[3].setGraphic(gameGridImageViews[3]);

        gamePanel = new StackPane();
        gamePanel.setStyle("-fx-border-color:black;" + "-fx-border-width: 2px;");
        gamePanel.getChildren().add(gameGridImageLabels[0]);
        gamePanel.setFocusTraversable(true);
        //gameGridImageView.fitWidthProperty().bind(gamePanel.widthProperty());
        //gameGridImageView.fitHeightProperty().bind(gamePanel.heightProperty());

        gameCanvas = new Pane();
        gamePanel.getChildren().add(gameCanvas);

        gameGridScrollPane = new ScrollPane();
        gameGridScrollPane.setContent(gamePanel);
        gameGridScrollPane.setFitToHeight(true);
        gameGridScrollPane.setFitToWidth(true);

        playerName = new Button("Player 1");
        playerName.setMaxWidth(Double.MAX_VALUE);
        playerName.setStyle("-fx-background-color:#000000,"
                + " linear-gradient(#7db9e8, #304b8e),"
                + " linear-gradient(#3e66b0, #233c74),"
                + " linear-gradient(#3256a7, #1e3361);"
                + " -fx-background-insets: 0,1,2,3;"
                + " -fx-background-radius: 3,2,2,2;"
                + " -fx-padding: 3,4,3,4;"
                + " -fx-text-fill: white;"
                + " -fx-font-size: 15px;"
                + " -fx-font-family: \"Arial\";"
                + " -fx-font-weight: bold;");
        
        

        cardPanel = new StackPane();
        cardCanvas = new Pane();
        cardPanel.getChildren().add(new Rectangle(242, 656, Color.WHITE));
        cardPanel.getChildren().add(cardCanvas);

        gameButtonsPanel = new VBox();
        gameButtonsPanel.setAlignment(Pos.TOP_CENTER);

        btnGameHistory = this.initButton(JourneyThroughEuropePropertyType.GAME_HISTORY_IMAGE_NAME);
        btnGameHistory.setShape(new Circle(1));
        btnGameHistory.setStyle("-fx-effect: dropshadow( three-pass-box , black , 10 , 0 , 0 , 0 );");
        btnGameHistory.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToGameHistoryRequest(JourneyThroughEuropeUIState.VIEW_GAME_HISTORY_STATE);
            }
        });

        btnAboutPlay = this.initButton(JourneyThroughEuropePropertyType.ABOUT_IMAGE_NAME);
        btnAboutPlay.setShape(new Circle(1));
        btnAboutPlay.setStyle("-fx-effect: dropshadow( three-pass-box , black , 10 , 0 , 0 , 0 );");
        btnAboutPlay.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToSwitchScreenRequest(JourneyThroughEuropeUIState.VIEW_ABOUT_GAME_STATE);
            }
        });

        VBox gameHistoryPanel = new VBox();
        gameHistoryPanel.setAlignment(Pos.CENTER);

        gameHistoryPanel.setPadding(new Insets(100, 0, 0, 30));
        gameHistoryPanel.setSpacing(10.0);
        gameHistoryPanel.getChildren().add(btnGameHistory);
        gameHistoryPanel.getChildren().add(btnAboutPlay);

        VBox gridButtonsFirstColumnBox = new VBox();
        gridButtonsFirstColumnBox.setAlignment(Pos.CENTER);
        gridButtonsFirstColumnBox.setSpacing(3);
        Label lblAC = initLabel(JourneyThroughEuropePropertyType.AC_IMAGE_NAME);
        lblAC.setPadding(new Insets(0, 0, 5, 0));
        gridButtonsFirstColumnBox.getChildren().add(lblAC);

        VBox gridButtonsSecondColumnBox = new VBox();
        gridButtonsSecondColumnBox.setAlignment(Pos.CENTER);
        gridButtonsSecondColumnBox.setSpacing(3);
        Label lblDF = initLabel(JourneyThroughEuropePropertyType.DF_IMAGE_NAME);
        lblDF.setPadding(new Insets(0, 0, 5, 0));
        gridButtonsSecondColumnBox.getChildren().add(lblDF);

        VBox mapNumberPanel = new VBox();
        gridButtonsSecondColumnBox.setAlignment(Pos.CENTER);
        mapNumberPanel.setPadding(new Insets(65, 5, 0, 0));
        mapNumberPanel.setSpacing(30.0);
        Label lblOneThroughFour = initLabel(JourneyThroughEuropePropertyType.F4_IMAGE_NAME);
        Label lblFiveThroughEight = initLabel(JourneyThroughEuropePropertyType.F8_IMAGE_NAME);
        lblFiveThroughEight.setAlignment(Pos.CENTER);
        lblOneThroughFour.setAlignment(Pos.CENTER);

        mapNumberPanel.getChildren().addAll(lblOneThroughFour, lblFiveThroughEight);

        gridButtons = new Button[4];
        gridButtons[0] = initButton(JourneyThroughEuropePropertyType.GRID1_BUTTON_IMAGE_NAME);
        gridButtons[0].setStyle("-fx-effect: dropshadow( three-pass-box , black , 10 , 0 , 0 , 0 );");
        gridButtons[0].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToChangeGridRequest(GRID1_IMAGE_STATE);
            }
        });

        gridButtons[1] = initButton(JourneyThroughEuropePropertyType.GRID2_BUTTON_IMAGE_NAME);
        gridButtons[1].setStyle("-fx-effect: dropshadow( three-pass-box , black , 10 , 0 , 0 , 0 );");
        gridButtons[1].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToChangeGridRequest(GRID2_IMAGE_STATE);
            }
        });

        gridButtons[2] = initButton(JourneyThroughEuropePropertyType.GRID3_BUTTON_IMAGE_NAME);
        gridButtons[2].setStyle("-fx-effect: dropshadow( three-pass-box , black , 10 , 0 , 0 , 0 );");
        gridButtons[2].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToChangeGridRequest(GRID3_IMAGE_STATE);
            }
        });

        gridButtons[3] = initButton(JourneyThroughEuropePropertyType.GRID4_BUTTON_IMAGE_NAME);
        gridButtons[3].setStyle("-fx-effect: dropshadow( three-pass-box , black , 10 , 0 , 0 , 0 );");
        gridButtons[3].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToChangeGridRequest(GRID4_IMAGE_STATE);
            }
        });

        gridButtonsFirstColumnBox.getChildren().addAll(gridButtons[0], gridButtons[2]);
        gridButtonsSecondColumnBox.getChildren().addAll(gridButtons[1], gridButtons[3]);

        VBox movesRemainingPanel = new VBox();
        movesRemainingPanel.setAlignment(Pos.CENTER);
        
        btnMovesRemaining = new Button("Moves Remaining: 0" );
        btnMovesRemaining.setMaxWidth(Double.MAX_VALUE);
        btnMovesRemaining.setStyle("-fx-background-color:#000000,"
                + " linear-gradient(#7db9e8, #304b8e),"
                + " linear-gradient(#3e66b0, #233c74),"
                + " linear-gradient(#3256a7, #1e3361);"
                + " -fx-background-insets: 0,1,2,3;"
                + " -fx-background-radius: 3,2,2,2;"
                + " -fx-padding: 3,4,3,4;"
                + " -fx-text-fill: white;"
                + " -fx-font-size: 15px;"
                + " -fx-font-family: \"Arial\";"
                + " -fx-font-weight: bold;");
        
        movesRemainingPanel.getChildren().add(btnMovesRemaining);
        
        diePanel = new VBox();
        diePanel.setAlignment(Pos.CENTER);
        diePanel.setPadding(new Insets(30, 0, 50, 30));

        btnDie = this.initButton(JourneyThroughEuropePropertyType.DEFAULT_DIE_IMAGE_NAME);
        btnDie.setDisable(true);
        btnDie.setStyle("-fx-effect: dropshadow( three-pass-box , black , 10 , 0 , 0 , 0 );");
        btnDie.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToRollRequest();
            }
        });

        Label lblRollDie = this.initLabel(JourneyThroughEuropePropertyType.ROLL_DIE_IMAGE_NAME);
        diePanel.getChildren().addAll(lblRollDie, btnDie);
        diePanel.setSpacing(5.0);

        HBox gridButtonsContainer = new HBox();
        gridButtonsContainer.setAlignment(Pos.CENTER);
        gridButtonsContainer.setSpacing(2);
        gridButtonsContainer.getChildren().add(mapNumberPanel);
        gridButtonsContainer.getChildren().add(gridButtonsFirstColumnBox);
        gridButtonsContainer.getChildren().add(gridButtonsSecondColumnBox);

        gameButtonsPanel.getChildren().add(movesRemainingPanel);
        gameButtonsPanel.getChildren().add(diePanel);
        gameButtonsPanel.getChildren().add(gridButtonsContainer);
        gameButtonsPanel.getChildren().add(gameHistoryPanel);

        StackPane rightPaneContainer = new StackPane();
        rightPaneContainer.getChildren().add(new Rectangle(242, 636, Color.WHITE));
        rightPaneContainer.getChildren().add(gameButtonsPanel);

        rightPanel = new VBox();
        rightPanel.getChildren().add(rightPaneContainer);
        rightPanel.setStyle("-fx-border-color:black;" + "-fx-border-width: 2px;");

        leftPanel = new VBox();
        leftPanel.getChildren().add(playerName);
        leftPanel.getChildren().add(cardPanel);
        leftPanel.setStyle("-fx-border-color:black;" + "-fx-border-width: 2px;");

        gameAndCardPanel = new StackPane();
        BorderPane gameAndCardPanelContainer = new BorderPane();
        gameAndCardPanelContainer.setCenter(gameGridScrollPane);
        gameAndCardPanelContainer.setLeft(leftPanel);
        gameAndCardPanel.getChildren().add(gameAndCardPanelContainer);

        gameGridScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        gameGridScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        gameGridScrollPane.setPannable(true);

        gameScreenContainer.setLeft(leftPanel);
        gameScreenContainer.setCenter(gameGridScrollPane);
        gameScreenContainer.setRight(rightPanel);
        workspace.getChildren().add(gameScreenContainer);

    }

    private void initGameHistoryScreen() {

        gameHistoryToolbar = new HBox();
        gameHistoryToolbar.setPadding(new Insets(5, 5, 5, 5));

        btnGame = initButton(JourneyThroughEuropePropertyType.GAME_IMG_NAME);
        btnGame.setShape(new Circle(1));
        btnGame.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 0 , 0 , 0 );");

        btnGame.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToSwitchScreenRequest(JourneyThroughEuropeUI.JourneyThroughEuropeUIState.PLAY_GAME_STATE);
            }
        });

        // WE'LL DISPLAY ALL STATS IN A JEditorPane
        gameHistoryPane = new JEditorPane();
        gameHistoryPane.setEditable(false);
        gameHistoryPane.setContentType("text/html");

        gameHistoryScrollPane = new JScrollPane(gameHistoryPane);

        SwingNode gameHistorySwingNode = new SwingNode();
        gameHistorySwingNode.setContent(gameHistoryScrollPane);

       
        loadPage(gameHistoryPane, JourneyThroughEuropePropertyType.GAME_HISTORY_FILE_NAME);
        //HTMLDocument statsDoc = (HTMLDocument) gameHistoryPane.getDocument();
        //docManager.setStatsDoc(statsDoc);

        gameHistoryWebView = new WebView();
        gameHistoryWebEngine = gameHistoryWebView.getEngine();
        gameHistoryWebEngine.loadContent(gameHistoryPane.getText());

        gameHistoryScrollPaneFX = new ScrollPane();
        gameHistoryScrollPaneFX.setContent(gameHistoryWebView);
        gameHistoryScrollPaneFX.setFitToHeight(true);
        gameHistoryScrollPaneFX.setFitToWidth(true);

        gameHistoryToolbar.getChildren().add(btnGame);

        gameHistoryScreenContainer.setTop(gameHistoryToolbar);
        gameHistoryScreenContainer.setCenter(gameHistoryScrollPaneFX);

        workspace.getChildren().add(gameHistoryScreenContainer);
    }

    public void changeWorkspace(JourneyThroughEuropeUIState uiScreen) {
        disableAllPanes();

        switch (uiScreen) {
            case SPLASH_SCREEN_STATE:
                splashScreenContainer.setVisible(true);
                mainPane.setStyle("-fx-background-color:blue");
                break;
            case PLAY_GAME_STATE:
                gameScreenContainer.setVisible(true);
                mainPane.setStyle("-fx-background-color:cb0d11");
                break;
            case VIEW_GAME_HISTORY_STATE:
                gameHistoryScreenContainer.setVisible(true);
                mainPane.setStyle("-fx-background-color:black");
                break;
            case VIEW_ABOUT_MENU_STATE:
                aboutMenuScreenContainer.setVisible(true);
                mainPane.setStyle("-fx-background-color:black");
                break;
            case VIEW_ABOUT_GAME_STATE:
                aboutGameScreenContainer.setVisible(true);
                mainPane.setStyle("-fx-background-color:black");
                break;
            case GAME_SETUP_STATE:
                gameSetupScreenContainer.setVisible(true);
                mainPane.setStyle("-fx-background-color:brown");
                break;
            default:
        }
    }

    public void changeGridImage(int gridImageState) {
        double gridWidth = gamePanel.getWidth();
        double gridHeight = gamePanel.getHeight();
        switch (gridImageState) {
            case GRID1_IMAGE_STATE:
                gamePanel.getChildren().clear();
                gamePanel.getChildren().add(gameGridImageLabels[0]);
                gamePanel.getChildren().add(gameCanvas);
                
                gameGridScrollPane.setHvalue(0);
                gameGridScrollPane.setVvalue(0);
                
                currentGrid = 1;
                break;
            case GRID2_IMAGE_STATE:
                gamePanel.getChildren().clear();
                gamePanel.getChildren().add(gameGridImageLabels[1]);
                gamePanel.getChildren().add(gameCanvas);
                
                gameGridScrollPane.setHvalue(gridWidth/gridWidth);
                gameGridScrollPane.setVvalue(0);
                
                currentGrid = 2;
                break;
            case GRID3_IMAGE_STATE:
                gamePanel.getChildren().clear();
                gamePanel.getChildren().add(gameGridImageLabels[2]);
                gamePanel.getChildren().add(gameCanvas);
                
                gameGridScrollPane.setHvalue(0);
                gameGridScrollPane.setVvalue(gridHeight/gridHeight);
                
                currentGrid = 3;
                break;
            case GRID4_IMAGE_STATE:
                gamePanel.getChildren().clear();
                gamePanel.getChildren().add(gameGridImageLabels[3]);
                gamePanel.getChildren().add(gameCanvas);
                
                gameGridScrollPane.setHvalue(gridWidth/gridWidth);
                gameGridScrollPane.setVvalue(gridHeight/gridHeight);
                
                currentGrid = 4;
                break;
        }
    }

    public void changeDieImage(JourneyThroughEuropeUIState dieImageState) {
        switch (dieImageState) {
            case DIE1_IMAGE_STATE:
                btnDie.setGraphic(setupImageView(JourneyThroughEuropePropertyType.DIE1_IMAGE_NAME));
                break;
            case DIE2_IMAGE_STATE:
                btnDie.setGraphic(setupImageView(JourneyThroughEuropePropertyType.DIE2_IMAGE_NAME));
                break;
            case DIE3_IMAGE_STATE:
                btnDie.setGraphic(setupImageView(JourneyThroughEuropePropertyType.DIE3_IMAGE_NAME));
                break;
            case DIE4_IMAGE_STATE:
                btnDie.setGraphic(setupImageView(JourneyThroughEuropePropertyType.DIE4_IMAGE_NAME));
                break;
            case DIE5_IMAGE_STATE:
                btnDie.setGraphic(setupImageView(JourneyThroughEuropePropertyType.DIE5_IMAGE_NAME));
                break;
            case DIE6_IMAGE_STATE:
                btnDie.setGraphic(setupImageView(JourneyThroughEuropePropertyType.DIE6_IMAGE_NAME));
                break;
            case DEFAULT_DIE_IMAGE_NAME:
                btnDie.setGraphic(setupImageView(JourneyThroughEuropePropertyType.DEFAULT_DIE_IMAGE_NAME));
                break;
        }
    }

    public void loadPage(JEditorPane jep, JourneyThroughEuropePropertyType fileProperty) {
        // GET THE FILE NAME
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String fileName = props.getProperty(fileProperty);
        try {
            // LOAD THE HTML INTO THE EDITOR PANE
            String fileHTML = JourneyThroughEuropeFileLoader.loadTextFile(fileName);
            jep.setText(fileHTML);
        } catch (IOException ioe) {
            errorHandler.processError(JourneyThroughEuropePropertyType.INVALID_URL_ERROR_TEXT);
        }
    }

    public ImageView setupImageView(JourneyThroughEuropePropertyType prop) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imageName = props.getProperty(prop);

        // LOAD THE IMAGE
        Image image = loadImage(imageName);
        ImageView imageIcon = new ImageView(image);

        return imageIcon;
    }

    public Button initButton(JourneyThroughEuropePropertyType prop) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imageName = props.getProperty(prop);

        // LOAD THE IMAGE
        Image image = loadImage(imageName);
        ImageView imageIcon = new ImageView(image);

        // MAKE THE BUTTON
        Button button = new Button();
        button.setGraphic(imageIcon);

        return button;
    }

    public Label initLabel(JourneyThroughEuropePropertyType prop) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imageName = props.getProperty(prop);

        // LOAD THE IMAGE
        Image image = loadImage(imageName);
        ImageView imageIcon = new ImageView(image);

        // MAKE THE BUTTON
        Label label = new Label();
        label.setGraphic(imageIcon);

        return label;
    }

    public ImageView initImageView(JourneyThroughEuropePropertyType prop) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imageName = props.getProperty(prop);

        // LOAD THE IMAGE
        Image image = loadImage(imageName);
        ImageView imageIcon = new ImageView(image);

        return imageIcon;
    }

    public Image initPlayerImage(int imageNum) {

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imageName = "";

        switch (imageNum) {
            case 0:
                imageName = props.getProperty(JourneyThroughEuropePropertyType.BLUE_BUTTON);
                break;
            case 1:
                imageName = props.getProperty(JourneyThroughEuropePropertyType.GREEN_BUTTON);
                break;
            case 2:
                imageName = props.getProperty(JourneyThroughEuropePropertyType.GRAY_BUTTON);
                break;
            case 3:
                imageName = props.getProperty(JourneyThroughEuropePropertyType.LIGHTBLUE_BUTTON);
                break;
            case 4:
                imageName = props.getProperty(JourneyThroughEuropePropertyType.RED_BUTTON);
                break;
            case 5:
                imageName = props.getProperty(JourneyThroughEuropePropertyType.YELLOW_BUTTON);
                break;
        }

        Image image = loadImage(imageName);
        return image;
    }
    
    
    public Image initFlagImage(int imageNum) {

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imageName = "";

        switch (imageNum) {
            case 0:
                imageName = props.getProperty(JourneyThroughEuropePropertyType.BLUE_FLAG);
                break;
            case 1:
                imageName = props.getProperty(JourneyThroughEuropePropertyType.GREEN_FLAG);
                break;
            case 2:
                imageName = props.getProperty(JourneyThroughEuropePropertyType.GRAY_FLAG);
                break;
            case 3:
                imageName = props.getProperty(JourneyThroughEuropePropertyType.LIGHTBLUE_FLAG);
                break;
            case 4:
                imageName = props.getProperty(JourneyThroughEuropePropertyType.RED_FLAG);
                break;
            case 5:
                imageName = props.getProperty(JourneyThroughEuropePropertyType.YELLOW_FLAG);
                break;
        }

        Image image = loadImage(imageName);
        return image;
    }

    public void disableAllPanes() {
        splashScreenContainer.setVisible(false);
        gameSetupScreenContainer.setVisible(false);
        gameScreenContainer.setVisible(false);
        gameHistoryScreenContainer.setVisible(false);
        aboutMenuScreenContainer.setVisible(false);
        aboutGameScreenContainer.setVisible(false);
    }

    public VBox setupPlayerGridPane(int flagColor) {
        VBox playerPaneContainer = new VBox();
        playerPaneContainer.setAlignment(Pos.CENTER);
        playerPaneContainer.setPadding(new Insets(0, 0, 80, 0));
        playerPaneContainer.setSpacing(15.0);

        HBox flagLabelPane = new HBox();
        flagLabelPane.setAlignment(Pos.CENTER);
        flagLabelPane.setPadding(new Insets(0, 0, 0, 55));
        Label flagLabel = this.initLabel(JourneyThroughEuropePropertyType.BLUE_FLAG);
        switch (flagColor) {
            case 0:
                flagLabel = this.initLabel(JourneyThroughEuropePropertyType.BLUE_FLAG);
                break;
            case 1:
                flagLabel = this.initLabel(JourneyThroughEuropePropertyType.GREEN_FLAG);
                break;
            case 2:
                flagLabel = this.initLabel(JourneyThroughEuropePropertyType.GRAY_FLAG);
                break;
            case 3:
                flagLabel = this.initLabel(JourneyThroughEuropePropertyType.LIGHTBLUE_FLAG);
                break;
            case 4:
                flagLabel = this.initLabel(JourneyThroughEuropePropertyType.RED_FLAG);
                break;
            case 5:
                flagLabel = this.initLabel(JourneyThroughEuropePropertyType.YELLOW_FLAG);
                break;
        }
        flagLabelPane.getChildren().add(flagLabel);

        HBox playerPane = new HBox();
        playerPane.setAlignment(Pos.CENTER);
        playerPane.setPadding(marginlessInsets);
        playerPane.setSpacing(25);

        ToggleGroup group = new ToggleGroup();
        RadioButton player = new RadioButton("Player");
        RadioButton computer = new RadioButton("Computer");
        player.setSelected(true);
        player.setToggleGroup(group);
        computer.setToggleGroup(group);

        Label lblName = new Label("Name:");
        TextField txtName = new TextField();
        txtName.setPrefColumnCount(5);

        VBox playerNamePane = new VBox();
        playerNamePane.setAlignment(Pos.CENTER);
        playerNamePane.setSpacing(5);
        playerNamePane.getChildren().add(lblName);
        playerNamePane.getChildren().add(txtName);

        VBox playerTypePane = new VBox();
        playerTypePane.setAlignment(Pos.CENTER_LEFT);
        playerTypePane.getChildren().add(player);
        playerTypePane.getChildren().add(computer);
        playerTypePane.setSpacing(5);

        PlayerManager temp = new PlayerManager(txtName, true);
        playersManager.add(temp);
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                    Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    String playerStatus = ((RadioButton) group.getSelectedToggle()).getText();
                    if (playerStatus.equalsIgnoreCase("Player")) {
                        temp.setHuman(true);
                    }
                    if (playerStatus.equalsIgnoreCase("Computer")) {
                        temp.setHuman(false);
                    }
                }
            }
        });

        playerPane.getChildren().add(playerTypePane);
        playerPane.getChildren().add(playerNamePane);

        playerPaneContainer.getChildren().add(flagLabelPane);
        playerPaneContainer.getChildren().add(playerPane);

        return playerPaneContainer;
    }

    public StackPane setupPlayerGridContainers() {

        StackPane pane = new StackPane();
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-border-color:brown;" + "-fx-border-width: 3px;" + "-fx-effect: dropshadow( three-pass-box , brown , 10 , 0 , 0 , 0 );");
        pane.getChildren().add(new Rectangle(300, 275, Color.SANDYBROWN));

        return pane;
    }

    public void disablePlayerGridPanes() {
        for (int i = 0; i < playerGridPanes.length; i++) {
            playerGridPanes[i].setVisible(false);
        }
    }

    public void enablePlayerGridPanes(int numPlayers) {
        for (int i = 0; i < numPlayers; i++) {
            playerGridPanes[i].setVisible(true);
        }
    }

    public void setGameToScreen(GameRenderer gameRenderer) {
        gameCanvas.getChildren().clear();
        gameCanvas.getChildren().add(gameRenderer);
    }

    public void setCardToScreen(CardRenderer cardRenderer) {
        cardCanvas.getChildren().clear();
        cardCanvas.getChildren().add(cardRenderer);
    }

    public GameRenderer getGameRenderer() {
        return gameRenderer;
    }

    public ArrayList<PlayerManager> getPlayers() {
        return playersManager;
    }

    public void setCurrentPlayer(int player) {
        playerName.setText(playersManager.get(player).getPlayerName());
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public StackPane getCardPanel() {
        return cardPanel;
    }

    public StackPane getGamePanel() {
        return gamePanel;
    }

    public ScrollPane getGameScrollPane() {
        return gameGridScrollPane;
    }

    public int getCurrentGrid() {
        return currentGrid;
    }

    public ImageView[] getGameGridImages() {
        return gameGridImageViews;
    }
    
    public void disableGridButtons()
    {
        for(int i = 0; i<gridButtons.length; i++)
            gridButtons[i].setDisable(true);
    }
    
    public void enableGridButtons()
    {
        for(int i = 0; i<gridButtons.length; i++)
            gridButtons[i].setDisable(false);
    }
            
    public void disableRollButton()
    {
        btnDie.setDisable(true);
    }
    
    public void enableRollButton()
    {
        btnDie.setDisable(false);
    }
    
    public boolean isRollButtonDisabled()
    {
        return btnDie.isDisabled();
    }
    
    public void resetRollImage()
    {
        changeDieImage(JourneyThroughEuropeUIState.DEFAULT_DIE_IMAGE_NAME);
    }
    
    public void updateMovesRemaining(String movesRemaining)
    {
        btnMovesRemaining.setText(movesRemaining);
    }

}

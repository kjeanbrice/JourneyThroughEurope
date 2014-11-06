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
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLDocument;
import journeythrougheurope.application.Main;
import journeythrougheurope.application.Main.JourneyThroughEuropePropertyType;
import journeythrougheurope.file.JourneyThroughEuropeFileLoader;
import journeythrougheurope.game.GameRenderer;
import journeythrougheurope.game.JourneyThroughEuropeGameStateManager;
import properties_manager.PropertiesManager;

/**
 *
 * @author Karl
 */
public class JourneyThroughEuropeUI extends Pane {

    private GameRenderer gameManager;
    private JourneyThroughEuropeMouseHandler mouseHandler;

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
    private HBox playerGridPanes[];
    private StackPane playerGridContainers[];
    private Button btnGo;

    //Game Screen
    private VBox leftPanel;
    private StackPane gamePanel;
    private Pane canvas;
    private Image gameGridImage;
    private ImageView gameGridImageView;
    private Label gameGridImageLabels[];

    private Button playerName;
    private StackPane cardPanel;

    private VBox rightPanel;
    private Button btnGameHistory;
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

    //Containers
    private BorderPane splashScreenContainer;
    private BorderPane gameSetupScreenContainer;
    private BorderPane gameScreenContainer;
    private BorderPane gameHistoryScreenContainer;

    public enum JourneyThroughEuropeUIState {

        SPLASH_SCREEN_STATE, PLAY_GAME_STATE, VIEW_GAME_HISTORY_STATE, VIEW_ABOUT_STATE, GAME_SETUP_STATE,
        GRID1_IMAGE_STATE, GRID2_IMAGE_STATE, GRID3_IMAGE_STATE, GRID4_IMAGE_STATE, DIE1_IMAGE_STATE, DIE2_IMAGE_STATE,
        DIE3_IMAGE_STATE, DIE4_IMAGE_STATE, DIE5_IMAGE_STATE, DIE6_IMAGE_STATE
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
        initGameHistoryScreen();
        initGameScreen();
        changeWorkspace(JourneyThroughEuropeUIState.SPLASH_SCREEN_STATE);
    }

    private void initMainPane() {
        marginlessInsets = new Insets(3, 3, 3, 3);
        mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color:blue");
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

        /*helpScreenContainer = new BorderPane();
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

        btnStart = initButton(JourneyThroughEuropePropertyType.START_IMAGE_NAME);
        btnLoad = initButton(JourneyThroughEuropePropertyType.LOAD_IMG_NAME);
        btnAbout = initButton(JourneyThroughEuropePropertyType.ABOUT_SPLASH_IMG_NAME);
        btnExit = initButton(JourneyThroughEuropePropertyType.EXIT_IMG_NAME);

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

                //eventHandler.respondToSwitchScreenRequest();
            }
        });

        btnExit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToExitGameRequest(primaryStage);
            }
        });

        VBox container = new VBox();

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

    private void initAboutScreen() {

    }

    private void initGameSetupScreen() {

        northPanel = new HBox();
        centerPanel = new FlowPane();

        playerSelectionComboBox = new ComboBox();
        playerSelectionComboBox.getItems().addAll("1", "2", "3", "4", "5", "6");
        playerSelectionComboBox.setValue("1");

        playerSelectionComboBox.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int numPlayers = Integer.parseInt(playerSelectionComboBox.getValue().toString());
                eventHandler.respondToChangeNumberOfPlayersRequest(numPlayers);
            }
        });

        Label lblPlayerSelection = new Label("Number of Players :");

        btnGo = new Button("GO!");
        btnGo.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToStartGameRequest(JourneyThroughEuropeUIState.PLAY_GAME_STATE);
            }
        });

        northPanel.setSpacing(10);
        northPanel.setPadding(new Insets(10, 10, 10, 10));
        northPanel.setAlignment(Pos.CENTER_LEFT);
        northPanel.getChildren().add(lblPlayerSelection);
        northPanel.getChildren().add(playerSelectionComboBox);
        northPanel.getChildren().add(btnGo);

        centerPanel.setPadding(marginlessInsets);
        centerPanel.setAlignment(Pos.CENTER);
        centerPanel.setVgap(5.0);
        centerPanel.setHgap(5.0);

        playerGridPanes = new HBox[MAX_PLAYERS];
        playerGridContainers = new StackPane[MAX_PLAYERS];

        for (int i = 0; i < playerGridPanes.length; i++) {
            playerGridPanes[i] = setupPlayerGridPane();
            playerGridContainers[i] = setupPlayerGridContainers();
            playerGridContainers[i].getChildren().add(playerGridPanes[i]);
            centerPanel.getChildren().add(playerGridContainers[i]);
        }

        disablePlayerGridPanes();
        enablePlayerGridPanes(1);
        gameSetupScreenContainer.setTop(northPanel);
        gameSetupScreenContainer.setCenter(centerPanel);
        workspace.getChildren().add(gameSetupScreenContainer);
    }

    private void initGameScreen() {

        gameGridImageLabels = new Label[4];
        gameGridImageLabels[0] = initLabel(JourneyThroughEuropePropertyType.GAME_GRID1_IMAGE_NAME);
        gameGridImageLabels[1] = initLabel(JourneyThroughEuropePropertyType.GAME_GRID2_IMAGE_NAME);
        gameGridImageLabels[2] = initLabel(JourneyThroughEuropePropertyType.GAME_GRID3_IMAGE_NAME);
        gameGridImageLabels[3] = initLabel(JourneyThroughEuropePropertyType.GAME_GRID4_IMAGE_NAME);

        gamePanel = new StackPane();
        gamePanel.setStyle("-fx-border-color:black;" + "-fx-border-width: 2px;");
        gamePanel.getChildren().add(gameGridImageLabels[0]);
        //gameGridImageView.fitWidthProperty().bind(gamePanel.widthProperty());
        //gameGridImageView.fitHeightProperty().bind(gamePanel.heightProperty());

        canvas = new Pane();
        gamePanel.getChildren().add(canvas);

        playerName = new Button("Player 1");
        playerName.setMaxWidth(Double.MAX_VALUE);
        playerName.setStyle("-fx-background-color:#000000,"
                + " linear-gradient(#7ebcea, #2f4b8f),"
                + " linear-gradient(#426ab7, #263e75),"
                + " linear-gradient(#395cab, #223768);"
                + " -fx-background-insets: 0,1,2,3;"
                + " -fx-background-radius: 3,2,2,2;"
                + " -fx-padding: 3,4,3,4;"
                + " -fx-text-fill: white;"
                + " -fx-font-size: 15px;"
                + " -fx-font-family: \"Arial\";"
                + " -fx-font-weight: bold;");

        cardPanel = new StackPane();
        cardPanel.getChildren().add(new Rectangle(242, 636, Color.WHITE));

        gameButtonsPanel = new VBox();
        gameButtonsPanel.setAlignment(Pos.TOP_CENTER);

        btnGameHistory = this.initButton(JourneyThroughEuropePropertyType.GAME_HISTORY_IMAGE_NAME);
        btnGameHistory.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToGameHistoryRequest(JourneyThroughEuropeUIState.VIEW_GAME_HISTORY_STATE);
            }
        });

        btnAboutPlay = this.initButton(JourneyThroughEuropePropertyType.ABOUT_IMAGE_NAME);
        VBox gameHistoryPanel = new VBox();
        gameHistoryPanel.setAlignment(Pos.CENTER);

        gameHistoryPanel.setPadding(new Insets(100, 0, 0, 30));
        gameHistoryPanel.setSpacing(10.0);
        gameHistoryPanel.getChildren().add(btnGameHistory);
        gameHistoryPanel.getChildren().add(btnAboutPlay);

        VBox gridButtonsFirstColumnBox = new VBox();
        gridButtonsFirstColumnBox.setAlignment(Pos.CENTER);
        Label lblAC = initLabel(JourneyThroughEuropePropertyType.AC_IMAGE_NAME);
        lblAC.setPadding(new Insets(0, 0, 5, 0));
        gridButtonsFirstColumnBox.getChildren().add(lblAC);

        VBox gridButtonsSecondColumnBox = new VBox();
        gridButtonsSecondColumnBox.setAlignment(Pos.CENTER);
        Label lblDF = initLabel(JourneyThroughEuropePropertyType.DF_IMAGE_NAME);
        lblDF.setPadding(new Insets(0, 0, 5, 0));
        gridButtonsSecondColumnBox.getChildren().add(lblDF);

        VBox mapNumberPanel = new VBox();
        gridButtonsSecondColumnBox.setAlignment(Pos.CENTER);
        mapNumberPanel.setPadding(new Insets(60, 5, 0, 0));
        mapNumberPanel.setSpacing(30.0);
        Label lblOneThroughFour = initLabel(JourneyThroughEuropePropertyType.F4_IMAGE_NAME);
        Label lblFiveThroughEight = initLabel(JourneyThroughEuropePropertyType.F8_IMAGE_NAME);
        lblFiveThroughEight.setAlignment(Pos.CENTER);
        lblOneThroughFour.setAlignment(Pos.CENTER);

        mapNumberPanel.getChildren().addAll(lblOneThroughFour, lblFiveThroughEight);

        gridButtons = new Button[4];
        gridButtons[0] = initButton(JourneyThroughEuropePropertyType.GRID1_BUTTON_IMAGE_NAME);
        gridButtons[0].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToChangeGridRequest(JourneyThroughEuropeUIState.GRID1_IMAGE_STATE);
            }
        });

        gridButtons[1] = initButton(JourneyThroughEuropePropertyType.GRID2_BUTTON_IMAGE_NAME);
        gridButtons[1].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToChangeGridRequest(JourneyThroughEuropeUIState.GRID2_IMAGE_STATE);
            }
        });

        gridButtons[2] = initButton(JourneyThroughEuropePropertyType.GRID3_BUTTON_IMAGE_NAME);
        gridButtons[2].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToChangeGridRequest(JourneyThroughEuropeUIState.GRID3_IMAGE_STATE);
            }
        });

        gridButtons[3] = initButton(JourneyThroughEuropePropertyType.GRID4_BUTTON_IMAGE_NAME);
        gridButtons[3].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                eventHandler.respondToChangeGridRequest(JourneyThroughEuropeUIState.GRID4_IMAGE_STATE);
            }
        });

        gridButtonsFirstColumnBox.getChildren().addAll(gridButtons[0], gridButtons[2]);
        gridButtonsSecondColumnBox.getChildren().addAll(gridButtons[1], gridButtons[3]);

        diePanel = new VBox();
        diePanel.setAlignment(Pos.CENTER);
        diePanel.setPadding(new Insets(30, 0, 50, 30));

        btnDie = this.initButton(JourneyThroughEuropePropertyType.DEFAULT_DIE_IMAGE_NAME);
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
        gridButtonsContainer.getChildren().add(mapNumberPanel);
        gridButtonsContainer.getChildren().add(gridButtonsFirstColumnBox);
        gridButtonsContainer.getChildren().add(gridButtonsSecondColumnBox);

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

        gameScreenContainer.setCenter(gamePanel);
        gameScreenContainer.setLeft(leftPanel);
        gameScreenContainer.setRight(rightPanel);
        workspace.getChildren().add(gameScreenContainer);

    }

    private void initGameHistoryScreen() {

        gameHistoryToolbar = new HBox();
        btnGame = initButton(JourneyThroughEuropePropertyType.GAME_IMG_NAME);
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

        // LOAD THE STARTING STATS PAGE, WHICH IS JUST AN OUTLINE
        // AND DOESN"T HAVE ANY OF THE STATS, SINCE THOSE WILL 
        // BE DYNAMICALLY ADDED
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
                break;
            case PLAY_GAME_STATE:
                gameScreenContainer.setVisible(true);
                mainPane.setStyle("-fx-background-color:cb0d11");
                break;
            case VIEW_GAME_HISTORY_STATE:
                gameHistoryScreenContainer.setVisible(true);
                mainPane.setStyle("-fx-background-color:white");
                break;
            case VIEW_ABOUT_STATE:
                //helpScreenContainer.setVisible(true);
                break;
            case GAME_SETUP_STATE:
                gameSetupScreenContainer.setVisible(true);
                mainPane.setStyle("-fx-background-color:brown");
                break;
            default:
        }
        mainPane.setCenter(workspace);
    }

    public void changeGridImage(JourneyThroughEuropeUIState gridImageState) {
        switch (gridImageState) {
            case GRID1_IMAGE_STATE:
                gamePanel.getChildren().clear();
                gamePanel.getChildren().add(gameGridImageLabels[0]);
                gamePanel.getChildren().add(canvas);
                break;
            case GRID2_IMAGE_STATE:
                gamePanel.getChildren().clear();
                gamePanel.getChildren().add(gameGridImageLabels[1]);
                gamePanel.getChildren().add(canvas);
                break;
            case GRID3_IMAGE_STATE:
                gamePanel.getChildren().clear();
                gamePanel.getChildren().add(gameGridImageLabels[2]);
                gamePanel.getChildren().add(canvas);
                break;
            case GRID4_IMAGE_STATE:
                gamePanel.getChildren().clear();
                gamePanel.getChildren().add(gameGridImageLabels[3]);
                gamePanel.getChildren().add(canvas);
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

    public ImageView setupImageView(JourneyThroughEuropePropertyType prop)
    {
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

    public void disableAllPanes() {
        splashScreenContainer.setVisible(false);
        gameSetupScreenContainer.setVisible(false);
        gameScreenContainer.setVisible(false);
        gameHistoryScreenContainer.setVisible(false);
        //helpScreenContainer.setVisible(false);
    }

    public HBox setupPlayerGridPane() {
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

        playerPane.getChildren().add(playerTypePane);
        playerPane.getChildren().add(playerNamePane);

        return playerPane;
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

    public void setGameToScreen(GameRenderer gameManager) {
        canvas.getChildren().add(gameManager);
    }

    public void testClick() {
        System.out.println("Width: " + gamePanel.getWidth() + "Height: " + gamePanel.getHeight());
        gameManager = new GameRenderer(gamePanel.getWidth(), gamePanel.getHeight());
        mouseHandler = new JourneyThroughEuropeMouseHandler(gameManager, primaryStage);
        setGameToScreen(gameManager);
        canvas.setOnMouseClicked(mouseHandler);
        canvas.setOnMouseDragged(mouseHandler);
    }

}

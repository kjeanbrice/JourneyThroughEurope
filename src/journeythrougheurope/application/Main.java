package journeythrougheurope.application;

import properties_manager.PropertiesManager;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import journeythrougheurope.ui.JourneyThroughEuropeUI;

public class Main extends Application {

    static String PROPERTY_TYPES_LIST = "property_types.txt";
    static String UI_PROPERTIES_FILE_NAME = "properties.xml";
    static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";
    static String DATA_PATH = "./data/";

    @Override
    public void start(Stage primaryStage) {
        try {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(JourneyThroughEuropePropertyType.UI_PROPERTIES_FILE_NAME,
                    UI_PROPERTIES_FILE_NAME);
            props.addProperty(JourneyThroughEuropePropertyType.PROPERTIES_SCHEMA_FILE_NAME,
                    PROPERTIES_SCHEMA_FILE_NAME);
            props.addProperty(JourneyThroughEuropePropertyType.DATA_PATH.toString(),
                    DATA_PATH);
            props.loadProperties(UI_PROPERTIES_FILE_NAME,
                    PROPERTIES_SCHEMA_FILE_NAME);

            // GET THE LOADED TITLE AND SET IT IN THE FRAME
            String title = props.getProperty(JourneyThroughEuropePropertyType.SPLASH_SCREEN_TITLE_TEXT);
            primaryStage.setTitle(title);

            JourneyThroughEuropeUI root = new JourneyThroughEuropeUI();
            root.initJourneyThroughEuropeGame();
            BorderPane mainPane = root.getMainPane();
            root.setStage(primaryStage);
           

            Scene scene = new Scene(mainPane, mainPane.getWidth(), mainPane.getHeight());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public enum JourneyThroughEuropePropertyType {
        /* SETUP FILE NAMES */

        UI_PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME,
        /* DIRECTORIES FOR FILE LOADING */
        DATA_PATH, IMG_PATH,
        /* WINDOW DIMENSIONS */
        WINDOW_WIDTH, WINDOW_HEIGHT,
        /* LEVEL OPTIONS PROPERTIES */
        LEVEL_OPTIONS, LEVEL_FILES, 
        /* GAME TEXT */
        SPLASH_SCREEN_TITLE_TEXT, GAME_TITLE_TEXT, GAME_SUBHEADER_TEXT, WIN_DISPLAY_TEXT, LOSE_DISPLAY_TEXT, GAME_RESULTS_TEXT, GUESS_LABEL, LETTER_OPTIONS, EXIT_REQUEST_TEXT, YES_TEXT, NO_TEXT, DEFAULT_YES_TEXT, DEFAULT_NO_TEXT, DEFAULT_EXIT_TEXT,
        /* IMAGE FILE NAMES */
        WINDOW_ICON, SPLASH_SCREEN_IMAGE_NAME, GAME_IMG_NAME,GAME_GRID1_IMAGE_NAME,GAME_GRID2_IMAGE_NAME, GAME_GRID3_IMAGE_NAME, GAME_GRID4_IMAGE_NAME,GAME_HISTORY_IMG_NAME, HELP_IMG_NAME, EXIT_IMG_NAME, NEW_GAME_IMG_NAME, HOME_IMG_NAME,GRID1_BUTTON_IMAGE_NAME,GRID2_BUTTON_IMAGE_NAME,GRID3_BUTTON_IMAGE_NAME,GRID4_BUTTON_IMAGE_NAME,AC_IMAGE_NAME,
        DF_IMAGE_NAME,F4_IMAGE_NAME,F8_IMAGE_NAME,DIE1_IMAGE_NAME,DIE2_IMAGE_NAME,DIE3_IMAGE_NAME,DIE4_IMAGE_NAME,DIE5_IMAGE_NAME,DIE6_IMAGE_NAME,DEFAULT_DIE_IMAGE_NAME,ROLL_DIE_IMAGE_NAME,GAME_HISTORY_IMAGE_NAME,ABOUT_IMAGE_NAME,START_IMAGE_NAME,ABOUT_SPLASH_IMG_NAME,
        LOAD_IMG_NAME, BACK_IMG_NAME,
                
        /* DATA FILE STUFF */
        GAME_FILE_NAME, GAME_HISTORY_FILE_NAME, ABOUT_FILE_NAME,
        /* TOOLTIPS */
        GAME_TOOLTIP, STATS_TOOLTIP, HELP_TOOLTIP, EXIT_TOOLTIP, NEW_GAME_TOOLTIP, HOME_TOOLTIP,
        /*
         * THESE ARE FOR LANGUAGE-DEPENDENT ERROR HANDLING, LIKE FOR TEXT PUT
         * INTO DIALOG BOXES TO NOTIFY THE USER WHEN AN ERROR HAS OCCURED
         */
        ERROR_DIALOG_TITLE_TEXT, DUPLICATE_WORD_ERROR_TEXT, IMAGE_LOADING_ERROR_TEXT, INVALID_URL_ERROR_TEXT, INVALID_DOC_ERROR_TEXT, INVALID_XML_FILE_ERROR_TEXT, INVALID_GUESS_LENGTH_ERROR_TEXT, WORD_NOT_IN_DICTIONARY_ERROR_TEXT, INVALID_DICTIONARY_ERROR_TEXT,
        INSETS
    }
}

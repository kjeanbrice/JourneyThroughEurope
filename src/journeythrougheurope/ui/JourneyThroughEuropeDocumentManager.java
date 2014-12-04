/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.ui;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import journeythrougheurope.application.Main;
import journeythrougheurope.application.Main.JourneyThroughEuropePropertyType;
import journeythrougheurope.game.JourneyThroughEuropeGameStateManager;
import properties_manager.PropertiesManager;

/**
 *
 * @author Karl
 */
public class JourneyThroughEuropeDocumentManager {

    private JourneyThroughEuropeUI ui;
    // THESE ARE THE DOCUMENTS WE'LL BE UPDATING HERE
    private HTMLDocument statsDoc;

    // WE'LL USE THESE TO BUILD OUR HTML
    private final String START_TAG = "<";
    private final String END_TAG = ">";
    private final String SLASH = "/";
    private final String SPACE = " ";
    private final String EMPTY_TEXT = "";
    private final String NL = "\n";
    private final String QUOTE = "\"";
    private final String OPEN_PAREN = "(";
    private final String CLOSE_PAREN = ")";
    private final String COLON = ":";
    private final String EQUAL = "=";
    private final String COMMA = ",";
    private final String RGB = "rgb";

    // THESE ARE IDs IN THE GAME DISPLAY HTML FILE SO THAT WE
    // CAN GRAB THE NECESSARY ELEMENTS AND UPDATE THEM
    private final String GUESSES_SUBHEADER_ID = "guesses_subheader";
    private final String GUESSES_LIST_ID = "guesses_list";
    private final String WIN_DISPLAY_ID = "win_display";
    private final String LOSE_DISPLAY_ID = "lose_display";

    // THESE ARE IDs IN THE STATS HTML FILE SO THAT WE CAN
    // GRAB THE NECESSARY ELEMENTS AND UPDATE THEM
    private final String GAMES_PLAYED_ID = "games_played";
    private final String WINS_ID = "wins";
    private final String LOSSES_ID = "losses";
    private final String FEWEST_GUESSES_ID = "fewest_guesses";
    private final String FASTEST_WIN_ID = "fastest_win";
    private final String GAME_RESULTS_HEADER_ID = "game_results_header";
    private final String GAME_RESULTS_LIST_ID = "game_results_list";
    private final String PLAYER1_ID = "player_1";
    private final String PLAYER2_ID = "player_2";
    private final String PLAYER3_ID = "player_3";
    private final String PLAYER4_ID = "player_4";
    private final String PLAYER5_ID = "player_5";
    private final String PLAYER6_ID = "player_6";

    public JourneyThroughEuropeDocumentManager(JourneyThroughEuropeUI ui) {
        this.ui = ui;
    }

    public void setStatsDoc(HTMLDocument initStatsDoc) {
        statsDoc = initStatsDoc;
    }

    public void addGameResultToStatsPage(ArrayList<PlayerManager> players, int player) {
        // GET THE GAME STATS
        JourneyThroughEuropeGameStateManager gsm = ui.getGSM();

        try {

            // ADD THE SUBHEADER
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String gameResultsText = props.getProperty(JourneyThroughEuropePropertyType.GAME_RESULTS_TEXT);
            Element h2 = statsDoc.getElement(GAME_RESULTS_HEADER_ID);
            statsDoc.setInnerHTML(h2, gameResultsText);

            // AND NOW ADD THE LATEST GAME TO THE LIST
            Element ol = statsDoc.getElement(GAME_RESULTS_LIST_ID);
            Element li = null;
            switch (player) {
                case 0:
                    li = statsDoc.getElement(PLAYER1_ID);
                    break;
                case 1:
                    li = statsDoc.getElement(PLAYER2_ID);
                    break;
                case 2:
                    li = statsDoc.getElement(PLAYER3_ID);
                    break;
                case 3:
                    li = statsDoc.getElement(PLAYER4_ID);
                    break;
                case 4:
                    li = statsDoc.getElement(PLAYER5_ID);
                    break;
                case 5:
                    li = statsDoc.getElement(PLAYER6_ID);
                    break;

            }
            statsDoc.setInnerHTML(li, players.get(player).printGameHistory());
            /*String htmlText = "";
            for (int i = 0; i < players.size(); i++) {
                htmlText += START_TAG + HTML.Tag.LI + END_TAG + players.get(i).printGameHistory() + START_TAG + SLASH + HTML.Tag.LI + END_TAG + NL;
            }
            statsDoc.setInnerHTML(ol, htmlText);*/
        } // WE'LL LET THE ERROR HANDLER TAKE CARE OF ANY ERRORS,
        // WHICH COULD HAPPEN IF XML SETUP FILES ARE IMPROPERLY
        // FORMATTED
        catch (BadLocationException | IOException e) {
            JourneyThroughEuropeErrorHandler errorHandler = ui.getErrorHandler();
            errorHandler.processError(JourneyThroughEuropePropertyType.INVALID_DOC_ERROR_TEXT);
        }
    }

}

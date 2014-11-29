/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.thread;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Circle;
import journeythrougheurope.botalgorithm.Dijkstra;
import journeythrougheurope.botalgorithm.Vertex;
import journeythrougheurope.file.JourneyThroughEuropeFileLoader;
import journeythrougheurope.game.JourneyThroughEuropeCity;
import journeythrougheurope.ui.JourneyThroughEuropeUI;
import journeythrougheurope.ui.PlayerManager;

/**
 *
 * @author Karl
 */
public class GameManager {

    private final String REYKJAVIK = "REYKJAVIK";
    private final String FAROER = "FAROER";
    private final String BERLIN = "BERLIN";
    private final String DUBLIN = "DUBLIN";

    private final double CONVERSION_FACTOR = .60;
    private final int SIDE_LENGTH = (int) (20 * CONVERSION_FACTOR);
    private final int SCROLL_STEPS = 100;
    private final int MOVE_STEPS = 50;

    private PlayerManager currentPlayer;
    private ArrayList<PlayerManager> players;
    private Point2D startLocation;
    private JourneyThroughEuropeUI ui;
    private JourneyThroughEuropeCity destination;
    private String previousCity;

    private ScrollPane gameScrollPane;
    private double hValueX;
    private double vValueY;
    private double gridWidth;
    private double gridHeight;

    private boolean moveInProgress;
    private boolean scrolling;

    public GameManager(PlayerManager currentPlayer, ArrayList<PlayerManager> players, JourneyThroughEuropeUI ui) {
        this.ui = ui;
        this.currentPlayer = currentPlayer;
        destination = null;
        moveInProgress = false;
        scrolling = true;
        startLocation = null;
        previousCity = "";

        hValueX = 0;
        vValueY = 0;
        gameScrollPane = this.ui.getGameScrollPane();
        gridWidth = this.ui.getGameGridImages()[currentPlayer.getCurrentGridLocation() - 1].getImage().getWidth();
        gridHeight = this.ui.getGameGridImages()[currentPlayer.getCurrentGridLocation() - 1].getImage().getHeight();
        this.players = players;
    }

    public synchronized void scrollBack() {

        double destinationX = currentPlayer.getCurrentPosition().getX();
        double destinationY = currentPlayer.getCurrentPosition().getY();

        double xScrollOffset = (destinationX - hValueX);
        double YScrollOffset = (destinationY - vValueY);

        Rectangle2D currentScrollLocation = new Rectangle2D(gameScrollPane.getHvalue() * gridWidth, gameScrollPane.getVvalue() * gridHeight, 15, 15);
        Rectangle2D destinationRectLocation = new Rectangle2D(destinationX, destinationY, 30, 30);
        if (currentScrollLocation.intersects(destinationRectLocation)) {
            gameScrollPane.setHvalue((currentPlayer.getCurrentPosition().getX() / gridWidth));
            gameScrollPane.setVvalue((currentPlayer.getCurrentPosition().getY() / gridHeight));
            scrolling = false;
            hValueX = 0;
            vValueY = 0;
        } else {
            gameScrollPane.setHvalue((gameScrollPane.getHvalue() + ((xScrollOffset / gridWidth) / SCROLL_STEPS)));
            gameScrollPane.setVvalue((gameScrollPane.getVvalue() + ((YScrollOffset / gridHeight) / SCROLL_STEPS)));
        }
    }

    public synchronized boolean move() {
        if (moveInProgress) {
            float xOffset = (float) ((destination.getGridX() - startLocation.getX()));
            float yOffset = (float) ((destination.getGridY() - startLocation.getY()));

            Rectangle2D currentRectLocation = new Rectangle2D(currentPlayer.getCurrentPosition().getX(), currentPlayer.getCurrentPosition().getY(), 5, 5);
            Rectangle2D destinationRectLocation = new Rectangle2D(destination.getGridX(), destination.getGridY(), 5, 5);
            if (currentRectLocation.intersects(destinationRectLocation)) {

                previousCity = currentPlayer.getCurrentCity();
                if (previousCity.equalsIgnoreCase(REYKJAVIK) || previousCity.equalsIgnoreCase(FAROER) || previousCity.equalsIgnoreCase(BERLIN)
                        || previousCity.equalsIgnoreCase(DUBLIN)) {
                    previousCity = "";
                }

                currentPlayer.setCurrentCity(destination.getCityName());
                currentPlayer.setCurrentPosition(destination.getPoint());
                System.out.println("Game Manager: " + currentPlayer.getCurrentCity());

                ui.getGameScrollPane().setHvalue((destination.getPoint().getX() / gridWidth));
                ui.getGameScrollPane().setVvalue((destination.getPoint().getY() / gridHeight));

                currentPlayer.addToMoveHistory(destination.getCityName());
                ui.getDocumentManager().addGameResultToStatsPage(players);
                //JourneyThroughEuropeFileLoader.saveFile(players);
               //JourneyThroughEuropeFileLoader.loadFile();
                destination = null;
                moveInProgress = false;
                
                return true;
            } else {
                Point2D currentLocation = currentPlayer.getCurrentPosition();
                currentPlayer.setCurrentPosition(currentLocation.add(xOffset / MOVE_STEPS, yOffset / MOVE_STEPS));

                double x = currentLocation.getX();
                double y = currentLocation.getY();

                ui.getGameScrollPane().setHvalue((currentLocation.getX() / gridWidth));
                ui.getGameScrollPane().setVvalue((currentLocation.getY() / gridHeight));
            }

        }

        return false;
    }

    public boolean isHumanMoveValid(double xPosition, double yPosition) {

        if (moveInProgress) {
            return false;
        } else {
            JourneyThroughEuropeCity playerCity = ui.getGSM().processGetCityRequest(currentPlayer.getCurrentCity());
            Rectangle2D clickedPosition = new Rectangle2D(xPosition - (SIDE_LENGTH / 2), yPosition - (SIDE_LENGTH / 2), SIDE_LENGTH, SIDE_LENGTH);

            ArrayList<String> neighboringLandCities = playerCity.getNeighboringLandCities();
            for (int i = 0; i < neighboringLandCities.size(); i++) {

                JourneyThroughEuropeCity temp = ui.getGSM().processGetCityRequest(neighboringLandCities.get(i));
                Rectangle2D cityPosition = new Rectangle2D(temp.getGridX() - (SIDE_LENGTH / 2), temp.getGridY() - (SIDE_LENGTH / 2), SIDE_LENGTH, SIDE_LENGTH);
                if (clickedPosition.intersects(cityPosition)) {
                    if (!temp.getCityName().equalsIgnoreCase(previousCity)) {
                        for (int j = 0; j < players.size(); j++) {
                            if (players.get(j).getCurrentCity().equalsIgnoreCase(temp.getCityName())) {
                                return false;
                            }

                        }

                        destination = temp;
                        moveInProgress = true;
                        startLocation = currentPlayer.getCurrentPosition();
                        scrolling = true;
                        hValueX = gameScrollPane.getHvalue() * gridWidth;
                        vValueY = gameScrollPane.getVvalue() * gridHeight;
                        return true;
                    }
                }
            }
            ArrayList<String> neighboringSeaCities = playerCity.getNeighboringSeaCities();
            for (int i = 0; i < neighboringSeaCities.size(); i++) {
                JourneyThroughEuropeCity temp = ui.getGSM().processGetCityRequest(neighboringSeaCities.get(i));
                Rectangle2D cityPosition = new Rectangle2D(temp.getGridX() - (SIDE_LENGTH / 2), temp.getGridY() - (SIDE_LENGTH / 2), SIDE_LENGTH, SIDE_LENGTH);
                if (clickedPosition.intersects(cityPosition)) {
                    if (!temp.getCityName().equalsIgnoreCase(previousCity)) {
                        for (int j = 0; j < players.size(); j++) {
                            if (players.get(j).getCurrentCity().equalsIgnoreCase(temp.getCityName())) {
                                return false;
                            }
                        }

                        destination = temp;
                        moveInProgress = true;
                        startLocation = currentPlayer.getCurrentPosition();
                        scrolling = true;
                        hValueX = gameScrollPane.getHvalue() * gridWidth;
                        vValueY = gameScrollPane.getVvalue() * gridHeight;
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public boolean isBotMoveValid() {
        if (moveInProgress) {
            return false;
        } else {
            if (currentPlayer.getCards().isEmpty()) {
                return false;
            } else {
                List<Vertex> path;
                JourneyThroughEuropeCity playerCity = ui.getGSM().processGetCityRequest(currentPlayer.getCurrentCity());
                Dijkstra.computePaths(playerCity.getVertex());
                if (currentPlayer.getCards().size() == 1) {
                    path = Dijkstra.getShortestPathTo(ui.getGSM().processGetCityRequest(currentPlayer.getCards().get(0)).getVertex());
                } else {
                    path = Dijkstra.getShortestPathTo(ui.getGSM().processGetCityRequest(currentPlayer.getCards().get(1)).getVertex());
                    for (int k = 1; k < currentPlayer.getCards().size(); k++) {
                        List<Vertex> temp = Dijkstra.getShortestPathTo(ui.getGSM().processGetCityRequest(currentPlayer.getCards().get(k)).getVertex());
                        //Fix this area - TIRANE
                        if (temp.size() < path.size() || path.get(0).toString().equalsIgnoreCase("TIRANE")) {
                            path = temp;
                        }
                    }
                }

                if(path.size() == 1)
                    return false;
                
                destination = ui.getGSM().processGetCityRequest(path.get(1).toString());
                moveInProgress = true;
                startLocation = currentPlayer.getCurrentPosition();
                scrolling = true;
                hValueX = gameScrollPane.getHvalue() * gridWidth;
                vValueY = gameScrollPane.getVvalue() * gridHeight;
                ui.getGSM().resetVertex();
                return true;

            }
        }
    }

    public boolean isMoveInProgress() {
        return moveInProgress;
    }

    public void setMoveInProgress(boolean status) {
        moveInProgress = status;
    }

    public PlayerManager getPlayerManager() {
        return currentPlayer;
    }

    public boolean isScrolling() {
        return scrolling;
    }

    public void resetPreviousCity() {
        previousCity = "";
    }

}

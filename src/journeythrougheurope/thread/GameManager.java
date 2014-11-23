/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.thread;

import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Circle;
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
   
    private final double CONVERSION_FACTOR = .60;
    private final int SIDE_LENGTH = (int) (20 * CONVERSION_FACTOR);
    private final int STEPS = 70;

    private PlayerManager player;
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

    public GameManager(PlayerManager player, JourneyThroughEuropeUI ui) {
        this.ui = ui;
        this.player = player;
        destination = null;
        moveInProgress = false;
        scrolling = true;
        startLocation = null;
        previousCity = "";

        hValueX = 0;
        vValueY = 0;
        gameScrollPane = this.ui.getGameScrollPane();
        gridWidth = this.ui.getGameGridImages()[player.getCurrentGridLocation() - 1].getImage().getWidth();
        gridHeight = this.ui.getGameGridImages()[player.getCurrentGridLocation() - 1].getImage().getHeight();

    }

    public synchronized void scrollBack() {

        double destinationX = player.getCurrentPosition().getX();
        double destinationY = player.getCurrentPosition().getY();

        double xScrollOffset = (destinationX - hValueX);
        double YScrollOffset = (destinationY - vValueY);

        Rectangle2D currentScrollLocation = new Rectangle2D(gameScrollPane.getHvalue() * gridWidth, gameScrollPane.getVvalue() * gridHeight, 10, 10);
        Rectangle2D destinationRectLocation = new Rectangle2D(destinationX, destinationY, 5, 5);
        if (currentScrollLocation.intersects(destinationRectLocation)) {
            gameScrollPane.setHvalue((destinationX / gridWidth));
            gameScrollPane.setVvalue((destinationY / gridHeight));
            scrolling = false;
            hValueX = 0;
            vValueY = 0;
        } else {
            gameScrollPane.setHvalue((gameScrollPane.getHvalue() + ((xScrollOffset / gridWidth) / STEPS)));
            gameScrollPane.setVvalue((gameScrollPane.getVvalue() + ((YScrollOffset / gridHeight) / STEPS)));
        }
    }

    public synchronized boolean move() {
        if (moveInProgress) {
            float xOffset = (float) ((destination.getGridX() - startLocation.getX()));
            float yOffset = (float) ((destination.getGridY() - startLocation.getY()));

            Rectangle2D currentRectLocation = new Rectangle2D(player.getCurrentPosition().getX(), player.getCurrentPosition().getY(), 5, 5);
            Rectangle2D destinationRectLocation = new Rectangle2D(destination.getGridX(), destination.getGridY(), 5, 5);
            if (currentRectLocation.intersects(destinationRectLocation)) {
               
                previousCity = player.getCurrentCity();
                if(previousCity.equalsIgnoreCase(REYKJAVIK) || previousCity.equalsIgnoreCase(FAROER))
                    previousCity = "";
                
                player.setCurrentCity(destination.getCityName());
                player.setCurrentPosition(destination.getPoint());

                double x = destination.getPoint().getX();
                double gridWidth = ui.getGameGridImages()[player.getCurrentGridLocation() - 1].getImage().getWidth();

                double y = destination.getPoint().getY();
                double gridHeight = ui.getGameGridImages()[player.getCurrentGridLocation() - 1].getImage().getHeight();

                ui.getGameScrollPane().setHvalue((x / gridWidth));
                ui.getGameScrollPane().setVvalue((y / gridHeight));

                destination = null;
                moveInProgress = false;
                return true;

                // System.out.println("Game Manager: " + player.toString() + "\n");
            } else {

                //System.out.println("Game Manager: " + player.toString() + "\n");
                Point2D currentLocation = player.getCurrentPosition();
                player.setCurrentPosition(currentLocation.add(xOffset / STEPS, yOffset / STEPS));

                double x = currentLocation.getX();
                double y = currentLocation.getY();

                ui.getGameScrollPane().setHvalue((x / gridWidth));
                ui.getGameScrollPane().setVvalue((y / gridHeight));
            }

        }

        return false;
    }

    public synchronized boolean isMoveValid(double xPosition, double yPosition) {
        scrollBack();
        if (moveInProgress) {
            return false;
        } else {
            JourneyThroughEuropeCity playerCity = ui.getGSM().processGetCityRequest(player.getCurrentCity());
            Rectangle2D clickedPosition = new Rectangle2D(xPosition - (SIDE_LENGTH / 2), yPosition - (SIDE_LENGTH / 2), SIDE_LENGTH, SIDE_LENGTH);

            ArrayList<String> neighboringLandCities = playerCity.getNeighboringLandCities();
            for (int i = 0; i < neighboringLandCities.size(); i++) {

                JourneyThroughEuropeCity temp = ui.getGSM().processGetCityRequest(neighboringLandCities.get(i));
                Rectangle2D cityPosition = new Rectangle2D(temp.getGridX() - (SIDE_LENGTH / 2), temp.getGridY() - (SIDE_LENGTH / 2), SIDE_LENGTH, SIDE_LENGTH);
                if (clickedPosition.intersects(cityPosition)) {
                    if (!temp.getCityName().equalsIgnoreCase(previousCity)) {
                        destination = temp;
                        moveInProgress = true;
                        startLocation = player.getCurrentPosition();

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
                        destination = temp;
                        moveInProgress = true;

                        startLocation = player.getCurrentPosition();

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

    public boolean isMoveInProgress() {
        return moveInProgress;
    }

    public void setMoveInProgress(boolean status) {
        moveInProgress = status;
    }

    public PlayerManager getPlayerManager() {
        return player;
    }

    public boolean isScrolling() {
        return scrolling;
    }

    public double calculateY(double x, Point2D currentPosition, Point2D destinationPosition) {
        double dx = destinationPosition.getX() - currentPosition.getX();
        double dy = destinationPosition.getY() - currentPosition.getY();

        double slope = dx / dy;
        double b = currentPosition.getY() - (dx / dy) * x;

        return (dx / dy) * x + b;
    }
    
    public void resetPreviousCity()
    {
        previousCity = "";
    }

}

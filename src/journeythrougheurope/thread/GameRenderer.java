/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.thread;

import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import journeythrougheurope.ui.JourneyThroughEuropeUI;

/**
 *
 * @author Karl
 */
public class GameRenderer extends Canvas {

    private static String IMAGE_PATH_GREEN = "file:images/cards/";
    private static String IMAGE_PATH_RED = "file:images/cards/";
    private static String IMAGE_PATH_YELLOW = "file:images/cards/";
    
    private static int IMAGE_SIZE = 35;
    private static int IMAGE_OFFSET = 17;

    private final double CONVERSION_FACTOR = .60;
    private final int SIDE_LENGTH = (int) (36 * CONVERSION_FACTOR);

    private JourneyThroughEuropeUI ui;
    private GraphicsContext gc;
    //private ArrayList<JourneyThroughEuropeCity> cityData;

    private GameManager[] gameManagers;
    private int currentPlayer;

    private double canvasWidth;
    private double canvasHeight;
    private boolean intersects;

    public GameRenderer(double canvasWidth, double canvasHeight, JourneyThroughEuropeUI ui, GameManager[] gameManagers) {
        this.ui = ui;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        setWidth(this.canvasWidth);
        setHeight(this.canvasHeight);
        intersects = false;
        this.gameManagers = gameManagers;
        currentPlayer = -1;
    }

    public void repaint() {
        gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        if (currentPlayer != -1) {
            ArrayList<String> neighboringLandCities = ui.getGSM().processGetCityRequest(gameManagers[currentPlayer].getPlayerManager().getCurrentCity()).getNeighboringLandCities();
            for (int i = 0; i < neighboringLandCities.size(); i++) {
                Point2D startLocation = ui.getGSM().processGetCityRequest(gameManagers[currentPlayer].getPlayerManager().getCurrentCity()).getPoint();
                Point2D endLocation = ui.getGSM().processGetCityRequest(neighboringLandCities.get(i)).getPoint();
                gc.setStroke(Color.RED);
                gc.setLineWidth(5);
                gc.strokeLine(startLocation.getX(), startLocation.getY(), endLocation.getX(), endLocation.getY());
            }

            ArrayList<String> neighboringSeaCities = ui.getGSM().processGetCityRequest(gameManagers[currentPlayer].getPlayerManager().getCurrentCity()).getNeighboringSeaCities();
            for (int i = 0; i < neighboringSeaCities.size(); i++) {
                Point2D startLocation = ui.getGSM().processGetCityRequest(gameManagers[currentPlayer].getPlayerManager().getCurrentCity()).getPoint();
                Point2D endLocation = ui.getGSM().processGetCityRequest(neighboringSeaCities.get(i)).getPoint();
                gc.setStroke(Color.RED);
                gc.setLineWidth(5);
                gc.strokeLine(startLocation.getX(), startLocation.getY(), endLocation.getX(), endLocation.getY());
            }

            ArrayList<String> cardHand = gameManagers[currentPlayer].getPlayerManager().getCards();
            for (int i = 0; i < cardHand.size(); i++) {
                Point2D cardLocation = ui.getGSM().processGetCityRequest(cardHand.get(i)).getPoint();
                gc.setFill(Color.CORAL);
                gc.fillOval(cardLocation.getX() - (SIDE_LENGTH / 2), cardLocation.getY() - (SIDE_LENGTH / 2), SIDE_LENGTH, SIDE_LENGTH);
            }
        }

        for (int i = 0; i < gameManagers.length; i++) {

            gc.setFill(Color.GREEN);
            gc.drawImage(gameManagers[i].getPlayerManager().getPlayerImage(),gameManagers[i].getPlayerManager().getCurrentPosition().getX() - IMAGE_OFFSET,gameManagers[i].getPlayerManager().getCurrentPosition().getY() - IMAGE_OFFSET);
            gc.drawImage(gameManagers[i].getPlayerManager().getHomeImage(), ui.getGSM().processGetCityRequest(gameManagers[i].getPlayerManager().getHomeCity()).getGridX() - 5, ui.getGSM().processGetCityRequest(gameManagers[i].getPlayerManager().getHomeCity()).getGridY() - 70);

        }

    }

    public void repaint(double x, double y) {
        gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        if (currentPlayer != -1) {
            ArrayList<String> neighboringLandCities = ui.getGSM().processGetCityRequest(gameManagers[currentPlayer].getPlayerManager().getCurrentCity()).getNeighboringLandCities();
            for (int i = 0; i < neighboringLandCities.size(); i++) {
                Point2D startLocation = ui.getGSM().processGetCityRequest(gameManagers[currentPlayer].getPlayerManager().getCurrentCity()).getPoint();
                Point2D endLocation = ui.getGSM().processGetCityRequest(neighboringLandCities.get(i)).getPoint();
                gc.setStroke(Color.RED);
                gc.setLineWidth(5);
                gc.strokeLine(startLocation.getX(), startLocation.getY(), endLocation.getX(), endLocation.getY());
            }

            ArrayList<String> neighboringSeaCities = ui.getGSM().processGetCityRequest(gameManagers[currentPlayer].getPlayerManager().getCurrentCity()).getNeighboringSeaCities();
            for (int i = 0; i < neighboringSeaCities.size(); i++) {
                Point2D startLocation = ui.getGSM().processGetCityRequest(gameManagers[currentPlayer].getPlayerManager().getCurrentCity()).getPoint();
                Point2D endLocation = ui.getGSM().processGetCityRequest(neighboringSeaCities.get(i)).getPoint();
                gc.setStroke(Color.RED);
                gc.setLineWidth(5);
                gc.strokeLine(startLocation.getX(), startLocation.getY(), endLocation.getX(), endLocation.getY());
            }

            ArrayList<String> cardHand = gameManagers[currentPlayer].getPlayerManager().getCards();
            for (int i = 0; i < cardHand.size(); i++) {
                Point2D cardLocation = ui.getGSM().processGetCityRequest(cardHand.get(i)).getPoint();
                gc.setFill(Color.CORAL);
                gc.fillOval(cardLocation.getX() - (SIDE_LENGTH / 2), cardLocation.getY() - (SIDE_LENGTH / 2), SIDE_LENGTH, SIDE_LENGTH);
            }

            gc.setFill(Color.GREEN);
            gc.drawImage(gameManagers[currentPlayer].getPlayerManager().getPlayerImage(),x - IMAGE_OFFSET,y - IMAGE_OFFSET);
           
        }

        for (int i = 0; i < gameManagers.length; i++) {

            
            if (i != currentPlayer) {
                gc.drawImage(gameManagers[i].getPlayerManager().getPlayerImage(),gameManagers[i].getPlayerManager().getCurrentPosition().getX()- IMAGE_OFFSET,gameManagers[i].getPlayerManager().getCurrentPosition().getY() - IMAGE_OFFSET);
            }
            gc.drawImage(gameManagers[i].getPlayerManager().getHomeImage(), ui.getGSM().processGetCityRequest(gameManagers[i].getPlayerManager().getHomeCity()).getGridX() - 5, ui.getGSM().processGetCityRequest(gameManagers[i].getPlayerManager().getHomeCity()).getGridY() - 70);

        }
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}

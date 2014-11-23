/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.thread;

import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import journeythrougheurope.game.JourneyThroughEuropeCity;
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
        //cityData = ui.getGSM().getCurrentGridData();
        intersects = false;
        this.gameManagers = gameManagers;
        currentPlayer = -1;
    }

    public void repaint() {
        gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        int currentGrid = ui.getCurrentGrid();

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
        //switch(u)
        /*
 for (int i = 0; i < cityData.size(); i++) {
 Rectangle2D temp = new Rectangle2D(x, y, SIDE_LENGTH, SIDE_LENGTH);

 int cityX = (int) cityData.get(i).getGridX();
 int cityY = (int) cityData.get(i).getGridY();

 if (temp.intersects(new Rectangle2D(cityX, cityY, SIDE_LENGTH, SIDE_LENGTH))) {
              
 switch (cityData.get(i).getCardColor().toUpperCase()) {
 case "RED":
 Image test = new Image(IMAGE_PATH_RED + cityData.get(i).getCityName());
                        
 gc.drawImage(new Image(IMAGE_PATH_RED + cityData.get(i).getCityName() + ".jpg"), x - SIDE_LENGTH, y-SIDE_LENGTH,200,300);
 break;
 case "GREEN":
 gc.drawImage(new Image(IMAGE_PATH_GREEN + cityData.get(i).getCityName() + ".jpg"), x - SIDE_LENGTH, y-SIDE_LENGTH,200,300);
 break;
 case "YELLOW":
 gc.drawImage(new Image(IMAGE_PATH_YELLOW + cityData.get(i).getCityName() + ".jpg"), x - SIDE_LENGTH, y-SIDE_LENGTH,200,300);
 System.out.println(cityData.get(i).getCityName());
 break;
 }
 gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
 gc.setFill(Color.GREEN);
 gc.fillText(cityData.get(i).getCityName(), x - SIDE_LENGTH, y - SIDE_LENGTH);
 gc.fillOval(x - (SIDE_LENGTH / 2), y - (SIDE_LENGTH / 2), SIDE_LENGTH, SIDE_LENGTH);
 gc.strokeOval(cityX - (SIDE_LENGTH / 2), cityY - (SIDE_LENGTH / 2), SIDE_LENGTH, SIDE_LENGTH);
 intersects = true;
 break;
 }
 }

 if (!intersects) {
 gc.setFill(Color.RED);
 gc.fillOval(x - (SIDE_LENGTH / 2), y - (SIDE_LENGTH / 2), SIDE_LENGTH, SIDE_LENGTH);
 } else {
 intersects = false;
 }
 */


/*
 public void updateCityData() {
 cityData = ui.getGSM().getCurrentGridData();
 }*/

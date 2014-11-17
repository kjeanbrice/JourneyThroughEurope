/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.thread;

import java.util.ArrayList;
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

    private final double CONVERSION_FACTOR = .60;
    private final int SIDE_LENGTH = (int) (36 * CONVERSION_FACTOR);

    private JourneyThroughEuropeUI ui;
    private GraphicsContext gc;
    private ArrayList<JourneyThroughEuropeCity> cityData;
    private double canvasWidth;
    private double canvasHeight;
    private boolean intersects;

    public GameRenderer(double canvasWidth, double canvasHeight, JourneyThroughEuropeUI ui) {
        this.ui = ui;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        setWidth(this.canvasWidth);
        setHeight(this.canvasHeight);
        cityData = ui.getGSM().getCurrentGridData();
        intersects = false;
    }

    public void repaint(double x, double y) {
        gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

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
    }

    public void updateCityData() {
        cityData = ui.getGSM().getCurrentGridData();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.game;

import java.util.ArrayList;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import journeythrougheurope.ui.JourneyThroughEuropeUI;

/**
 *
 * @author Karl
 */
public class GameRenderer extends Canvas {

    private final int GRID_IMAGE_WIDTH = 2010;
    private final int GRID_IMAGE_HEIGHT = 2569;
    private final int GRID_IMAGE_WIDTH_LARGE = 2010;
    private final int GRID_IMAGE_HEIGHT_LARGE = 2569;
    private final int SIDE_LENGTH = 36;

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

            int point[] = this.pointConversion(cityData.get(i).getGridX(), cityData.get(i).getGridY());
            int cityX = point[0];
            int cityY = point[1];

            if (temp.intersects(new Rectangle2D(cityX, cityY, SIDE_LENGTH, SIDE_LENGTH))) {
                gc.setFont(Font.font("Arial", FontWeight.BOLD, 25));
                gc.setFill(Color.GREEN);
                gc.fillText(cityData.get(i).getCityName(), x - SIDE_LENGTH, y - SIDE_LENGTH);
                gc.fillOval(x - (SIDE_LENGTH / 2), y - (SIDE_LENGTH / 2), SIDE_LENGTH, SIDE_LENGTH);
                gc.strokeOval(cityX - (SIDE_LENGTH / 2) ,cityY - (SIDE_LENGTH / 2),SIDE_LENGTH,SIDE_LENGTH);
                intersects = true;
                break;
            }
        }
        
        if(!intersects)
        {
            gc.setFill(Color.RED);
            gc.fillOval(x - (SIDE_LENGTH / 2), y - (SIDE_LENGTH / 2), SIDE_LENGTH, SIDE_LENGTH);
        }
        else
            intersects = false;
    }

    public void updateCityData() {
        cityData = ui.getGSM().getCurrentGridData();
    }

    private int[] pointConversion(double x, double y) {
        int point[] = new int[2];
        point[0] = (int) ((x / GRID_IMAGE_WIDTH_LARGE) * GRID_IMAGE_WIDTH);
        point[1] = (int) ((y / GRID_IMAGE_HEIGHT_LARGE) * GRID_IMAGE_HEIGHT);

        return point;
    }
}

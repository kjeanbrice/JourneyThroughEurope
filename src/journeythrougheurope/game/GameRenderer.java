/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Karl
 */
public class GameRenderer extends Canvas {

    private GraphicsContext gc;
    private double canvasWidth;
    private double canvasHeight;

    public GameRenderer(double canvasWidth, double canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        setWidth(this.canvasWidth);
        setHeight(this.canvasHeight);

    }

    public void repaint(double x, double y) {
        gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        gc.strokeRect(x-(18/2), y-(18/2), 18,18);
    }
}

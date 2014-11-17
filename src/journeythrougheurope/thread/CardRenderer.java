/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.thread;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import journeythrougheurope.game.JourneyThroughEuropeCity;
import journeythrougheurope.ui.JourneyThroughEuropeUI;
import journeythrougheurope.ui.PlayerManager;

public class CardRenderer extends Canvas {

    private JourneyThroughEuropeUI ui;
    private GraphicsContext gc;
    private double canvasWidth;
    private double canvasHeight;
    private double DEFAULT_CARD_WIDTH;
    private final double DEFAULT_CARD_HEIGHT = 300;
    private static String IMAGE_PATH = "file:images/cards/";

    public CardRenderer(double canvasWidth, double canvasHeight, JourneyThroughEuropeUI ui) {
        this.ui = ui;
        DEFAULT_CARD_WIDTH = canvasWidth;
        this.setWidth(canvasWidth);
        this.setHeight(canvasHeight);

    }

    public void repaint(PlayerManager player, int numCardsToDeal) {
        gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < numCardsToDeal; i++) {
            gc.drawImage(new Image(IMAGE_PATH + player.getCards().get(i) + ".jpg"), player.getCardLocations().get(i).getX(),
                    player.getCardLocations().get(i).getY(), DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT);
            //gc.strokeRect(player.getCardLocations().get(i).getX(), player.getCardLocations().get(i).getY(), DEFAULT_CARD_WIDTH, 57.0);
        }
    }

    public void displayCard(PlayerManager player, int showCard) {
        gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < player.getCards().size(); i++) {
            if (i != showCard) {
                gc.drawImage(new Image(IMAGE_PATH + player.getCards().get(i) + ".jpg"), player.getCardLocations().get(i).getX(),
                        player.getCardLocations().get(i).getY(), DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT);
            }
        }

        gc.drawImage(new Image(IMAGE_PATH + player.getCards().get(showCard) + ".jpg"), player.getCardLocations().get(showCard).getX(),
                player.getCardLocations().get(showCard).getY(), DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT);
        gc.setStroke(Color.ROYALBLUE);
        gc.setLineWidth(5);
        gc.strokeRect(player.getCardLocations().get(showCard).getX(),
                player.getCardLocations().get(showCard).getY(), DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT);
    }

}

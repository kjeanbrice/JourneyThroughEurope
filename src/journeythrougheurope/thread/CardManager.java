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
import journeythrougheurope.ui.JourneyThroughEuropeUI;
import journeythrougheurope.ui.PlayerManager;

/**
 *
 * @author Karl
 */
public class CardManager {

    public final int STEPS = 100;
    private JourneyThroughEuropeUI ui;
    private ArrayList<Point2D> cardLocations;
    private PlayerManager player;
    private double canvasWidth;
    private double canvasHeight;
    private ScrollPane gameScrollPane;
    private double hValueX;
    private double vValueY;
    private double gridWidth;
    private double gridHeight;
    private boolean scrolling;

    public CardManager(double canvasWidth, double canvasHeight, JourneyThroughEuropeUI ui, PlayerManager player) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.ui = ui;
        this.player = player;
        cardLocations = this.player.getCardLocations();
        gameScrollPane = this.ui.getGameScrollPane();
        hValueX = 0;
        vValueY = 0;
        gridWidth = this.ui.getGameGridImages()[player.getCurrentGridLocation() - 1].getImage().getWidth();
        gridHeight = this.ui.getGameGridImages()[player.getCurrentGridLocation() - 1].getImage().getHeight();
        scrolling = false;

    }

    public void initDefaultCardLocations() {
        System.out.println("Card Manager - Max Cards: " + player.getCards().size());
        for (int i = 0; i < player.getCards().size(); i++) {
            cardLocations.add(new Point2D(0, canvasHeight));
        }
    }

    public boolean scrollToPlayerLocation() {
        if (scrolling) {
            double destinationX = player.getCurrentPosition().getX();
            double destinationY = player.getCurrentPosition().getY();

            double xScrollOffset = (destinationX - hValueX);
            double YScrollOffset = (destinationY - vValueY);

            Rectangle2D currentScrollLocation = new Rectangle2D(gameScrollPane.getHvalue() * gridWidth, gameScrollPane.getVvalue() * gridHeight, 1, 1);
            Rectangle2D destinationRectLocation = new Rectangle2D(destinationX, destinationY, 3, 3);
            if (currentScrollLocation.intersects(destinationRectLocation)) {
                gameScrollPane.setHvalue((destinationX / gridWidth));
                gameScrollPane.setVvalue((destinationY / gridHeight));
                scrolling = false;
                hValueX = 0;
                vValueY = 0;
                return false;

            } else {
                gameScrollPane.setHvalue((gameScrollPane.getHvalue() + ((xScrollOffset / gridWidth) / STEPS)));
                gameScrollPane.setVvalue((gameScrollPane.getVvalue() + ((YScrollOffset / gridHeight) / STEPS)));
                return true;
            }
        }
        return true;

    }

    public void moveCardUp(int cardIndex, double yOffset, double finalYPosition) {
        if (cardLocations.get(cardIndex).getY() > finalYPosition) {
            if ((cardLocations.get(cardIndex).getY() + yOffset) < finalYPosition) {
                cardLocations.set(cardIndex, new Point2D(0, finalYPosition));
            } else {
                cardLocations.set(cardIndex, cardLocations.get(cardIndex).add(new Point2D(0, yOffset)));
            }
        }
    }

    public void moveCardDown(int cardIndex, double yOffset, double finalYPosition) {
        if (cardLocations.get(cardIndex).getY() < finalYPosition) {
            if ((cardLocations.get(cardIndex).getY() + yOffset) > finalYPosition) {
                cardLocations.set(cardIndex, new Point2D(0, finalYPosition));
            } else {
                cardLocations.set(cardIndex, cardLocations.get(cardIndex).add(new Point2D(0, yOffset)));
            }
        }
    }

    public void resetCardLocations() {
        for (int i = 0; i < player.getCards().size(); i++) {
            cardLocations.set(i, new Point2D(0, canvasHeight));
        }
    }

    public PlayerManager getPlayerManager() {
        return player;
    }

    public void setCurrentGameScrollLocation(double hValue, double vValue) {
        this.hValueX = hValue * gridWidth;
        this.vValueY = vValue * gridHeight;
    }

    public void setScrolling(boolean scrolling) {
        this.scrolling = scrolling;
    }

    public boolean isScrolling() {
        return scrolling;
    }

}

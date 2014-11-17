/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.thread;

import java.util.ArrayList;
import javafx.geometry.Point2D;
import journeythrougheurope.ui.JourneyThroughEuropeUI;
import journeythrougheurope.ui.PlayerManager;

/**
 *
 * @author Karl
 */
public class CardManager {

    private JourneyThroughEuropeUI ui;
    private ArrayList<Point2D> cardLocations;
    private PlayerManager player;
    private double canvasWidth;
    private double canvasHeight;

    public CardManager(double canvasWidth, double canvasHeight, JourneyThroughEuropeUI ui, PlayerManager player) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.ui = ui;
        this.player = player;
        cardLocations = player.getCardLocations();
       
    }

    public void initDefaultCardLocations() {
        System.out.println(player.getCards().size());
        for (int i = 0; i < player.getCards().size(); i++) {
            cardLocations.add(new Point2D(0, canvasHeight));
        }
    }

    public void moveCard(int cardIndex, double yOffset, double finalYPosition) {
        if (cardLocations.get(cardIndex).getY() > finalYPosition) {
            if ((cardLocations.get(cardIndex).getY() + yOffset) < finalYPosition) {
                cardLocations.set(cardIndex, new Point2D(0, finalYPosition));         
            } else {
                cardLocations.set(cardIndex, cardLocations.get(cardIndex).add(new Point2D(0, yOffset)));
            }
        }

    }
    
    public PlayerManager getPlayerManager()
    {
        return player;
    }

}

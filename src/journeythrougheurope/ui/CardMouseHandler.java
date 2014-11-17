/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.ui;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import journeythrougheurope.thread.CardRenderer;
import journeythrougheurope.thread.CardThread;

/**
 *
 * @author Karl
 */
public class CardMouseHandler implements EventHandler<MouseEvent> {

    private boolean doubleClick;
    private PlayerManager player;
    private CardRenderer cardRenderer;
    private double DEFAULT_CARD_WIDTH;
    private int previousClick;

    public CardMouseHandler(PlayerManager player, CardRenderer cardRenderer, double canvasWidth) {
        DEFAULT_CARD_WIDTH = canvasWidth;
        this.player = player;
        this.cardRenderer = cardRenderer;
        doubleClick = false;
    }

    public void handle(MouseEvent event) {
        switch (event.getEventType().toString()) {
            case "MOUSE_CLICKED":
                if (event.getClickCount() == 2) {
                    for (int i = 0; i < player.getCardLocations().size(); i++) {
                        Rectangle2D cardLocation = new Rectangle2D(player.getCardLocations().get(i).getX(),
                                player.getCardLocations().get(i).getY(), DEFAULT_CARD_WIDTH, 57);
                        Point2D clickLocation = new Point2D(event.getX(),
                                event.getY());
                        if (cardLocation.contains(clickLocation)) {
                            if (!doubleClick) {
                                cardRenderer.displayCard(player, i);
                                doubleClick = true;
                            } else {
                                cardRenderer.repaint(player, player.getCards().size());
                                doubleClick = false;
                                       
                            }
                            break;
                        }

                    }
                }
                //gameManager.repaint(event.getX(),event.getY());

                break;
            case "MOUSE_RELEASED":
                break;
            case "MOUSE_DRAGGED":
                System.out.println("X: " + event.getX() + "     Y: " + event.getY());
                //gameManager.repaint(event.getX(), event.getY());
                break;
        }
    }

    public void setPlayer(PlayerManager player) {
        this.player = player;
    }
}

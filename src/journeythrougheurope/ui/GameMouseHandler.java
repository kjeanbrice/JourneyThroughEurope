/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.ui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import journeythrougheurope.thread.GameManager;
import journeythrougheurope.thread.GameRenderer;

/**
 *
 * @author Karl
 */
public class GameMouseHandler implements EventHandler<MouseEvent> {

    private GameManager currentGameManager;

    public GameMouseHandler() {
        currentGameManager = null;
    }

    public void handle(MouseEvent event) {

        if (currentGameManager != null) {
            switch (event.getEventType().toString()) {
                case "MOUSE_CLICKED":
                    System.out.println("X: " + event.getX() + "     Y: " + event.getY());
                    if (currentGameManager.getPlayerManager().getMovesRemaining() != 0) {
                        System.out.println(currentGameManager.isMoveValid(event.getX(), event.getY()));
                    }
                    break;
                case "MOUSE_RELEASED":
                    break;
                case "MOUSE_DRAGGED":
                    System.out.println("X: " + event.getX() + "     Y: " + event.getY());
                    //gameRenderer.repaint(event.getX(),event.getY());
                    break;
            }
        }

    }

    public void setGameManager(GameManager currentGameManager) {
        this.currentGameManager = currentGameManager;
    }
}

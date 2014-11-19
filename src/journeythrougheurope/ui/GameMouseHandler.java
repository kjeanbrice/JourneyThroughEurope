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
import journeythrougheurope.thread.GameRenderer;

/**
 *
 * @author Karl
 */
public class GameMouseHandler  implements EventHandler<MouseEvent> {

    private GameRenderer gameRenderer;
    
    public GameMouseHandler(GameRenderer gameManager) 
    {
         this.gameRenderer = gameManager;
    }


    public void handle(MouseEvent event) {
       switch (event.getEventType().toString()) {
            case "MOUSE_CLICKED":
                System.out.println("X: " +event.getX() + "     Y: " + event.getY());
                //gameRenderer.repaint(event.getX(),event.getY());
                
                break;
            case "MOUSE_RELEASED":
                break;
            case "MOUSE_DRAGGED":
                System.out.println("X: " +event.getX() + "     Y: " + event.getY());
                //gameRenderer.repaint(event.getX(),event.getY());
                break;
        }
    
    }
}

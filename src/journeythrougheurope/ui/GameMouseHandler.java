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
import journeythrougheurope.thread.GameManager;
import journeythrougheurope.thread.GameRenderer;

/**
 *
 * @author Karl
 */
public class GameMouseHandler implements EventHandler<MouseEvent> {

    private GameManager currentGameManager;
    private GameRenderer gameRenderer;
    private JourneyThroughEuropeUI ui;
    private boolean mouseDragged;
    private double gridWidth;
    private double gridHeight;

    public GameMouseHandler(JourneyThroughEuropeUI ui, GameRenderer gameRenderer) {
        this.gameRenderer = gameRenderer;
        currentGameManager = null;
        mouseDragged = false;

        this.ui = ui;
        gridWidth = this.ui.getGameGridImages()[0].getImage().getWidth();
        gridHeight = this.ui.getGameGridImages()[0].getImage().getHeight();
    }

    public void handle(MouseEvent event) {

        if (currentGameManager != null) {
            switch (event.getEventType().toString()) {
                case "MOUSE_CLICKED":
                    //System.out.println("Mouse Clicked");
                    if (currentGameManager.getPlayerManager().getMovesRemaining() != 0 && currentGameManager.getPlayerManager().isHuman()) {
                        System.out.println("GameMouseHandler: " + currentGameManager.isHumanMoveValid(event.getX(), event.getY()));
                    }

                    break;
                case "MOUSE_RELEASED":
                    if (mouseDragged) {
                        //System.out.println("Mouse Released");
                        mouseDragged = false;
                        ui.getGameScrollPane().setPannable(true);
                    }
                    break;
                case "MOUSE_DRAGGED":
                    //System.out.println("Mouse Dragged");
                    //System.out.println("X: " + event.getX() + "     Y: " + event.getY());
                    Point2D currentPlayerPosition = currentGameManager.getPlayerManager().getCurrentPosition();
                    Point2D currentDragPosition = new Point2D(event.getX(), event.getY());

                    Rectangle2D currentPlayerRect = new Rectangle2D(currentPlayerPosition.getX(), currentPlayerPosition.getY(), 30, 30);
                    Rectangle2D currentDragRect = new Rectangle2D(currentDragPosition.getX(), currentDragPosition.getY(), 10, 10);
                    if (currentPlayerRect.intersects(currentDragRect) && currentGameManager.getPlayerManager().getMovesRemaining() != 0) {
                        mouseDragged = true;
                    }
                    if (mouseDragged) {
                        ui.getGameScrollPane().setPannable(false);
                        gameRenderer.repaint(event.getX(), event.getY());
                    }
                    break;
            }
        }

    }

    public void setGameManager(GameManager currentGameManager) {
        this.currentGameManager = currentGameManager;
    }

    public boolean getMouseDragged() {
        return mouseDragged;
    }
}

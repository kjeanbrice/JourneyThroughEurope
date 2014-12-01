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
import journeythrougheurope.thread.FlightManager;
import journeythrougheurope.thread.FlightRenderer;
import journeythrougheurope.thread.GameRenderer;

/**
 *
 * @author Karl
 */
public class FlightMouseHandler implements EventHandler<MouseEvent> {

    private JourneyThroughEuropeUI ui;
    private FlightRenderer flightRenderer;
    private FlightManager currentFlightManager;

    private double gridWidth;
    private double gridHeight;

    public FlightMouseHandler(JourneyThroughEuropeUI ui, FlightRenderer flightRenderer) {
        this.flightRenderer = flightRenderer;
        currentFlightManager = null;
        //mouseDragged = false;

        this.ui = ui;
        gridWidth = this.ui.getFlightScreenImage().getImage().getWidth();
        gridHeight = this.ui.getFlightScreenImage().getImage().getHeight();
    }

    @Override
    public void handle(MouseEvent event) {
        if (currentFlightManager != null) {
            switch (event.getEventType().toString()) {
                case "MOUSE_CLICKED":
                    if (currentFlightManager.isPlayerMoveValid(event.getX(), event.getY())) {
                        System.out.println("FlightMouseHandler - Move is valid");
                        ui.getEventHandler().respondToSwitchScreenRequest(JourneyThroughEuropeUI.JourneyThroughEuropeUIState.PLAY_GAME_STATE);
                        ui.getGSM().processFlightRequest(currentFlightManager.getDestination(), currentFlightManager.getMoveCost());
                        currentFlightManager.reset();
                    }
                    else
                        System.out.println("FlightMouseHandler - Move is not valid");
                    break;
                case "MOUSE_RELEASED":
                    break;
                case "MOUSE_DRAGGED":
                    
                    break;
            }
        } else {
            System.out.println("CurrentFlightManager is null");
        }
    }

    public void setFlightManager(FlightManager currentFlightManager) {
        this.currentFlightManager = currentFlightManager;
    }
}

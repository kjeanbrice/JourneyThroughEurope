/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.thread;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import journeythrougheurope.ui.FlightMouseHandler;

import journeythrougheurope.ui.JourneyThroughEuropeUI;
import journeythrougheurope.ui.PlayerManager;

/**
 *
 * @author Karl
 */
public class FlightThread extends AnimationTimer {

    private JourneyThroughEuropeUI ui;
    private int currentPlayer;
    private ArrayList<PlayerManager> players;
    private FlightRenderer flightRenderer;
    private FlightMouseHandler mouseHandler;
   
    private FlightManager flightManagers[];
    private FlightManager currentFlightManager;
    

    public FlightThread(JourneyThroughEuropeUI ui) {
        this.ui = ui;
        currentPlayer = -1;

        players = this.ui.getPlayers();
        initFlightManagers();

        flightRenderer = new FlightRenderer(this.ui.getFlightPanel().getWidth(), this.ui.getFlightPanel().getHeight(), this.ui, flightManagers);
        mouseHandler = new FlightMouseHandler(this.ui, flightRenderer);
        this.ui.getFlightPanel().setOnMouseClicked(mouseHandler);
        this.ui.getFlightPanel().setOnMouseDragged(mouseHandler);
        this.ui.getFlightPanel().setOnMouseReleased(mouseHandler);
        
        currentFlightManager = null;
    }

    public void initFlightManagers() {
        flightManagers = new FlightManager[players.size()];
        for (int i = 0; i < players.size(); i++) {
            flightManagers[i] = new FlightManager(players.get(i), players, ui);
        }
    }

    public void startFlightThread() {
        ui.setFlightToScreen(flightRenderer);
        start();
    }

    public void stopFlightThread() {
        stop();
    }

    @Override
    public void handle(long now) {
       if(currentFlightManager != null)
       {
           update();
           render();
       }
    }
    
    public void render()
    {
        flightRenderer.repaint();
    }
    
    public void update()
    {
        
    }
    public void updatePlayer(int currentPlayer)
    {
        this.currentPlayer = currentPlayer;
        currentFlightManager = flightManagers[this.currentPlayer];
        flightRenderer.setCurrentPlayer(this.currentPlayer);
        mouseHandler.setFlightManager(currentFlightManager);
    }

}

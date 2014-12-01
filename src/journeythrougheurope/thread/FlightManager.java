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
import journeythrougheurope.game.JourneyThroughEuropeCity;
import journeythrougheurope.ui.JourneyThroughEuropeUI;
import journeythrougheurope.ui.PlayerManager;

/**
 *
 * @author Karl
 */
public class FlightManager {

    private PlayerManager currentPlayer;
    private ArrayList<PlayerManager> players;
    private JourneyThroughEuropeUI ui;
    private ArrayList<String> airportCities;

    private final double CONVERSION_FACTOR = .60;
    private final int SIDE_LENGTH = 25;

    private boolean moveInProgress;
    private boolean scrolling;

    private Point2D startLocation;
    private JourneyThroughEuropeCity destination;

    private ScrollPane flightScrollPane;
    private double hValueX;
    private double vValueY;
    private double gridWidth;
    private double gridHeight;
    private int moveCost;

    private final int GRID_1 = 1;
    private final int GRID_2 = 2;
    private final int GRID_3 = 3;
    private final int GRID_4 = 4;
    private final int GRID_5 = 5;
    private final int GRID_6 = 6;
    private final int SAME_GRID = 2;
    private final int DIFFERENT_GRID = 4;

    public FlightManager(PlayerManager currentPlayer, ArrayList<PlayerManager> players, JourneyThroughEuropeUI ui) {
        this.currentPlayer = currentPlayer;
        this.players = players;
        this.airportCities = ui.getGSM().processAirportRequest();
        this.ui = ui;
        moveInProgress = false;

        startLocation = null;
        destination = null;
        hValueX = 0;
        vValueY = 0;
        flightScrollPane = ui.getFlightScrollPane();

        gridWidth = this.ui.getFlightPanel().getWidth();
        gridHeight = this.ui.getFlightPanel().getHeight();     
        moveCost = -1;

    }

    public boolean isPlayerMoveValid(double xPosition, double yPosition) {
            if (isPlayerAtAirport()) {
                boolean airportFound = false;
                Rectangle2D clickedPosition = new Rectangle2D(xPosition - (SIDE_LENGTH / 2), yPosition - (SIDE_LENGTH / 2), SIDE_LENGTH, SIDE_LENGTH);
                JourneyThroughEuropeCity city = null;
                for (int i = 0; i < airportCities.size(); i++) {
                    city = ui.getGSM().processGetCityRequest(airportCities.get(i));
                    Rectangle2D airportLocation = new Rectangle2D(city.getAirportLocation().getX() - (SIDE_LENGTH / 2), city.getAirportLocation().getY() - (SIDE_LENGTH / 2), SIDE_LENGTH, SIDE_LENGTH);
                    if (clickedPosition.intersects(airportLocation) && !(currentPlayer.getCurrentCity().equalsIgnoreCase(city.getCityName()))) {
                        airportFound = true;
                        break;
                    }
                }

                if (!airportFound || city == null)// or previous city = city.getCityName()
                {
                    return false;
                }

                switch (ui.getGSM().processGetCityRequest(currentPlayer.getCurrentCity()).getAirportGrid()) {
                    case GRID_1:
                        if (city.getAirportGrid() == GRID_1 && (currentPlayer.getMovesRemaining() - SAME_GRID) >= 0) {
                            moveCost = 2;
                        } else if ((city.getAirportGrid() == GRID_2 || city.getAirportGrid() == GRID_4) && ((currentPlayer.getMovesRemaining() - DIFFERENT_GRID) >= 0)) {
                            moveCost = 4;
                        } else {
                            return false;
                        }
                        break;

                    case GRID_2:
                        if (city.getAirportGrid() == GRID_2 && (currentPlayer.getMovesRemaining() - SAME_GRID) >= 0) {
                            moveCost = 2;
                        } else if ((city.getAirportGrid() == GRID_1 || city.getAirportGrid() == GRID_3) && ((currentPlayer.getMovesRemaining() - DIFFERENT_GRID) >= 0)) {
                            moveCost = 4;
                        } else {
                            return false;
                        }
                        break;
                    case GRID_3:
                        if (city.getAirportGrid() == GRID_3 && (currentPlayer.getMovesRemaining() - SAME_GRID) >= 0) {
                            moveCost = 2;
                        } else if ((city.getAirportGrid() == GRID_2 || city.getAirportGrid() == GRID_4 || city.getAirportGrid() == GRID_6)
                                && ((currentPlayer.getMovesRemaining() - DIFFERENT_GRID) >= 0)) {
                           moveCost = 4;
                        } else {
                            return false;
                        }
                        break;
                    case GRID_4:
                        if (city.getAirportGrid() == GRID_4 && (currentPlayer.getMovesRemaining() - SAME_GRID) >= 0) {
                            moveCost = 2;
                        } else if ((city.getAirportGrid() == GRID_1 || city.getAirportGrid() == GRID_5 || city.getAirportGrid() == GRID_3)
                                && ((currentPlayer.getMovesRemaining() - DIFFERENT_GRID) >= 0)) {
                            moveCost = 4;
                        } else {
                            return false;
                        }
                        break;
                    case GRID_5:
                        if (city.getAirportGrid() == GRID_5 && (currentPlayer.getMovesRemaining() - SAME_GRID) >= 0) {
                            moveCost = 2;
                        } else if ((city.getAirportGrid() == GRID_4 || city.getAirportGrid() == GRID_6) && ((currentPlayer.getMovesRemaining() - DIFFERENT_GRID) >= 0)) {
                           moveCost = 4;
                        } else {
                            return false;
                        }
                        break;
                    case GRID_6:
                        if (city.getAirportGrid() == GRID_6 && (currentPlayer.getMovesRemaining() - SAME_GRID) >= 0) {
                            moveCost = 2;
                        } else if ((city.getAirportGrid() == GRID_5 || city.getAirportGrid() == GRID_3) && ((currentPlayer.getMovesRemaining() - DIFFERENT_GRID) >= 0)) {
                            moveCost = 4;
                        } else {
                            return false;
                        }
                        break;
                }

                
                 destination = city; 
                return true;
            }
            return false;
        }
    
    public boolean isPlayerAtAirport() {
        for (int i = 0; i < airportCities.size(); i++) {
            if (currentPlayer.getCurrentCity().equalsIgnoreCase(airportCities.get(i))) {
                return true;
            }
        }
        return false;
    }
    
    public PlayerManager getPlayerManager()
    {
        return currentPlayer;
    }
    
    public JourneyThroughEuropeCity getDestination()
    {
        return destination;
    }
    
    public int getMoveCost()
    {
        return moveCost;
    }
    
    public void reset()
    {
        destination = null;
        moveCost = 0;
    }

}

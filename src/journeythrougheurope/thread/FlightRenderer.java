/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.thread;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import journeythrougheurope.game.JourneyThroughEuropeCity;
import journeythrougheurope.ui.JourneyThroughEuropeUI;

/**
 *
 * @author Karl
 */
public class FlightRenderer extends Canvas {

    private static int IMAGE_SIZE = 35;
    private static int IMAGE_OFFSET = 20;
    private final double CONVERSION_FACTOR = .60;
    private final int SIDE_LENGTH = 25;
    
    private double canvasWidth;
    private double canvasHeight;

    private GraphicsContext gc;
    private JourneyThroughEuropeUI ui;
    private FlightManager[] flightManagers;

    
    private int currentPlayer;

    public FlightRenderer(double canvasWidth, double canvasHeight, JourneyThroughEuropeUI ui, FlightManager[] flightManagers) {
        this.ui = ui;
        this.flightManagers = flightManagers;
        
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;

        setWidth(this.canvasWidth);
        setHeight(this.canvasHeight);
        currentPlayer = -1;
    }

    public void repaint() {
        gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        
        if(currentPlayer != -1)
        {   
            if(flightManagers[currentPlayer].isPlayerAtAirport())
            {
            JourneyThroughEuropeCity airport = ui.getGSM().processGetCityRequest(flightManagers[currentPlayer].getPlayerManager().getCurrentCity());
            gc.setFill(Color.GREEN);
            gc.drawImage(flightManagers[currentPlayer].getPlayerManager().getPlayerImage(),airport.getAirportLocation().getX() - IMAGE_OFFSET,airport.getAirportLocation().getY() - IMAGE_OFFSET);  
            }
        }
        /*
        ArrayList<String> airportData = ui.getGSM().processAirportRequest();


         for (int i = 0; i < airportData.size(); i++) {
         JourneyThroughEuropeCity city = ui.getGSM().processGetCityRequest(airportData.get(i));
         gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
         gc.setFill(Color.GREEN);
         gc.fillText(city.getCityName(), city.getAirportLocation().getX() - SIDE_LENGTH, city.getAirportLocation().getY() - SIDE_LENGTH);
         gc.fillOval(city.getAirportLocation().getX() - (SIDE_LENGTH / 2), city.getAirportLocation().getY() - (SIDE_LENGTH / 2), SIDE_LENGTH, SIDE_LENGTH);
         //gc.strokeOval(cityX - (SIDE_LENGTH / 2) ,cityY - (SIDE_LENGTH / 2),SIDE_LENGTH,SIDE_LENGTH);
         }
        
        // if(!intersects)
        //{
        gc.setFill(Color.BLUE);
        gc.fillOval(x - (SIDE_LENGTH / 2), y - (SIDE_LENGTH / 2), SIDE_LENGTH, SIDE_LENGTH);
        //}
        //else
        //  intersects = false;*/
    }
    
    public void setCurrentPlayer(int currentPlayer)
    {
        this.currentPlayer = currentPlayer;
    }

}

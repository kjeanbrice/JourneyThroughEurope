/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.game;

import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import journeythrougheurope.botalgorithm.Edge;
import journeythrougheurope.botalgorithm.Vertex;

/**
 *
 * @author Karl
 */
public class JourneyThroughEuropeCity {

    private Image front;
    private String cityName;
    private String cardColor;
    private int gridLocation;
    private double gridX;
    private double gridY;
    private ArrayList<String> neighboringLandCities;
    private ArrayList<String> neighboringSeaCities;
    private Vertex vertex;
    private Edge[] adjacencies;

    private boolean isAirport;
    private Point2D airportLocation;
    private int airportGrid;
    
    private boolean hasTownInformation;
    private String townInformation;

    public JourneyThroughEuropeCity(String cityName, String cardColor, int gridLocation, double gridX, double gridY) {
        this.cityName = cityName;
        this.cardColor = cardColor;
        this.gridLocation = gridLocation;
        this.gridX = gridX;
        this.gridY = gridY;
        neighboringLandCities = new ArrayList<String>();
        neighboringSeaCities = new ArrayList<String>();
        vertex = null;
        adjacencies = null;
        
        isAirport = false;
        hasTownInformation = false;
        
        townInformation = "";
        airportLocation = null;
        airportGrid = -1;
    }

    public JourneyThroughEuropeCity() {
        this("", "", -1, -1, -1);
    }

    public void setTownInformation(String townInformation)
    {
        this.townInformation = townInformation;
    }
   
    public void setHasTownInformation(boolean hasTownInformation)
    {
        this.hasTownInformation = hasTownInformation;
    }
    
    public boolean hasTownInformation()
    {
        return hasTownInformation;
    }
    
    public String getTownInformation()
    {
        return townInformation;
    }
    public void setAirport(boolean isAirport) {
        this.isAirport = isAirport;
    }

    public boolean isAirport() {
        return isAirport;
    }

    public void setAirportLocation(Point2D airportLocation) {
        this.airportLocation = airportLocation;
    }

    public Point2D getAirportLocation() {
        return airportLocation;
    }

    public void setAirportGrid(int airportGrid) {
        this.airportGrid = airportGrid;
    }

    public int getAirportGrid() {
        return airportGrid;
    }

    public void setFront(String imageName) {
        front = new Image("file:images/cards/" + imageName + ".jpg");
    }

    public Image getFront() {
        return front;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCardColor() {
        return cardColor;
    }

    public int getGridLocation() {
        return gridLocation;
    }

    public double getGridX() {
        return gridX;
    }

    public double getGridY() {
        return gridY;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCardColor(String cardColor) {
        this.cardColor = cardColor;
    }

    public void setGridLocation(int gridLocation) {
        this.gridLocation = gridLocation;
    }

    public void setGridX(double gridX) {
        this.gridX = gridX;
    }

    public void setGridY(double gridY) {
        this.gridY = gridY;
    }

    public Point2D getPoint() {
        return new Point2D(gridX, gridY);
    }

    public void addNeighboringLandCity(String city) {
        neighboringLandCities.add(city);
    }

    public void addNeighboringSeaCity(String city) {
        neighboringSeaCities.add(city);
    }

    public ArrayList<String> getNeighboringLandCities() {
        return neighboringLandCities;
    }

    public ArrayList<String> getNeighboringSeaCities() {
        return neighboringSeaCities;
    }

    public void setNeighboringSeaCities(ArrayList<String> neighboringSeaCities) {
        this.neighboringSeaCities = neighboringSeaCities;
    }

    public void setNeighboringLandCities(ArrayList<String> neighboringLandCities) {
        this.neighboringLandCities = neighboringLandCities;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertexAdjacencies(Edge[] adjacencies) {
        this.adjacencies = adjacencies;
        vertex.setAdjacencies(adjacencies);
    }

    public String toString() {
        String output = "";
        output += "City Name: " + cityName + "\nCard Color: " + cardColor + "\nGrid Location: " + gridLocation + "\nX: " + gridX
                + "\nY: " + gridY + "\nNeighboring Land Cities: " + neighboringLandCities.toString() + "\nNeighboring Sea Cities: " + neighboringSeaCities.toString()
                + "\nVertex: " + vertex.toString() + "\n";
        for (int i = 0; i < adjacencies.length; i++) {
            output += "Edge " + i + ": " + adjacencies[i].toString() + "\n";
        }

        return output;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.game;

/**
 *
 * @author Karl
 */
public class JourneyThroughEuropeCity {

    private String cityName;
    private String cardColor;
    private int gridLocation;
    private double gridX;
    private double gridY;

    public JourneyThroughEuropeCity(String cityName, String cardColor, int gridLocation, double gridX, double gridY) {
         this.cityName = cityName;
         this.cardColor = cardColor;
         this.gridLocation = gridLocation;
         this.gridX = gridX;
         this.gridY = gridY;
    }

    public JourneyThroughEuropeCity() {
        this("","",-1,-1,-1);
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

    public String toString() {
        return "City Name: " + cityName + "\nCard Color: " + cardColor + "\nGrid Location: " + gridLocation + "\nX: " + gridX
                + "\nY: " + gridY;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.ui;

import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

/**
 *
 * @author Karl
 */
public class PlayerManager {

    private TextField playerName;
    private boolean human;
    private ArrayList<String> cards;
    private ArrayList<Point2D> cardLocations;
    private String currentCity;
    private String homeCity;
    private Point2D currentLocation;
    private int currentGridLocation;
    private int homeGridLocation;
    
    private Image homeImage;

    public PlayerManager(TextField playerName, boolean human) {
        this.playerName = playerName;
        this.human = human;
        cards = new ArrayList<String>();
        cardLocations = new ArrayList<Point2D>();
        currentCity = "";
        homeCity = "";
        currentLocation = null;
        currentGridLocation = -1;
    }

    public void setCurrentGridLocation(int gridLocation) {
        this.currentGridLocation = gridLocation;
    }

    public int getCurrentGridLocation() {
        return currentGridLocation;
    }
    
    public void setHomeGridLocation(int homeGridLocation)
    {
        this.homeGridLocation = homeGridLocation;
    }
    
    public int getHomeGridLocation()
    {
        return homeGridLocation;
    }
    
    public void setHomeImage(Image homeImage)
    {
        this.homeImage = homeImage;
    }
    
    public Image getHomeImage()
    {
        return homeImage;
    }

    public void setCurrentLocation(Point2D point) {
        currentLocation = point;
    }

    public Point2D getCurrentLocation() {
        return currentLocation;
    }

    public String getPlayerName() {
        return playerName.getText();
    }

    public void setPlayerName(String name) {
        playerName.setText(name);
    }

    public void setHuman(boolean human) {
        this.human = human;
    }

    public boolean isHuman() {
        return human;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public void addCard(String city) {
        cards.add(city);
    }

    public boolean removeCard(String city) {
        if (cards.isEmpty()) {
            throw new NullPointerException("This PlayerManager has no more cards");
        }

        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).equalsIgnoreCase(city)) {
                cards.remove(i);
                return true;
            }
        }

        return false;
    }

    public ArrayList<String> getCards() {
        return cards;
    }

    public ArrayList<Point2D> getCardLocations() {
        return cardLocations;
    }

    public String toString() {
        return "Player name: " + playerName.getText() + "\nIs Human: " + human + "\nHand: " + cards.toString() + ""
                + "\nCurrent City: " + currentCity + "\nReturn City: " + homeCity + "\nCurrent Location: "
                + currentLocation.toString();
    }

}

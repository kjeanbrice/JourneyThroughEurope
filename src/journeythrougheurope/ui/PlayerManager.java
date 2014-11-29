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
    private ArrayList<String> moveHistory;

    private String currentCity;
    private String homeCity;
    private Point2D currentPosition;
    private int currentGridLocation;
    private int homeGridLocation;
    private int movesRemaining;

    private Image homeImage;
    private Image playerImage;

    public PlayerManager(TextField playerName, boolean human) {
        this.playerName = playerName;
        this.human = human;

        cards = new ArrayList<String>();
        cardLocations = new ArrayList<Point2D>();
        moveHistory = new ArrayList<String>();

        currentCity = "";
        homeCity = "";

        currentPosition = null;
        homeImage = null;
        playerImage = null;
        currentGridLocation = -1;
        movesRemaining = 0;
    }

    public void setMovesRemaining(int movesRemaining) {
        this.movesRemaining = movesRemaining;
    }

    public int getMovesRemaining() {
        return movesRemaining;
    }

    public void setCurrentGridLocation(int gridLocation) {
        this.currentGridLocation = gridLocation;
    }

    public int getCurrentGridLocation() {
        return currentGridLocation;
    }

    public void setHomeGridLocation(int homeGridLocation) {
        this.homeGridLocation = homeGridLocation;
    }

    public int getHomeGridLocation() {
        return homeGridLocation;
    }

    public void setHomeImage(Image homeImage) {
        this.homeImage = homeImage;
    }

    public Image getHomeImage() {
        return homeImage;
    }

    public void setPlayerImage(Image playerImage) {
        this.playerImage = playerImage;
    }

    public Image getPlayerImage() {
        return playerImage;
    }

    public void setCurrentPosition(Point2D point) {
        currentPosition = point;
    }

    public Point2D getCurrentPosition() {
        return currentPosition;
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
    
    public void setCards(ArrayList<String> cards)
    {
        this.cards = cards;
    }

    public ArrayList<Point2D> getCardLocations() {
        return cardLocations;
    }

    public void addToMoveHistory(String city) {
        moveHistory.add(city);
    }

    public ArrayList<String> getMoveHistory() {
        return moveHistory;
    }
    
    public void setMoveHistory(ArrayList<String> moveHistory)
    {
        this.moveHistory = moveHistory;
    }

    public String printGameHistory() {
        String output = playerName.getText().trim() + " - ";
        for (int i = 0; i < moveHistory.size(); i++) {
            if (i == moveHistory.size() - 1) {
                output += moveHistory.get(i);
            } else {
                output += moveHistory.get(i) + ", ";
            }
        }
        return output;
    }

    public String toString() {
        return "Player name: " + playerName.getText() + "\nIs Human: " + human + "\nHand: " + cards.toString() + ""
                + "\nCurrent City: " + currentCity + "\nReturn City: " + homeCity + "\nCurrent Location: "
                + currentPosition.toString();
    }

}

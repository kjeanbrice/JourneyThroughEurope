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

/**
 *
 * @author Karl
 */
public class PlayerManager 
{
    private TextField playerName;
    private boolean human;
    private ArrayList<String> cards;
    private ArrayList<Point2D> cardLocations;
    
    public PlayerManager(TextField playerName,boolean human)
    {
        this.playerName = playerName;
        this.human = human;
        cards = new ArrayList<String>();
        cardLocations = new ArrayList<Point2D>();
    }
    
    public String getPlayerName()
    {
       return playerName.getText();
    }
    
    public void setPlayerName(String name)
    {
        playerName.setText(name);
    }
    
    public void setHuman(boolean human)
    {
        this.human = human;
    }
    
    public boolean isHuman()
    {
        return human;
    }
    
    public void addCard(String city)
    {
        cards.add(city);
    }
    
    public ArrayList<String> getCards()
    {
        return cards;
    }
    
    public ArrayList<Point2D> getCardLocations()
    {
        return cardLocations;
    }
    
    public String toString()
    {
        return "Player name: " + playerName.getText() + "\nIs Human: " + human;
    }
            
}

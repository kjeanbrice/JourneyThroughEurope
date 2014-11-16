/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.ui;

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
    
    public PlayerManager(TextField playerName,boolean human)
    {
        this.playerName = playerName;
        this.human = human;
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
    
    public String toString()
    {
        return "Player name: " + playerName.getText() + "\nIs Human: " + human;
    }
            
}

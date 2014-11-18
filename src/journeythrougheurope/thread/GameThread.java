/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.thread;

import javafx.animation.AnimationTimer;
import journeythrougheurope.ui.GameMouseHandler;
import journeythrougheurope.ui.JourneyThroughEuropeUI;

/**
 *
 * @author Karl
 */
public class GameThread extends AnimationTimer
{
    
    private JourneyThroughEuropeUI ui;
    private GameMouseHandler mouseHandler;
    private boolean won;

    public GameThread(JourneyThroughEuropeUI ui)
    {
        this.ui = ui;      
    }
    @Override
    public void handle(long now) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void startGameThread()
    {
        start();
    }
    
    public void stopGameThread()
    {
        stop();
    }
    public boolean isWon()
    {
        return won;
    }

   
    
    
    
}

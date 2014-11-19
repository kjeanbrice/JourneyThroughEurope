/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.game;

import journeythrougheurope.thread.CardThread;
import journeythrougheurope.thread.GameThread;
import journeythrougheurope.ui.JourneyThroughEuropeUI;

/**
 *
 * @author Karl
 */
public class JourneyThroughEuropeGameData {

    private JourneyThroughEuropeUI ui;
    private CardThread cardThread;
    private GameThread gameThread;
    private int currentPlayer;
    private boolean won;

    public JourneyThroughEuropeGameData(JourneyThroughEuropeUI ui) {
        this.ui = ui;
        currentPlayer = 0;
        cardThread = new CardThread(this.ui);
        gameThread = new GameThread(this.ui);
        won = false;
    }

    public void startGame() {
        cardThread.startCardThread();
        gameThread.startGameThread();
    }

    private void stopGame() {
        cardThread.stopCardThread();
        gameThread.stopGameThread();
    }

    public boolean isWon() {
        if (gameThread.isWon()) {
            won = true;
            stopGame();
        }
        return won;
    }
    
    public void giveUp()
    {
        stopGame();
    }
    
    public void incrementPlayer()
    {  
        currentPlayer++;
        if(currentPlayer == ui.getPlayers().size())
         currentPlayer = 0;
    }

    public void updateDieRolledRequest(int die)
    {
        gameThread.updateRemainingMoves(die);
    }
    
    public void startTurn()
    {
        gameThread.updatePlayer(currentPlayer);
        cardThread.updatePlayer(currentPlayer);
    }
}

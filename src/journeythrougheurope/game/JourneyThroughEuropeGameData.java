/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.game;

import journeythrougheurope.thread.CardThread;
import journeythrougheurope.thread.FlightThread;
import journeythrougheurope.thread.GameThread;
import journeythrougheurope.ui.JourneyThroughEuropeUI;

/**
 *
 * @author Karl
 */
public class JourneyThroughEuropeGameData {

    private final int SIX = 6;

    private JourneyThroughEuropeUI ui;
    private CardThread cardThread;
    private GameThread gameThread;
    private FlightThread flightThread;

    private int currentPlayer;
    private boolean won;

    private boolean endOfFirstTurn;
    private boolean wait;

    public JourneyThroughEuropeGameData(JourneyThroughEuropeUI ui) {
        this.ui = ui;
        wait = false;
        currentPlayer = 0;
        endOfFirstTurn = false;
        cardThread = new CardThread(this.ui);
        gameThread = new GameThread(this.ui);
        flightThread = new FlightThread(this.ui);

        won = false;
    }

    public void startGame() {
        cardThread.startCardThread();
        gameThread.startGameThread();
        flightThread.startFlightThread();
    }

    public void stopGame() {
        cardThread.stopCardThread();
        gameThread.stopGameThread();
        flightThread.stopFlightThread();
    }

    public boolean isWon() {
        if (gameThread.isWon()) {
            won = true;
        }
        return won;
    }

    public void giveUp() {
        stopGame();
    }

    public synchronized void incrementPlayer() {
        currentPlayer++;
        if (currentPlayer == ui.getPlayers().size()) {
            currentPlayer = 0;
            if (!endOfFirstTurn) {
                gameThread.setEndOfFirstTurn(true);
                endOfFirstTurn = true;
            }
        }
    }

    public synchronized void updateRollRequest(int die) {
        if (die == SIX) {
            gameThread.updateRemainingMoves(die);
        } else {
            gameThread.updateRemainingMoves(die);
            ui.disableRollButton();
        }
            gameThread.checkFlightStatus();
        
    }

    public synchronized void startTurn() {
        if (!wait) {
            gameThread.updatePlayer(currentPlayer);
            cardThread.updatePlayer(currentPlayer);
            flightThread.updatePlayer(currentPlayer);
            ui.resetRollImage();
        }
    }

    public void sendFlightRequest(JourneyThroughEuropeCity city, int moveCost) {
        gameThread.handleFlightRequest(city, moveCost);
    }
    
    public void sendStatusOnScrollPane(boolean status)
    { 
            gameThread.setStatusOnScrollPane(status);
            cardThread.setStatusOnScrollPane(status);
    }

    public synchronized void removeCardFromCurrentPlayer(int cityIndex) {
        cardThread.setRemovingCardStatus(true, cityIndex);
    }

    public void setWait(boolean wait) {
        this.wait = wait;
    }

    public boolean getWait() {
        return wait;
    }
    
    public int getCurrentPlayer()
    {
        return currentPlayer;
    }
    
    public void setCurrentPlayer(int currentPlayer)
    {
        this.currentPlayer = currentPlayer;
        
    }
}

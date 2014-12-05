/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.thread;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import journeythrougheurope.game.JourneyThroughEuropeCity;
import journeythrougheurope.ui.GameMouseHandler;
import journeythrougheurope.ui.JourneyThroughEuropeUI;
import journeythrougheurope.ui.PlayerManager;

/**
 *
 * @author Karl
 */
public class GameThread extends AnimationTimer {

    private final int MAX_DELAY = 40;
    private JourneyThroughEuropeUI ui;
    private GameMouseHandler mouseHandler;
    private ArrayList<String> airports;

    private ArrayList<PlayerManager> players;
    private GameManager gameManagers[];
    private GameRenderer gameRenderer;
    private GameManager currentGameManager;
    private int remainingMoves;
    private int moveCost;

    private int currentPlayer;
    private int delayCount;

    private boolean won;
    private boolean botRoll;
    private boolean endOfFirstTurn;
    private boolean delay;
    private boolean nextTurn;

    public GameThread(JourneyThroughEuropeUI ui) {
        this.ui = ui;
        currentPlayer = -1;
        remainingMoves = 0;
        delayCount = 0;

        players = this.ui.getPlayers();
        initGameManagers();

        gameRenderer = new GameRenderer(this.ui.getGamePanel().getWidth(), this.ui.getGamePanel().getHeight(), this.ui, gameManagers);
        mouseHandler = new GameMouseHandler(this.ui, gameRenderer);
        this.ui.getGamePanel().setOnMouseClicked(mouseHandler);
        this.ui.getGamePanel().setOnMouseDragged(mouseHandler);
        this.ui.getGamePanel().setOnMouseReleased(mouseHandler);

        botRoll = false;
        won = false;
        moveCost = 0;
        airports = ui.getGSM().processAirportRequest();
        endOfFirstTurn = false;
        delay = false;
        nextTurn = false;

    }

    public void initGameManagers() {
        gameManagers = new GameManager[players.size()];
        for (int i = 0; i < players.size(); i++) {
            gameManagers[i] = new GameManager(players.get(i), players, ui);
        }

    }

    public void startGameThread() {
        ui.setGameToScreen(gameRenderer);
        start();
    }

    public void stopGameThread() {
        stop();
    }

    @Override
    public void handle(long now) {

        if (!delay) {
            update();
            render();
        }

        if (delay) {
            delayCount++;
            if (delayCount == MAX_DELAY) {
                delay = false;
                delayCount = 0;
                if (nextTurn) {
                    currentGameManager.setAlreadyFlew(false);
                    ui.getGSM().processIncrementPlayerRequest();
                    ui.getGSM().processStartTurnRequest();
                    nextTurn = false;
                }
            }

        }
    }

    public void render() {
        if (!mouseHandler.getMouseDragged()) {
            gameRenderer.repaint();
        }
    }

    public void update() {
        if (currentGameManager != null) {

            if (!currentGameManager.getPlayerManager().isHuman()) {
                if (botRoll) {
                    int roll = (int) ((Math.random() * 6) + 1);
                    ui.setDieImage(roll);
                    updateRemainingMoves(roll);

                    ui.disableRollButton();
                    ui.disableGridButtons();
                    ui.disableSaveButton();

                    if (roll != 6) {
                        botRoll = false;
                    }
                }
                if (!currentGameManager.isMoveInProgress()) {
                    if (currentGameManager.isBotMoveValid(endOfFirstTurn)) {
                        ui.getGSM().processStatusOnScrollPaneRequest(false);
                    }
                }
            }
            if (currentGameManager.isMoveInProgress()) {
                ui.disableGridButtons();
                ui.disableSaveButton();

                if (currentGameManager.isDestinationSeaRoute()) {
                    if (!currentGameManager.isWaitingAtPort()) {
                        currentGameManager.dontMove();
                        currentGameManager.resetPreviousCity();
                        currentGameManager.getPlayerManager().setMovesRemaining(0);
                        currentGameManager.setWaitingAtPort(true);
                        System.out.println("Game Thread: " + currentGameManager.getPlayerManager().getPlayerName() + " is waiting to sail at the city " + currentGameManager.getPlayerManager().getCurrentCity() + ".");
                        ui.getTextArea().setText(currentGameManager.getPlayerManager().getPlayerName() + " is waiting to sail at the city " + currentGameManager.getPlayerManager().getCurrentCity() + ".");

                        ui.getGSM().processIncrementPlayerRequest();
                        ui.getGSM().processStartTurnRequest();
                        //System.out.println("Game Thread: Next Turn " + currentGameManager.getPlayerManager().getPlayerName() + " Moves Remaining: " + currentGameManager.getPlayerManager().getMovesRemaining() + ".");
                    }

                }

                if (currentGameManager.getPlayerManager().getMovesRemaining() != 0) {

                    if (currentGameManager.isScrolling()) {
                        currentGameManager.scrollBack();
                        ui.disableFlightButton();
                    } else {

                        if (!currentGameManager.move()) {

                            ui.getGSM().processStatusOnScrollPaneRequest(true);
                            delay = true;
                            if (currentGameManager.isWaitingAtPort()) {
                                currentGameManager.setWaitingAtPort(false);
                                System.out.println("Game Thread: " + currentGameManager.getPlayerManager().getPlayerName() + " is no longer waiting to sail.");
                            }

                            boolean removingCard = false;
                            if (moveCost == 0) {
                                currentGameManager.getPlayerManager().setMovesRemaining(currentGameManager.getPlayerManager().getMovesRemaining() - 1);
                            } else {
                                currentGameManager.getPlayerManager().setMovesRemaining(currentGameManager.getPlayerManager().getMovesRemaining() - moveCost);
                                ui.getTextArea().setText(ui.getTextArea().getText() + "\n" + currentGameManager.getPlayerManager().getPlayerName() + " used " + moveCost + " points to fly to "+ currentGameManager.getPlayerManager().getCurrentCity() + ".");
                                moveCost = 0;
                            }

                            checkFlightStatus();
                            ui.enableGridButtons();
                            ui.enableSaveButton();
                            ui.updateMovesRemaining("Moves Remaining: " + currentGameManager.getPlayerManager().getMovesRemaining());

                            if (currentGameManager.getPlayerManager().getCards().size() != 1) {
                                for (int i = 1; i < currentGameManager.getPlayerManager().getCards().size(); i++) {
                                    String currentCity = currentGameManager.getPlayerManager().getCurrentCity();
                                    if (currentCity.equalsIgnoreCase(currentGameManager.getPlayerManager().getCards().get(i))) {
                                        this.ui.getGSM().processRemoveCardRequest(i);
                                        removingCard = true;

                                    }
                                }
                            } else if (currentGameManager.getPlayerManager().getCards().size() == 1) {
                                ArrayList<String> cards = currentGameManager.getPlayerManager().getCards();
                                String currentCity = currentGameManager.getPlayerManager().getCurrentCity();
                                if (currentCity.equalsIgnoreCase(cards.get(0))) {
                                    won = true;
                                    this.ui.getGSM().processRemoveCardRequest(0);
                                    removingCard = true;
                                    stopGameThread();
                                    System.out.println(currentGameManager.getPlayerManager().getPlayerName() + " has won!");
                                }
                            }

                            if ((currentGameManager.getPlayerManager().getMovesRemaining() == 0 && ui.isRollButtonDisabled()) || removingCard) {
                                currentGameManager.resetPreviousCity();
                                currentGameManager.getPlayerManager().setMovesRemaining(0);
                                if (!removingCard) {
                                    nextTurn = true;
                                    //currentGameManager.setAlreadyFlew(false);
                                    //ui.getGSM().processIncrementPlayerRequest();
                                    //ui.getGSM().processStartTurnRequest();

                                } else {
                                    currentGameManager.setAlreadyFlew(false);
                                    ui.getGSM().processSetWaitRequest(true);
                                    ui.disableSaveButton();
                                    delay = false;
                                }

                            }
                        }
                    }
                } else {
                    currentGameManager.setMoveInProgress(false);
                }
            }
        }
    }

    public boolean isWon() {
        return won;
    }

    public void updatePlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        currentGameManager = gameManagers[this.currentPlayer];
        currentGameManager.setCurrentPlayer(currentPlayer);
        gameRenderer.setCurrentPlayer(this.currentPlayer);
        mouseHandler.setGameManager(currentGameManager);
        ui.updateMovesRemaining("Moves Remaining: " + currentGameManager.getPlayerManager().getMovesRemaining());
        botRoll = true;
    }

    public void updateRemainingMoves(int moves) {
        currentGameManager.getPlayerManager().setMovesRemaining(currentGameManager.getPlayerManager().getMovesRemaining() + moves);
        ui.updateMovesRemaining("Moves Remaining: " + currentGameManager.getPlayerManager().getMovesRemaining());
    }

    public void checkFlightStatus() {
        if (endOfFirstTurn) {
            boolean activateFlightButton = false;
            for (int i = 0; i < airports.size(); i++) {
                if (airports.get(i).equalsIgnoreCase(currentGameManager.getPlayerManager().getCurrentCity())
                        && !(currentGameManager.getPlayerManager().getMovesRemaining() < 2) && !currentGameManager.didPlayerFlyThisTurn()) {
                    activateFlightButton = true;
                    break;
                }
            }

            if (activateFlightButton) {
                ui.enableFlightButton();
            } else {
                ui.disableFlightButton();
            }
        }
    }

    public void handleFlightRequest(JourneyThroughEuropeCity city, int moveCost) {
        if (!currentGameManager.didPlayerFlyThisTurn()) {
            this.moveCost = moveCost;
            currentGameManager.handleFlightRequest(city);
        }
    }

    public void setEndOfFirstTurn(boolean status) {
        endOfFirstTurn = true;
    }

    public void setStatusOnScrollPane(boolean status) {
        if (status) {
            ui.enableScrollPaneFocus();
        } else {
            ui.disableScrollPaneFocus();
        }
    }
}

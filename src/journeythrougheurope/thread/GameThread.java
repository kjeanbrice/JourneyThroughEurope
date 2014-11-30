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
import journeythrougheurope.ui.GameMouseHandler;
import journeythrougheurope.ui.JourneyThroughEuropeUI;
import journeythrougheurope.ui.PlayerManager;

/**
 *
 * @author Karl
 */
public class GameThread extends AnimationTimer {

    private JourneyThroughEuropeUI ui;
    private GameMouseHandler mouseHandler;

    private ArrayList<PlayerManager> players;
    private GameManager gameManager[];
    private GameRenderer gameRenderer;
    private GameManager currentGameManager;
    private int remainingMoves;

    private int currentPlayer;

    private boolean won;
    private boolean botRoll;

    public GameThread(JourneyThroughEuropeUI ui) {
        this.ui = ui;
        currentPlayer = -1;
        remainingMoves = 0;

        players = this.ui.getPlayers();
        initGameManagers();

        gameRenderer = new GameRenderer(this.ui.getGamePanel().getWidth(), this.ui.getGamePanel().getHeight(), this.ui, gameManager);
        mouseHandler = new GameMouseHandler(this.ui, gameRenderer);
        this.ui.getGamePanel().setOnMouseClicked(mouseHandler);
        this.ui.getGamePanel().setOnMouseDragged(mouseHandler);
        this.ui.getGamePanel().setOnMouseReleased(mouseHandler);

        botRoll = false;
        won = false;

    }

    public void initGameManagers() {
        gameManager = new GameManager[players.size()];
        for (int i = 0; i < players.size(); i++) {
            gameManager[i] = new GameManager(players.get(i), players, ui);
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
        update();
        render();
    }

    public void render() {
        if (!mouseHandler.getMouseDragged()) {
            gameRenderer.repaint();
        }
    }

    public void update() {
        if (currentGameManager != null) {
            //System.out.println("Game Thread: " + currentGameManager.getPlayerManager().getPlayerName());
            //System.out.println("Game Thread: " + currentGameManager.getPlayerManager().toString());
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
                    currentGameManager.isBotMoveValid();
                }
            }
            if (currentGameManager.isMoveInProgress()) {
                ui.getGameScrollPane().setPannable(false);
                ui.disableGridButtons();
                ui.disableSaveButton();

                
                if (currentGameManager.isDestinationSeaRoute()) {
                    if (!currentGameManager.isWaitingAtPort()) {
                        currentGameManager.dontMove();
                        currentGameManager.resetPreviousCity();
                        currentGameManager.getPlayerManager().setMovesRemaining(0);
                        currentGameManager.setWaitingAtPort(true);
                        System.out.println("Game Thread: " + currentGameManager.getPlayerManager().getPlayerName() + " is waiting to sail at the city " + currentGameManager.getPlayerManager().getCurrentCity() + ".");
                        ui.getGSM().processIncrementPlayerRequest();
                        ui.getGSM().processStartTurnRequest();
                         //System.out.println("Game Thread: Next Turn " + currentGameManager.getPlayerManager().getPlayerName() + " Moves Remaining: " + currentGameManager.getPlayerManager().getMovesRemaining() + ".");
                    }
                        
                }

                if (currentGameManager.getPlayerManager().getMovesRemaining() != 0) {
                    if (currentGameManager.isScrolling()) {
                        currentGameManager.scrollBack();
                    } else {
                        if (!currentGameManager.move()) {
                           
                            //System.out.println("Game Thread - Moves Remaining: " + currentGameManager.getPlayerManager().getMovesRemaining());
                            if (currentGameManager.isWaitingAtPort()) {
                                currentGameManager.setWaitingAtPort(false);
                                System.out.println("Game Thread: " + currentGameManager.getPlayerManager().getPlayerName() + " is no longer waiting to sail.");
                            }

                            boolean removingCard = false;
                            currentGameManager.getPlayerManager().setMovesRemaining(currentGameManager.getPlayerManager().getMovesRemaining() - 1);

                            ui.enableGridButtons();
                            ui.enableSaveButton();

                            ui.getGameScrollPane().setPannable(true);
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
                                    ui.getGSM().processIncrementPlayerRequest();
                                    ui.getGSM().processStartTurnRequest();

                                } else {
                                    ui.getGSM().processSetWaitRequest(true);
                                    ui.disableSaveButton();
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
        currentGameManager = gameManager[this.currentPlayer];
        gameRenderer.setCurrentPlayer(this.currentPlayer);
        mouseHandler.setGameManager(currentGameManager);
        ui.updateMovesRemaining("Moves Remaining: " + currentGameManager.getPlayerManager().getMovesRemaining());
        botRoll = true;
    }

    public void updateRemainingMoves(int moves) {
        currentGameManager.getPlayerManager().setMovesRemaining(currentGameManager.getPlayerManager().getMovesRemaining() + moves);
        ui.updateMovesRemaining("Moves Remaining: " + currentGameManager.getPlayerManager().getMovesRemaining());
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.thread;

import java.util.ArrayList;
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

    }

    public void initGameManagers() {
        gameManager = new GameManager[players.size()];
        for (int i = 0; i < players.size(); i++) {
            gameManager[i] = new GameManager(players.get(i), ui);
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
        //System.out.println("This is the Game Thread");
    }

    public void render() {
        if (!mouseHandler.getMouseDragged()) {
            gameRenderer.repaint();
        }
    }

    public void update() {
        if (currentGameManager != null) {
            if (currentGameManager.isMoveInProgress()) {
                ui.getGameScrollPane().setPannable(false);
                if (currentGameManager.getPlayerManager().getMovesRemaining() != 0) {
                    if (currentGameManager.isScrolling()) {
                        currentGameManager.scrollBack();
                    } else {
                        if (currentGameManager.move()) {
                            
                            boolean removingCard = false;
                            currentGameManager.getPlayerManager().setMovesRemaining(currentGameManager.getPlayerManager().getMovesRemaining() - 1);
                            
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

                                    //stopGameThread();
                                    System.out.println(currentGameManager.getPlayerManager().getPlayerName() + " has won!");
                                }
                            }

                            if (currentGameManager.getPlayerManager().getMovesRemaining() == 0 && ui.isRollButtonDisabled()) {
                                currentGameManager.resetPreviousCity();
                                if (!removingCard) {
                                    ui.getGSM().processIncrementPlayerRequest();
                                    ui.getGSM().processStartTurnRequest();
                                } else {
                                    ui.getGSM().processSetWaitRequest(true);
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
    }

    public void updateRemainingMoves(int moves) {
        currentGameManager.getPlayerManager().setMovesRemaining(currentGameManager.getPlayerManager().getMovesRemaining() + moves);
        ui.updateMovesRemaining("Moves Remaining: " + currentGameManager.getPlayerManager().getMovesRemaining());
        System.out.println("Game Thread - Remaining Moves: " + remainingMoves);
    }
}

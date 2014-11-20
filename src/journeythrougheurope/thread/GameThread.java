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
        mouseHandler = new GameMouseHandler();
        this.ui.getGamePanel().setOnMouseClicked(mouseHandler);
        this.ui.getGamePanel().setOnMouseDragged(mouseHandler);

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
        gameRenderer.repaint();
    }

    public void update() {
        if (currentGameManager != null) {
            if (currentGameManager.isMoveInProgress()) {
                if (currentGameManager.isScrolling()) {
                    currentGameManager.scrollBack();
                } else {
                    currentGameManager.move();
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
    }

    public void updateRemainingMoves(int moves) {
        remainingMoves = moves;
        System.out.println("Game Thread - Remaining Moves: " + remainingMoves);
    }
}

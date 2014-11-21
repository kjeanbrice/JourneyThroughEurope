/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.thread;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import journeythrougheurope.ui.CardMouseHandler;
import journeythrougheurope.ui.JourneyThroughEuropeUI;
import journeythrougheurope.ui.PlayerManager;

/**
 *
 * @author Karl
 */
public class CardThread extends AnimationTimer {

    private final int Y_INCREMENT = 61;
    private final int DEAL_CARD_SPEED = -30;
    private final int MAX_CARDS = 7;

    private JourneyThroughEuropeUI ui;
    private Deck deck;
    private ArrayList<PlayerManager> playersManager;
    private CardManager cardManager[];
    private CardRenderer cardRenderer;
    private CardManager currentCardManager;

    private ImageView[] gameGridImageViews;
    private int yFinalLocation;
    private int currentPlayer;
    private int currentCard;

    private CardMouseHandler cardMouseHandler;
    private boolean dealFirstCard;
    private boolean dealRemainingCards;
    private boolean updatePlayer;

    public CardThread(JourneyThroughEuropeUI ui) {
        this.ui = ui;
        yFinalLocation = 0;
        currentPlayer = -1;
        currentCard = 0;

        dealFirstCard = true;
        dealRemainingCards = true;
        updatePlayer = false;

        deck = ui.getGSM().getDeck();
        playersManager = this.ui.getPlayers();
        cardRenderer = new CardRenderer(this.ui.getCardPanel().getWidth(), this.ui.getCardPanel().getHeight(), this.ui);
        currentCardManager = null;

        gameGridImageViews = this.ui.getGameGridImages();

        setupPlayerHands();
        initCardManagers();

        cardMouseHandler = new CardMouseHandler(cardManager[0].getPlayerManager(), cardRenderer, this.ui.getCardPanel().getWidth());
        ui.getCardPanel().setOnMouseClicked(cardMouseHandler);
    }

    public void initCardManagers() {
        cardManager = new CardManager[playersManager.size()];
        for (int i = 0; i < cardManager.length; i++) {
            cardManager[i] = new CardManager(ui.getCardPanel().getWidth(), ui.getCardPanel().getHeight(), ui, playersManager.get(i));
            cardManager[i].initDefaultCardLocations();
        }
    }

    public void startCardThread() {
        ui.setCardToScreen(cardRenderer);
        start();

        /*Timeline animation = new Timeline();
         KeyFrame temp = new KeyFrame(Duration.millis(1400), new EventHandler<ActionEvent>() {

         public void handle(ActionEvent event) {
         int x = (int) ((Math.random() * 500) + 1);
         int y = (int) ((Math.random() * 400) + 1);
         ui.getGameScrollPane().setHvalue((Math.random()));
         ui.getGameScrollPane().setVvalue((Math.random()));
         System.out.println(ui.getGameScrollPane().getHvalue());
         System.out.println(ui.getGameScrollPane().getVvalue());
         }
         });

         animation.getKeyFrames().add(temp);
         animation.setCycleCount(Timeline.INDEFINITE);
         animation.play();*/
    }

    public void stopCardThread() {
        stop();
    }

    public synchronized void update() {

        if (dealFirstCard) {
            dealFirstCard = dealFirstCard();
            currentCardManager.moveCard(currentCard, DEAL_CARD_SPEED, yFinalLocation);
        } else if (dealRemainingCards) {
            dealRemainingCards = dealRemainingCards();
            currentCardManager.moveCard(currentCard, DEAL_CARD_SPEED, yFinalLocation);
        }

        if (currentCardManager.isScrolling()) {
            currentCardManager.scrollBack();
        }
    }

    public synchronized void render() {
        cardRenderer.repaint(currentCardManager.getPlayerManager(), MAX_CARDS);
    }

    public CardRenderer getCardRenderer() {
        return cardRenderer;
    }

    @Override
    public void handle(long now) {
        if (dealFirstCard || dealRemainingCards || updatePlayer) {

            update();
            render();
            if (!currentCardManager.isScrolling()) {
                updatePlayer = false;
            }
            //TimeUnit.MILLISECONDS.sleep();
        }

        //System.out.println("This is the Card Thread");
    }

    public boolean dealFirstCard() {

        if (currentCardManager == null) {
            nextPlayer(0);
            ui.getEventHandler().respondToChangeGridRequest(currentCardManager.getPlayerManager().getCurrentGridLocation());

            /*
             double x = ui.getGSM().processGetCityRequest(currentCardManager.getPlayerManager().getCurrentCity()).getGridX();
             double gridWidth = gameGridImageViews[currentCardManager.getPlayerManager().getCurrentGridLocation() - 1].getImage().getWidth();

             double y = ui.getGSM().processGetCityRequest(currentCardManager.getPlayerManager().getCurrentCity()).getGridY();
             double gridHeight = gameGridImageViews[currentCardManager.getPlayerManager().getCurrentGridLocation() - 1].getImage().getHeight();

             ui.getGameScrollPane().setHvalue(x / gridWidth);
             ui.getGameScrollPane().setVvalue(y / gridHeight);*/
        }

        if (currentPlayer == cardManager.length) {
            resetCurrentPlayer();
            nextPlayer(1);

            int grid = currentCardManager.getPlayerManager().getCurrentGridLocation();
            ui.getEventHandler().respondToChangeGridRequest(grid);
            yFinalLocation += Y_INCREMENT;
            currentCard++;
            return false;
        } else {
            if (currentCardManager.getPlayerManager().getCardLocations().get(0).getY() == yFinalLocation) {
                if (!currentCardManager.isScrolling()) {
                    nextPlayer(0);
                    ui.getEventHandler().respondToChangeGridRequest(currentCardManager.getPlayerManager().getCurrentGridLocation());
                }
            }
            return true;
        }
    }

    public boolean dealRemainingCards() {

        if (currentPlayer == cardManager.length) {
            resetCurrentPlayer();
            nextPlayer(1);
            ui.getGSM().processStartTurnRequest();
            currentCard = 0;
            yFinalLocation = 0;
            return false;
        }

        //currentCardManager = cardManager[currentPlayer];
        //cardMouseHandler.setPlayer(currentCardManager.getPlayerManager());
        //ui.setCurrentPlayer(currentPlayer);
        if (currentCardManager.getPlayerManager().getCardLocations().get(currentCard).getY() == yFinalLocation) {
            currentCard++;
            yFinalLocation += Y_INCREMENT;
        }

        if (currentCard == MAX_CARDS) {
            nextPlayer(1);
            yFinalLocation = 60;
            currentCard = 1;
        }
        return true;

    }

    public synchronized void setupPlayerHands() {
        for (int i = 0, j = 1; i < playersManager.size(); i++, j++) {
            if (j == 4) {
                j = 1;
            }
            String firstCard = deck.dealCard(j);
            playersManager.get(i).addCard(firstCard);
            playersManager.get(i).setCurrentCity(firstCard);
            playersManager.get(i).setHomeCity(firstCard);
            playersManager.get(i).setCurrentGridLocation(ui.getGSM().processGetCityRequest(firstCard).getGridLocation());
            playersManager.get(i).setHomeGridLocation(ui.getGSM().processGetCityRequest(firstCard).getGridLocation());
            playersManager.get(i).setCurrentPosition(new Point2D(ui.getGSM().processGetCityRequest(firstCard).getGridX(),
                    ui.getGSM().processGetCityRequest(firstCard).getGridY()));
        }

        for (int i = 0; i < playersManager.size(); i++) {
            for (int j = 0; j < MAX_CARDS - 1; j++) {
                playersManager.get(i).addCard(deck.dealCard(0));
            }
            System.out.println(playersManager.get(i).toString() + "\n");
        }
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void nextPlayer(int scroll) {

        currentPlayer++;
        if (currentPlayer < cardManager.length) {
            currentCardManager = cardManager[currentPlayer];
            cardMouseHandler.setPlayer(currentCardManager.getPlayerManager());
            ui.setCurrentPlayer(currentPlayer);
            if (scroll == 0) {

                double hValue = ui.getGameScrollPane().getHvalue();

                currentCardManager.setCurrentGameScrollLocation(ui.getGameScrollPane().getHvalue(), ui.getGameScrollPane().getVvalue());
                currentCardManager.setScrolling(true);
            }
        }
    }

    public void updatePlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        currentCardManager = cardManager[currentPlayer];
        cardMouseHandler.setPlayer(currentCardManager.getPlayerManager());
        ui.setCurrentPlayer(currentPlayer);

        ui.getEventHandler().respondToChangeGridRequest(currentCardManager.getPlayerManager().getCurrentGridLocation());
        currentCardManager.setCurrentGameScrollLocation(ui.getGameScrollPane().getHvalue(), ui.getGameScrollPane().getVvalue());
        currentCardManager.setScrolling(true);
        updatePlayer = true;
    }

    public void resetCurrentPlayer() {
        if (currentPlayer == cardManager.length) {
            currentPlayer = -1;
        }
    }
}

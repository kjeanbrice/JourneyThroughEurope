/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.thread;

import journeythrougheurope.game.Deck;
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
    private final int DEAL_CARD_SPEED = -50;
    private final int REMOVE_CARD_SPEED = -5;
    private final int MAX_CARDS = 6;

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
    private int cardToRemove;

    private CardMouseHandler cardMouseHandler;
    private boolean dealFirstCard;
    private boolean dealRemainingCards;
    private boolean dealCards;
    private boolean updatePlayer;
    private boolean removingCard;
    private boolean isScrolling;

    public CardThread(JourneyThroughEuropeUI ui) {
        this.ui = ui;
        yFinalLocation = 0;
        currentPlayer = -1;
        cardToRemove = -1;
        currentCard = 0;

        dealFirstCard = true;
        dealRemainingCards = true;
        updatePlayer = false;
        removingCard = false;
        dealCards = false;
        isScrolling = false;

        deck = ui.getGSM().getDeck();
        playersManager = this.ui.getPlayers();
        cardRenderer = new CardRenderer(this.ui.getCardPanel().getWidth(), this.ui.getCardPanel().getHeight(), this.ui);
        currentCardManager = null;

        gameGridImageViews = this.ui.getGameGridImages();

        if (playersManager.get(0).getCards().isEmpty()) {
            setupPlayerHands();
        }

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
    }

    public void stopCardThread() {
        stop();
    }

    public synchronized void update() {
        if (dealFirstCard) {
            dealFirstCard = dealFirstCard();
            currentCardManager.moveCardUp(currentCard, DEAL_CARD_SPEED, yFinalLocation);
        } else if (dealRemainingCards) {
            dealRemainingCards = dealRemainingCards();
            currentCardManager.moveCardUp(currentCard, DEAL_CARD_SPEED, yFinalLocation);
        }

        if (isScrolling) {
            isScrolling = currentCardManager.scrollToPlayerLocation();
            if (!isScrolling) {
                ui.getGSM().processStatusOnScrollPaneRequest(true);
            } else {
                ui.disableGridButtons();
                ui.getGSM().processStatusOnScrollPaneRequest(false);
            }
        }

        if (removingCard) {
            removeCard();
        }

        if (dealCards) {
            dealCards = this.dealCards();
            currentCardManager.moveCardUp(currentCard, DEAL_CARD_SPEED, yFinalLocation);
        }
    }

    public synchronized void render() {
        if (cardToRemove != -1) {
            cardRenderer.displayCard(currentCardManager.getPlayerManager(), cardToRemove);
        } else {
            cardRenderer.repaint(currentCardManager.getPlayerManager(), currentCardManager.getPlayerManager().getCards().size());
        }
    }

    public CardRenderer getCardRenderer() {
        return cardRenderer;
    }

    @Override
    public void handle(long now) {
        if (dealFirstCard || dealRemainingCards || updatePlayer || removingCard || dealCards) {

            update();
            render();
            if (!removingCard && !dealCards) {
                if (!currentCardManager.isScrolling() && updatePlayer) {
                    updatePlayer = false;
                    if (currentCardManager.getPlayerManager().isHuman()) {
                        ui.enableRollButton();
                        ui.enableGridButtons();
                        ui.enableSaveButton();
                    }
                }
            }
        }
    }

    public boolean dealFirstCard() {

        if (currentCardManager == null) {
            nextPlayer(0);
            isScrolling = true;
        }

        if (currentPlayer == cardManager.length) {
            resetCurrentPlayer();
            nextPlayer(1);
            yFinalLocation += Y_INCREMENT;
            if (currentCard < currentCardManager.getPlayerManager().getCards().size() - 1) {
                currentCard++;
            }
            return false;
        } else {
            if (currentCardManager.getPlayerManager().getCardLocations().get(0).getY() == yFinalLocation) {
                if (!currentCardManager.isScrolling()) {
                    nextPlayer(0);
                    isScrolling = true;
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

        if (currentCardManager.getPlayerManager().getCardLocations().get(currentCard).getY() == yFinalLocation) {
            currentCard++;
            yFinalLocation += Y_INCREMENT;
        }

        if (currentCard == currentCardManager.getPlayerManager().getCards().size()) {
            nextPlayer(1);
            yFinalLocation = 60;
            currentCard = 1;
        }
        return true;

    }

    public boolean dealCards() {

        if (currentCardManager.getPlayerManager().getCardLocations().get(currentCard).getY() == yFinalLocation) {
            currentCard++;
            yFinalLocation += Y_INCREMENT;
        }

        if (currentCard == currentCardManager.getPlayerManager().getCards().size()) {
            yFinalLocation = 0;
            currentCard = 0;
            if (ui.getGSM().processGetWaitRequest()) {
                ui.getGSM().processSetWaitRequest(false);
                ui.getGSM().processIncrementPlayerRequest();
                ui.getGSM().processStartTurnRequest();

            }
            return false;
        }

        return true;
    }

    public synchronized void setupPlayerHands() {
        for (int i = 0, j = 1; i < playersManager.size(); i++, j++) {
            if (j == 4) {
                j = 1;
            }

            String firstCard = deck.dealCard(j);
            if (firstCard.equalsIgnoreCase("TIRANE")) {
                firstCard = deck.dealCard(j);
            }
            playersManager.get(i).addCard(firstCard);
            playersManager.get(i).setCurrentCity(firstCard);
            playersManager.get(i).setHomeCity(firstCard);
            playersManager.get(i).addToMoveHistory(playersManager.get(i).getPlayerName() + "'s home city is " + firstCard);
            playersManager.get(i).setCurrentGridLocation(ui.getGSM().processGetCityRequest(firstCard).getGridLocation());
            playersManager.get(i).setHomeGridLocation(ui.getGSM().processGetCityRequest(firstCard).getGridLocation());
            playersManager.get(i).setCurrentPosition(new Point2D(ui.getGSM().processGetCityRequest(firstCard).getGridX(),
                    ui.getGSM().processGetCityRequest(firstCard).getGridY()));
        }

        for (int i = 0, k = 0; i < playersManager.size(); i++) {
            for (int j = 1; j < MAX_CARDS; j++) {
                switch (ui.getGSM().processGetCityRequest(playersManager.get(i).getCards().get(j - 1)).getCardColor().toUpperCase().trim()) {
                    case "YELLOW":
                        playersManager.get(i).addCard(deck.dealCard(1));
                        break;
                    case "RED":
                        playersManager.get(i).addCard(deck.dealCard(2));
                        break;
                    case "GREEN":
                        String yellowCard = deck.dealCard(3);
                        if (yellowCard.equalsIgnoreCase("TIRANE")) {
                            System.out.println(yellowCard + " HAS BEEN DEALT!");
                            playersManager.get(i).addCard(deck.dealCard(3));
                        } else {
                            playersManager.get(i).addCard(yellowCard);
                        }
                        break;
                }

            }
            //System.out.println(playersManager.get(i).toString() + "\n");
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
                currentCardManager.setCurrentGameScrollLocation(ui.getGameScrollPane().getHvalue(), ui.getGameScrollPane().getVvalue());
                currentCardManager.setScrolling(true);
            }
        }
    }

    public void updatePlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        currentCardManager = cardManager[this.currentPlayer];
        cardMouseHandler.setPlayer(currentCardManager.getPlayerManager());
        ui.setCurrentPlayer(currentPlayer);

        ui.disableGridButtons();
        currentCardManager.setCurrentGameScrollLocation(ui.getGameScrollPane().getHvalue(), ui.getGameScrollPane().getVvalue());
        currentCardManager.setScrolling(true);
        isScrolling = true;
        updatePlayer = true;
    }

    public void removeCard() {
        if (currentCardManager.getPlayerManager().getCardLocations().get(cardToRemove).getY() == ui.getCardPanel().getHeight()) {
            removingCard = false;
            if (cardToRemove != -1) {
                currentCardManager.getPlayerManager().getCardLocations().remove(cardToRemove);
                currentCardManager.getPlayerManager().getCards().remove(cardToRemove);
                currentCardManager.resetCardLocations();
                cardToRemove = -1;

                if (currentCardManager.getPlayerManager().getCards().isEmpty()) {
                    ui.getGSM().processEndGameRequest();
                } else {
                    dealCards = true;
                }

            }
        } else {
            currentCardManager.moveCardDown(cardToRemove, (-REMOVE_CARD_SPEED), ui.getCardPanel().getHeight());
            removingCard = true;
        }
    }

    public void setRemovingCardStatus(boolean status, int cardIndex) {
        removingCard = status;
        cardToRemove = cardIndex;
    }

    public void resetCurrentPlayer() {
        if (currentPlayer == cardManager.length) {
            currentPlayer = -1;
        }
    }

    public void setStatusOnScrollPane(boolean status) {
        if (status) {
            ui.enableScrollPaneFocus();
        } else {
            ui.disableScrollPaneFocus();
        }
    }
}

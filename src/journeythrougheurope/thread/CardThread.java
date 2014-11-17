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
import javafx.application.Platform;
import journeythrougheurope.ui.JourneyThroughEuropeUI;
import journeythrougheurope.ui.PlayerManager;

/**
 *
 * @author Karl
 */
public class CardThread extends AnimationTimer {

    private final int Y_INCREMENT = 60;
    private final int DEAL_CARD_SPEED = -15;
    private final int MAX_CARDS = 4;

    private JourneyThroughEuropeUI ui;
    private Deck deck;
    private ArrayList<PlayerManager> playersManager;
    private CardManager cardManager[];
    private CardRenderer cardRenderer;
    private CardManager currentCardManager;
    private int yFinalLocation;
    private int currentPlayer;
    private int currentCard;

    private boolean dealFirstCard;
    private boolean dealRemainingCards;

    public CardThread(JourneyThroughEuropeUI ui) {
        this.ui = ui;
        yFinalLocation = 0;
        currentPlayer = 0;
        currentCard = 0;

        dealFirstCard = true;
        dealRemainingCards = true;

        deck = ui.getGSM().getDeck();
        playersManager = this.ui.getPlayers();
        cardRenderer = new CardRenderer(this.ui.getCardPanel().getWidth(), this.ui.getCardPanel().getHeight(), this.ui);
        currentCardManager = null;
        setupPlayerHands();
        initCardManagers();
    }

    public void initCardManagers() {
        cardManager = new CardManager[playersManager.size()];
        for (int i = 0; i < cardManager.length; i++) {
            cardManager[i] = new CardManager(ui.getCardPanel().getWidth(), ui.getCardPanel().getHeight(), ui, playersManager.get(i));
            cardManager[i].initDefaultCardLocations();
        }
    }

    public void startCardThread() {
        start();
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
    }

    public synchronized void render() {
        cardRenderer.repaint(currentCardManager.getPlayerManager(), MAX_CARDS);
    }

    public CardRenderer getCardRenderer() {
        return cardRenderer;
    }

    @Override
    public void handle(long now) {
        update();
        render();
    }

    public boolean dealFirstCard() {

        if (currentPlayer == cardManager.length) {
            currentPlayer = 0;
            currentCardManager = cardManager[currentPlayer];
            ui.setCurrentPlayer(currentPlayer);
            yFinalLocation += Y_INCREMENT;
            currentCard++;
            return false;
        } else {
            currentCardManager = cardManager[currentPlayer];
            ui.setCurrentPlayer(currentPlayer);
            if (currentCardManager.getPlayerManager().getCardLocations().get(0).getY() == yFinalLocation) {
                currentPlayer++;
            }
            return true;
        }
    }

    public boolean dealRemainingCards() {

        if (currentPlayer == cardManager.length) {
            currentPlayer = 0;
            currentCard = 0;
            yFinalLocation = 0;
            currentCardManager = cardManager[currentPlayer];
            ui.setCurrentPlayer(currentPlayer);
            return false;
        }

        currentCardManager = cardManager[currentPlayer];
        ui.setCurrentPlayer(currentPlayer);

        if (currentCardManager.getPlayerManager().getCardLocations().get(currentCard).getY() == yFinalLocation) {
            currentCard++;
            yFinalLocation += Y_INCREMENT;
        }

        if (currentCard == MAX_CARDS) {
            currentPlayer++;
            yFinalLocation = 60;
            currentCard = 1;
        }
        return true;

    }

    public void setupPlayerHands() {
        for (int i = 0, j = 1; i < playersManager.size(); i++, j++) {
            if (j == 4) {
                j = 1;
            }
            playersManager.get(i).addCard(deck.dealCard(j));
        }

        for (int i = 0; i < playersManager.size(); i++) {
            for (int j = 0; j < MAX_CARDS - 1; j++) {
                playersManager.get(i).addCard(deck.dealCard(0));
            }
            System.out.println(i + ": " + playersManager.get(i).getCards().toString());
        }
    }
}

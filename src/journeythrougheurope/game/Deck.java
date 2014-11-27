/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.game;

import java.util.ArrayList;

/**
 *
 * @author Karl
 */
public class Deck {

    private final int RANDOM = 0;
    private final int RED = 1;
    private final int GREEN = 2;
    private final int YELLOW = 3;
    private ArrayList<String> redCities;
    private ArrayList<String> greenCities;
    private ArrayList<String> yellowCities;

    public Deck() {
        redCities = new ArrayList<String>();
        greenCities = new ArrayList<String>();
        yellowCities = new ArrayList<String>();
    }

    public String dealCard(int deckColor) {
        int upperBound = 3;
        if (redCities.isEmpty()) {
            upperBound--;
        }
        if (greenCities.isEmpty()) {
            upperBound--;
        }
        if (yellowCities.isEmpty()) {
            upperBound--;
        }
        if (upperBound == 0) {
            throw new NullPointerException("There are no more cards in this deck");
        }

        int randomNumber;
        if (deckColor == RANDOM) {
            randomNumber = (int) ((Math.random() * upperBound) + 1);
        } else {
            randomNumber = deckColor;
        }
        
        String cardName = "";
        switch (randomNumber) {
            case RED:
                cardName = redCities.remove((int) ((Math.random() * redCities.size())));
                break;
            case GREEN:
                cardName = greenCities.remove((int) ((Math.random() * greenCities.size())));
                break;
            case YELLOW:
                cardName = yellowCities.remove((int) ((Math.random() * yellowCities.size())));
                break;
        }

        return cardName;
    }

    public void setRedDeck(ArrayList<String> redCities) {
        this.redCities = redCities;
    }

    public void setGreenDeck(ArrayList<String> greenCities) {
        this.greenCities = greenCities;
    }

    public void setYellowDeck(ArrayList<String> yellowCities) {
        this.yellowCities = yellowCities;
    }

}

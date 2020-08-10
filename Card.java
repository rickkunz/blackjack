package blackjack;

import java.util.*;

/**
 * This is a class to hold the values, suits, and names of individual cards in a deck.
 * 
 * @author Rick Kunz
 */
public class Card {

    private int value;
    private String suit;
    private String cardName;

    public Card(String s, int value) {
        this.suit = s;
        this.value = value;
    }

    public void initialize() {
        switch (value) {
            case 11:
                this.value = 10;
                this.cardName = String.format("%s of %s", "Jack", suit);
                break;
            case 12:
                this.value = 10;
                this.cardName = String.format("%s of %s", "Queen", suit);
                break;
            case 13:
                this.value = 10;
                this.cardName = String.format("%s of %s", "Jack", suit);
                break;
            case 1:
                this.value = 11;
                this.cardName = String.format("%s of %s", "Ace", suit);
                break;
            default:
                this.value = value;
                this.cardName = String.format("%s of %s", value, suit);
        }
    }

    public int getValue() {
        return value;
    }

    public String getSuit() {
        return suit;
    }

    public String getCardName() {
        return cardName;
    }
}

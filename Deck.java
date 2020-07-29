/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import java.util.*;

/**
 *
 * @author Rick Kunz
 */
public class Deck implements BlackjackConstants {

    private String[] suits;
    private List<Card> currentDeck = new ArrayList<>();
    private int currentCard = 0;
    private Iterator<Card> iterator;

    public Deck() {
        suits = new String[]{SUIT_HEARTS, SUIT_DIAMONDS, SUIT_SPADES, SUIT_CLUBS};
        initialize();
        shuffle();
    }

    public void initialize() {
        for (String s : suits) {
            for (int i = 1; i <= 13; i++) {
                Card card = new Card(s, i);
                card.initialize();
                currentDeck.add(card);
            }
        }
    }

    public Card drawCard() {
        if (iterator == null) {
            iterator = currentDeck.iterator();
        }

        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            System.out.println("No more cards in the deck.");
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    private void shuffle() {
        Collections.shuffle(currentDeck);
    }

}

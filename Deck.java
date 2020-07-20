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
public class Deck {
    String[] suits = {"Hearts", "Diamonds", "Spades", "Clubs"};
    List<Card> currentDeck = new ArrayList<>();
    int currentCard = 0;

    Deck() {
        for (String s : suits) {
            for (int i = 1; i <= 13; i++) {
                Card card = new Card(s, i);
                currentDeck.add(card);
            }
        }
        Collections.shuffle(currentDeck);
    }

    public Card drawCard() {
        return currentDeck.get(currentCard++);
    }
}

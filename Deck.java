package blackjack;

import java.util.*;

/**
 * This class creates a deck of 52 playing cards and includes methods to shuffle the deck and draw the next card.
 * 
 * @author Rick Kunz
 */
public class Deck implements BlackjackConstants {

    private static String[] suits = new String[]{SUIT_HEARTS, SUIT_DIAMONDS, SUIT_SPADES, SUIT_CLUBS};
    private List<Card> currentDeck;
    private int currentCard = 0;
    private Iterator<Card> iterator;

    public Deck() {
        currentDeck = new ArrayList<>();
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

    /**
     * Draw the next card in the deck
     * @return the next card in the deck or an ArrayIndexOutOfBoundsException if there are no more cards remaining.
     */
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

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
public class Card {
    int value;
    String suit;
    String cardName;

    Card(String s, int value) {
        this.suit = s;

        switch (value) {
            case 11:
                this.value = 10;
                this.cardName = "Jack of " + suit;
                break;
            case 12:
                this.value = 10;
                this.cardName = "Queen of " + suit;
                break;
            case 13:
                this.value = 10;
                this.cardName = "King of " + suit;
                break;
            case 1:
                this.value = 11;
                this.cardName = "Ace of " + suit;
                break;
            default:
                this.value = value;
                this.cardName = value + " of " + suit;
        }
    }
}

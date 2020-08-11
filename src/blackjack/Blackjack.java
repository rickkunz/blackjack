package blackjack;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This deck creates the playing area and game logic for the blackjack game.
 * 
 * @author Rick Kunz
 */

public class Blackjack extends JFrame implements ActionListener, BlackjackConstants {

    //Interactable buttons
    private JButton start = new JButton("Start");
    private JButton hit = new JButton("Hit");
    private JButton stay = new JButton("Stay");
    private JButton playAgain = new JButton("Play Again");
    private JButton cashOut = new JButton("Cash out");
    private JButton newGame = new JButton("New game");

    //Player and Dealer Hands
    private List<Card> playerHand = new ArrayList<>();
    private List<Card> dealerHand = new ArrayList<>();

    //Declare variables
    private int playerScore;
    private int dealerScore;
    private int playerMoney = PLAYER_STARTING_MONEY;
    private int playerAces;
    private int dealerAces;
    private Deck deck;

    //Declare variables for upper section
    private JPanel upperPane = new JPanel(new GridLayout(3, 1));
    private JPanel buttonPane = new JPanel();
    private JPanel statusPane = new JPanel();
    private JLabel status = new JLabel("Can you walk away with $1,000? Press start to begin!");
    private JLabel playerLabel = new JLabel(PLAYER);
    private JPanel playerName = new JPanel(new FlowLayout(FlowLayout.LEFT));

    //Set variables for playing cards
    private JPanel cardArea = new JPanel(new GridLayout(2, 1));
    private JPanel playerArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel dealerArea = new JPanel(new FlowLayout(FlowLayout.LEFT));

    //Declare variables for lower section
    private JPanel lowerPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel dealerName = new JPanel();

    //Declare variables for right section
    private JPanel scorePane = new JPanel(new GridLayout(3, 1));
    private JLabel dealerLabel = new JLabel(DEALER);
    private JLabel playerMoneyTracker = new JLabel(PLAYER + "'s Money: $" + playerMoney);
    private JLabel playerScoreTracker = new JLabel(PLAYER + " Score: 0");
    private JLabel dealerScoreTracker = new JLabel(DEALER + " Score: 0");

    //Declare variable for center section upon cashing out
    private JPanel victoryPane = new JPanel();

    public Blackjack() {
        super("Blackjack");
        setSize(1200, 750);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set the upper section
        start.addActionListener(this);
        hit.addActionListener(this);
        stay.addActionListener(this);
        playAgain.addActionListener(this);
        newGame.addActionListener(this);
        cashOut.addActionListener(this);

        buttonPane.add(start);
        buttonPane.add(hit);
        buttonPane.add(stay);
        buttonPane.add(playAgain);
        buttonPane.add(cashOut);
        buttonPane.add(newGame);
        upperPane.add(buttonPane);

        statusPane.add(status);
        upperPane.add(statusPane);

        playerName.add(playerLabel);
        upperPane.add(playerName);

        add(upperPane, BorderLayout.PAGE_START);

        //Set the section for playing cards
        cardArea.add(playerArea);
        cardArea.add(dealerArea);
        add(cardArea, BorderLayout.LINE_START);

        //Set the lower section
        dealerName.add(dealerLabel);
        lowerPane.add(dealerName);
        add(lowerPane, BorderLayout.PAGE_END);

        //Set the right section
        scorePane.add(playerMoneyTracker);
        scorePane.add(playerScoreTracker);
        scorePane.add(dealerScoreTracker);
        add(scorePane, BorderLayout.LINE_END);

        //set Fonts
        playerLabel.setFont(new Font("sansserif", Font.BOLD, LARGE_FONT));
        dealerLabel.setFont(new Font("sansserif", Font.BOLD, LARGE_FONT));
        status.setFont(new Font("sansserif", Font.PLAIN, SMALL_FONT));
        for (Component c : buttonPane.getComponents()) {
            c.setFont(new Font("sansserif", Font.BOLD, MEDIUM_FONT));
        }
        for (Component c : scorePane.getComponents()) {
            c.setFont(new Font("sansserif", Font.PLAIN, SMALL_FONT));
        }

        //Set the initial buttons seen
        setVisible(true);
        hit.setVisible(false);
        stay.setVisible(false);
        playAgain.setVisible(false);
        cashOut.setVisible(false);
        newGame.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();

        if (source == start) {
            start.setVisible(false);
            newRound();
        }

        if (source == hit) {
            hit();
        }

        if (source == stay) {
            stay();
        }

        if (source == playAgain) {
            newRound();
        }

        if (source == newGame) {
            playerMoney = PLAYER_STARTING_MONEY;
            playerMoneyTracker.setText(PLAYER + "'s Money: $" + playerMoney);
            newGame.setVisible(false);
            newRound();
        }

        if (source == cashOut) {
            cashOut();
        }

        updateStatus();
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(
                    "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hit() {
        playerHand.add(deck.drawCard());
        playerScore += playerHand.get((playerHand.size()) - 1).getValue();

        if (playerHand.get(playerHand.size() - 1).getValue() == DEFAULT_ACE_VALUE) {
            playerAces++;
        }

        displayCard(playerHand.get((playerHand.size()) - 1).getCardName(), PLAYER);
        if (playerScore > TARGET_SCORE) {
            if (playerAces > 0) {
                playerScore -= ACE_DECREMENT;
                playerAces--;
            } else {
                hit.setVisible(false);
                stay.setVisible(false);

                status.setText("You bust with a score of " + playerScore + ". Better luck next time!");
                playerMoney -= BET_AMOUNT;

                formatMoneyTracker();

                playAgain.setVisible(true);
            }
        }

        playerScoreTracker.setText(PLAYER + " Score: " + playerScore);

        if (playerHand.size() == FIVE_CARD_CHARLIE & playerScore <= TARGET_SCORE) {
            status.setText("Five Card Charlie! You win!");
            playerMoney += BET_AMOUNT;

            formatMoneyTracker();

            hit.setVisible(false);
            stay.setVisible(false);
            playAgain.setVisible(true);
        } else if (playerScore == TARGET_SCORE) {
            stay();
        }
    }

    private void stay() {
        dealerDraws();

        compareScores();

        hit.setVisible(false);
        stay.setVisible(false);

        if (playerMoney > 0) {
            playAgain.setVisible(true);
        }
    }

    private void newRound() {
        reset();
        drawOpeningHands();
        checkInitialScores();
    }

    private void cashOut() {
        Formatter moneyFormatted = new Formatter();
        moneyFormatted.format("%,d", playerMoney);
        status.setText("Congratulations! You walked away with $" + moneyFormatted + ".");

        playAgain.setVisible(false);
        cashOut.setVisible(false);

        cardArea.removeAll();
        lowerPane.removeAll();
        scorePane.removeAll();
        upperPane.remove(playerName);

        try {
            BufferedImage newCard = ImageIO.read(new File("src/images/Victory.png"));
            JLabel picLabel = new JLabel(new ImageIcon(newCard));
            victoryPane.add(picLabel);
            add(victoryPane, BorderLayout.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayBack() {
        try {
            BufferedImage newCard = ImageIO.read(new File("src/images/CardBack.png"));
            JLabel picLabel = new JLabel(new ImageIcon(newCard));
            dealerArea.add(picLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayCard(String cardName, String cardLocation) {
        try {
            String nameOfFile = "src/images/" + cardName + ".png";
            BufferedImage newCard = ImageIO.read(new File(nameOfFile));
            JLabel picLabel = new JLabel(new ImageIcon(newCard));

            if (cardLocation.equals(PLAYER)) {
                playerArea.add(picLabel);
            } else if (cardLocation.equals(DEALER)) {
                dealerArea.add(picLabel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateStatus() {
        if (playerMoney >= CASH_OUT_THRESHOLD & playAgain.isVisible() == true) {
            cashOut.setVisible(true);
        } else if (playerMoney < NEW_GAME_THRESHOLD) {
            status.setText("Tough luck. Press \"New Game\" to try again.");
            newGame.setVisible(true);
            playAgain.setVisible(false);
        }
        revalidate();
        repaint();
    }

    private void reset() {
        playerScore = 0;
        dealerScore = 0;
        playerAces = 0;
        dealerAces = 0;
        playAgain.setVisible(false);
        cashOut.setVisible(false);
        status.setText("Press \"Hit\" to draw again or \"Stay\" to stop.");
        playerHand.clear();
        dealerHand.clear();
        playerArea.removeAll();
        dealerArea.removeAll();
    }

    private void drawOpeningHands() {
        deck = new Deck();
        playerHand.add(deck.drawCard());
        dealerHand.add(deck.drawCard());
        playerHand.add(deck.drawCard());
        dealerHand.add(deck.drawCard());

        for (Card pH : playerHand) {
            playerScore += pH.getValue();
            if (pH.getValue() == DEFAULT_ACE_VALUE) {
                playerAces++;
            }

            displayCard(pH.getCardName(), PLAYER);

            if (playerAces == 2) {
                playerScore = 12;
                playerAces--;
            }
        }

        for (Card dH : dealerHand) {
            dealerScore += dH.getValue();
            if (dH.getValue() == DEFAULT_ACE_VALUE) {
                dealerAces++;
            }
        }

        displayCard(dealerHand.get(0).getCardName(), DEALER);
        displayBack();
    }

    private void checkInitialScores() {
        playerScoreTracker.setText(PLAYER + " Score: " + playerScore);
        dealerScoreTracker.setText(DEALER + " Score: Between " + (dealerHand.get(0).getValue() + 1)
                + " and " + (dealerHand.get(0).getValue() + DEFAULT_ACE_VALUE) + " ");

        if (playerScore == TARGET_SCORE & dealerScore != TARGET_SCORE) {
            status.setText("Blackjack! You win $150.");
            playerMoney += BLACKJACK_AMOUNT;

            formatMoneyTracker();

            hit.setVisible(false);
            stay.setVisible(false);
            playAgain.setVisible(true);
        } else if (playerScore == TARGET_SCORE) {
            stay();
        } else {
            hit.setVisible(true);
            stay.setVisible(true);
        }
    }

    private void formatMoneyTracker() {
        Formatter moneyFormatted = new Formatter();
        moneyFormatted.format("%,d", playerMoney);
        playerMoneyTracker.setText(PLAYER + "'s Money: $" + moneyFormatted);
    }

    private void dealerDraws() {
        dealerArea.removeAll();

        while (dealerScore < DEALER_MIN_STOP || (dealerScore > TARGET_SCORE && dealerAces > 0)) {
            dealerHand.add(deck.drawCard());

            if (dealerHand.get(dealerHand.size() - 1).getValue() == DEFAULT_ACE_VALUE) {
                dealerAces++;
            }

            if (dealerScore > TARGET_SCORE && dealerAces > 0) {
                dealerScore -= ACE_DECREMENT;
                dealerAces--;
            }

            dealerScore += dealerHand.get((dealerHand.size()) - 1).getValue();
        }

        dealerScoreTracker.setText(DEALER + " Score: " + dealerScore);

        for (Card dH : dealerHand) {
            displayCard(dH.getCardName(), DEALER);
        }
    }

    private void compareScores() {
        if (dealerScore > TARGET_SCORE) {
            status.setText(DEALER + " busts with a score of " + dealerScore + ". You win!");
            playerMoney += BET_AMOUNT;
        } else if (playerScore > dealerScore) {
            status.setText("You win, " + playerScore + " to " + dealerScore + "!");
            playerMoney += BET_AMOUNT;
        } else if (playerScore < dealerScore) {
            status.setText("You lose this round, " + playerScore + " to " + dealerScore + ". Better luck next time.");
            playerMoney -= BET_AMOUNT;
        } else {
            status.setText("It's a push, " + playerScore + " to " + dealerScore + ".");
        }

        formatMoneyTracker();
    }

    public static void main(String[] args) {
        setLookAndFeel();
        Blackjack bj = new Blackjack();
    }
}

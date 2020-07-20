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
 * @author Rick Kunz
 */
public class Blackjack extends JFrame implements ActionListener {

    //Interactable buttons
    JButton start = new JButton("Start");
    JButton hit = new JButton("Hit");
    JButton stay = new JButton("Stay");
    JButton playAgain = new JButton("Play Again");
    JButton cashOut = new JButton("Cash out");
    JButton newGame = new JButton("New game");

    //Player and Dealer Hands
    List<Card> playerHand = new ArrayList<>();
    List<Card> dealerHand = new ArrayList<>();

    //Declare variables
    int playerScore;
    int dealerScore;
    int playerMoney = 500;
    int playerAces;
    int dealerAces;

    //Initialize deck
    Deck deck = new Deck();

    //Declare variables for upper section
    JPanel upperPane = new JPanel(new GridLayout(3, 1));
    JPanel buttonPane = new JPanel();
    JPanel statusPane = new JPanel();
    JLabel status = new JLabel("Can you walk away with $1,000? Press start to begin!");
    JLabel playerLabel = new JLabel("Player");
    JPanel playerName = new JPanel(new FlowLayout(FlowLayout.LEFT));

    //Set variables for playing cards
    JPanel cardArea = new JPanel(new GridLayout(2, 1));
    JPanel playerArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JPanel dealerArea = new JPanel(new FlowLayout(FlowLayout.LEFT));

    //Declare variables for lower section
    JPanel lowerPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JPanel dealerName = new JPanel();

    //Declare variables for right section
    JPanel scorePane = new JPanel(new GridLayout(3, 1));
    JLabel dealerLabel = new JLabel("Dealer");
    JLabel playerMoneyTracker = new JLabel("Player's Money: $" + playerMoney);
    JLabel playerScoreTracker = new JLabel("Player Score: 0");
    JLabel dealerScoreTracker = new JLabel("Dealer Score: 0");

    //Declare variable for center section upon cashing out
    JPanel victoryPane = new JPanel();

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
        playerLabel.setFont(new Font("sansserif", Font.BOLD, 42));
        dealerLabel.setFont(new Font("sansserif", Font.BOLD, 42));
        status.setFont(new Font("sansserif", Font.PLAIN, 20));
        for (Component c : buttonPane.getComponents()) {
            c.setFont(new Font("sansserif", Font.BOLD, 32));
        }
        for (Component c : scorePane.getComponents()) {
            c.setFont(new Font("sansserif", Font.PLAIN, 20));
        }

        //Set the initial buttons seen
        setVisible(true);
        hit.setVisible(false);
        stay.setVisible(false);
        playAgain.setVisible(false);
        cashOut.setVisible(false);
        newGame.setVisible(false);

        //Draw the initial hands
        playerHand.add(deck.drawCard());
        dealerHand.add(deck.drawCard());
        playerHand.add(deck.drawCard());
        dealerHand.add(deck.drawCard());
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
            playerMoney = 500;
            playerMoneyTracker.setText("Player's Money: $" + playerMoney);
            newGame.setVisible(false);
            newRound();
        }

        if (source == cashOut) {
            cashOut();
        }

        if (playerMoney >= 1000 & playAgain.isVisible() == true) {
            cashOut.setVisible(true);
        } else if (playerMoney < 100) {
            status.setText("Tough luck. Press \"New Game\" to try again.");
            newGame.setVisible(true);
            playAgain.setVisible(false);
        }
        revalidate();
        repaint();
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(
                    "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void hit() {
        playerHand.add(deck.drawCard());
        playerScore += playerHand.get((playerHand.size()) - 1).value;

        if (playerHand.get(playerHand.size() - 1).value == 11) {
            playerAces++;
        }

        displayCard(playerHand.get((playerHand.size()) - 1).cardName, "player");
        if (playerScore > 21) {
            if (playerAces > 0) {
                playerScore -= 10;
                playerAces--;
            } else {
                hit.setVisible(false);
                stay.setVisible(false);

                status.setText("You bust with a score of " + playerScore + ". Better luck next time!");
                playerMoney -= 100;

                Formatter moneyFormatted = new Formatter();
                moneyFormatted.format("%,d", playerMoney);
                playerMoneyTracker.setText("Player's Money: $" + moneyFormatted);

                System.out.println("");

                playAgain.setVisible(true);
            }
        }

        playerScoreTracker.setText("Player Score: " + playerScore);

        if (playerHand.size() == 5 & playerScore <= 21) {
            status.setText("Five Card Charlie! You win!");
            playerMoney += 100;

            Formatter moneyFormatted = new Formatter();
            moneyFormatted.format("%,d", playerMoney);
            playerMoneyTracker.setText("Player's Money: $" + moneyFormatted);

            hit.setVisible(false);
            stay.setVisible(false);
            playAgain.setVisible(true);
        } else if (playerScore == 21) {
            stay();
        }
    }

    void stay() {
        dealerArea.removeAll();

        while (dealerScore < 17 || (dealerScore > 21 && dealerAces > 0)) {
            dealerHand.add(deck.drawCard());

            if (dealerHand.get(dealerHand.size() - 1).value == 11) {
                dealerAces++;
            }

            if (dealerScore > 21 && dealerAces > 0) {
                dealerScore -= 10;
                dealerAces--;
            }

            dealerScore += dealerHand.get((dealerHand.size()) - 1).value;
        }

        for (Card dH : dealerHand) {
            displayCard(dH.cardName, "dealer");
        }

        dealerScoreTracker.setText("Dealer Score: " + dealerScore);

        if (dealerScore > 21 | playerScore > dealerScore) {
            playerMoney += 100;
        } else if (playerScore < dealerScore) {
            playerMoney -= 100;
        }

        Formatter moneyFormatted = new Formatter();
        moneyFormatted.format("%,d", playerMoney);
        playerMoneyTracker.setText("Player's Money: $" + moneyFormatted);

        if (dealerScore > 21) {
            status.setText("Dealer busts with a score of " + dealerScore + ". You win!");
        } else if (playerScore > dealerScore) {
            status.setText("You win, " + playerScore + " to " + dealerScore + "!");
        } else if (playerScore < dealerScore) {
            status.setText("You lose this round, " + playerScore + " to " + dealerScore + ". Better luck next time.");
        } else {
            status.setText("It's a push, " + playerScore + " to " + dealerScore + ".");
        }

        hit.setVisible(false);
        stay.setVisible(false);

        if (playerMoney > 0) {
            playAgain.setVisible(true);
        }
    }

    void newRound() {
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

        deck = new Deck();
        playerHand.add(deck.drawCard());
        dealerHand.add(deck.drawCard());
        playerHand.add(deck.drawCard());
        dealerHand.add(deck.drawCard());

        for (Card pH : playerHand) {
            playerScore += pH.value;
            if (pH.value == 11) {
                playerAces++;
            }

            displayCard(pH.cardName, "player");

            if (playerAces == 2) {
                playerScore = 12;
                playerAces--;
            }
        }

        for (Card dH : dealerHand) {
            dealerScore += dH.value;
            if (dH.value == 11) {
                dealerAces++;
            }
        }

        displayCard(dealerHand.get(0).cardName, "dealer");
        displayBack();

        playerScoreTracker.setText("Player Score: " + playerScore);
        dealerScoreTracker.setText("Dealer Score: Between " + (dealerHand.get(0).value + 1)
                + " and " + (dealerHand.get(0).value + 11) + " ");

        if (playerScore == 21 & dealerScore != 21) {
            status.setText("Blackjack! You win $150.");
            playerMoney += 150;

            Formatter moneyFormatted = new Formatter();
            moneyFormatted.format("%,d", playerMoney);
            playerMoneyTracker.setText("Player's Money: $" + moneyFormatted);

            hit.setVisible(false);
            stay.setVisible(false);
            playAgain.setVisible(true);
        } else if (playerScore == 21) {
            stay();
        } else {
            hit.setVisible(true);
            stay.setVisible(true);
        }
    }

    void cashOut() {
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

    void displayBack() {
        try {
            BufferedImage newCard = ImageIO.read(new File("src/images/CardBack.png"));
            JLabel picLabel = new JLabel(new ImageIcon(newCard));
            dealerArea.add(picLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void displayCard(String cardName, String cardLocation) {
        try {
            String nameOfFile = "src/images/" + cardName + ".png";
            BufferedImage newCard = ImageIO.read(new File(nameOfFile));
            JLabel picLabel = new JLabel(new ImageIcon(newCard));

            if (cardLocation.equals("player")) {
                playerArea.add(picLabel);
            } else if (cardLocation.equals("dealer")) {
                dealerArea.add(picLabel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        setLookAndFeel();
        Blackjack bj = new Blackjack();
    }
}
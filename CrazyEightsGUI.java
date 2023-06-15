import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CrazyEightsGUI extends JFrame {

    // Game variables
    private Deck deck;
    private List<Card> discardPile;
    private List<Player> players;
    private Player currentPlayer;
    private Card currentCard;
    private String currentSuit;
    private boolean isEightPlayed;

    // UI components
    private JPanel playerPanel;
    private JPanel controlPanel;
    private JLabel currentPlayerLabel;
    private JLabel currentSuitLabel;
    private JLabel currentCardImageLabel;
    private JButton drawButton;
    private JButton playButton;
    private List<JButton> cardButtons;
    private JButton selectedButton; // Reference to the currently selected card button
    private boolean hasSelectedCard; // Flag to track whether a card has been selected

    private JPanel endScreen;
    private JLabel winnerLabel;

    public CrazyEightsGUI() {
        // Initialize game variables
        deck = new Deck();
        discardPile = new ArrayList<>();
        players = new ArrayList<>();
        players.add(new Player("Player 1"));
        players.add(new Player("Player 2"));
        currentPlayer = players.get(0);

        // Setup the UI
        setupUI();

        // Start the game
        startGame();
    }

    private void setupUI() {
        // Set up the main frame
        setTitle("Crazy Eights");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0, 128, 0)); // Dark Green

        // Create UI components
        playerPanel = new JPanel();
        controlPanel = new JPanel();
        currentPlayerLabel = new JLabel();
        currentSuitLabel = new JLabel();
        currentCardImageLabel = new JLabel();
        drawButton = new JButton("Draw");
        playButton = new JButton("Play");

        // Set the style for the UI components
        currentSuitLabel.setForeground(Color.WHITE);
        currentSuitLabel.setFont(new Font("Arial", Font.BOLD, 20));

        currentPlayerLabel.setForeground(Color.WHITE);
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Add action listeners to buttons
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawCard();
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playCard();
            }
        });

        // Set the layout for the player panel
        playerPanel.setLayout(new GridLayout(1, 0));

        // Add components to the control panel
        controlPanel.add(drawButton);
        controlPanel.add(playButton);

        // Add components to the main frame
        add(currentSuitLabel, BorderLayout.EAST);
        add(playerPanel, BorderLayout.NORTH);
        add(currentPlayerLabel, BorderLayout.WEST);
        add(currentCardImageLabel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Disable the "Play" button initially
        playButton.setEnabled(false);

        // Configure the frame
        pack();
        setSize(900, 750);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
    }

    private void startGame() {
        // Shuffle the deck and deal cards to players
        deck.shuffle();
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                Card card = deck.drawCard();
                player.addCardToHand(card);
            }
        }

        // Start the game with the first card from the deck
        currentCard = deck.drawCard();
        discardPile.add(currentCard);
        currentSuit = currentCard.getSuit();
        isEightPlayed = false;

        // Update the UI
        updateUI();
    }

    private void updateUI() {
        // Update the current player label
        currentPlayerLabel.setText("Current Player: " + currentPlayer.getName());

        // Set the current card image
        ImageIcon cardImageIcon = currentCard.getImageIcon();
        Image scaledCardImage = cardImageIcon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
        currentCardImageLabel.setIcon(new ImageIcon(scaledCardImage));
        currentCardImageLabel.setHorizontalAlignment(JLabel.CENTER);

        // Remove all card buttons from the player panel
        playerPanel.removeAll();

        // Create new card buttons for the current player's hand
        cardButtons = new ArrayList<>();
        for (Card card : currentPlayer.getHand()) {
            JButton cardButton = new JButton();
            ImageIcon cardButtonIcon = card.getImageIcon();
            Image scaledCardButtonImage = cardButtonIcon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
            cardButton.setIcon(new ImageIcon(scaledCardButtonImage));
            cardButton.addActionListener(new CardButtonListener(card));
            cardButtons.add(cardButton);
            playerPanel.add(cardButton);
        }

        // Repaint the player panel to update the UI
        playerPanel.revalidate();
        playerPanel.repaint();

        // Highlight the selected card button with a yellow glow
        for (JButton cardButton : cardButtons) {
            cardButton.setBorder(null);
            if (cardButton.getActionListeners().length > 0) {
                CardButtonListener listener = (CardButtonListener) cardButton.getActionListeners()[0];
                if (listener.getCard() == currentPlayer.getSelectedCard()) {
                    cardButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                    selectedButton = cardButton;
                }
            }
        }

        // Enable or disable the "Play" button based on whether a card is selected
        playButton.setEnabled(hasSelectedCard);
    }

    private void drawCard() {
        Card card = deck.drawCard();
        currentPlayer.addCardToHand(card);

        // Show the drawn card
        JOptionPane.showMessageDialog(this, "You drew: " + card.toString());

        nextTurn();
    }

    private void playCard() {
        if (hasSelectedCard) {
            Card selectedCard = currentPlayer.getSelectedCard();
            if (isValidPlay(selectedCard) || selectedCard.getRank().equals("8")) {
                currentPlayer.removeCardFromHand(selectedCard);
                discardPile.add(selectedCard);
                currentCard = selectedCard;
                currentSuit = currentCard.getSuit();
                isEightPlayed = selectedCard.getRank().equals("8");

                if (isEightPlayed) {
                    Object[] suits = { "Hearts", "Diamonds", "Clubs", "Spades" };
                    Object selectedSuit = JOptionPane.showInputDialog(this, "Choose a suit for the next turn:",
                            "Eight Played", JOptionPane.PLAIN_MESSAGE, null, suits, suits[0]);
                    currentSuit = selectedSuit.toString();
                }

                if (currentPlayer.getHand().size() == 0) {
                    displayWinner();
                } else {
                    nextTurn();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid card play! Please select a valid card.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a card to play.");
        }
    }

    private boolean isValidPlay(Card card) {
        if (isEightPlayed) {
            return card.getSuit().equals(currentSuit);
        } else {
            return card.getSuit().equals(currentSuit) || card.getRank().equals(currentCard.getRank());
        }
    }

    private void nextTurn() {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.setSelectedCard(null);
        hasSelectedCard = false;
        updateUI();
    }

    private void displayWinner() {
        // Hide the game UI
        getContentPane().removeAll();
        revalidate();

        // Create the end screen panel
        endScreen = new JPanel();
        endScreen.setLayout(new BorderLayout());
        endScreen.setBackground(new Color(0, 128, 0)); // Dark Green

        // Determine the winner
        Player winner = players.get(0);
        for (Player player : players) {
            if (player.getHand().size() < winner.getHand().size()) {
                winner = player;
            }
        }

        // Create the winner label
        winnerLabel = new JLabel("Winner: " + winner.getName());
        winnerLabel.setForeground(Color.WHITE);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        winnerLabel.setHorizontalAlignment(JLabel.CENTER);
        endScreen.add(winnerLabel, BorderLayout.CENTER);

        // Add the end screen panel to the frame
        add(endScreen);

        pack();
        setSize(900, 750);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
    }

    private class CardButtonListener implements ActionListener {
        private Card card;

        public CardButtonListener(Card card) {
            this.card = card;
        }

        public Card getCard() {
            return card;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            currentPlayer.setSelectedCard(card);

            // Highlight the selected card button with a yellow glow
            JButton selectedButton = (JButton) e.getSource();
            selectedButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));

            // Remove the yellow glow from other card buttons
            for (JButton cardButton : cardButtons) {
                if (cardButton != selectedButton) {
                    cardButton.setBorder(null);
                }
            }

            // Store the reference to the selected button
            selectedButton = selectedButton;

            // Enable or disable the "Play" button based on whether a card is selected
            hasSelectedCard = true;
            playButton.setEnabled(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CrazyEightsGUI();
            }
        });
    }
}
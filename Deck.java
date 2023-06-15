import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// A class representing a deck of playing cards
public class Deck {
    private List<Card> cards; // A list to store the cards in the deck

    // Constructor for creating a new deck
    public Deck() {
        cards = new ArrayList<>(); // Initialize the list of cards as an empty ArrayList
        initializeDeck(); // Call the private method to initialize the deck with all the cards
    }

    // Private method to initialize the deck with all the cards
    private void initializeDeck() {
        String[] ranks = { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };
        String[] suits = { "Clubs", "Diamonds", "Hearts", "Spades" };

        // Iterate over each suit
        for (String suit : suits) {
            // Iterate over each rank
            for (String rank : ranks) {
                String imagePath = "Images/" + rank + "_of_" + suit + ".png"; // Generate the image path for the card
                Card card = new Card(rank, suit, imagePath); // Create a new card object
                cards.add(card); // Add the card to the deck
            }
        }
    }

    // Method to shuffle the deck
    public void shuffle() {
        Collections.shuffle(cards); // Use the Collections.shuffle() method to randomize the order of the cards
    }

    // Method to draw a card from the deck
    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No cards left in the deck."); // Throw an exception if the deck is empty
        }
        return cards.remove(0); // Remove and return the card at the top of the deck (index 0)
    }
}
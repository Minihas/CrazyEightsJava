import java.util.ArrayList;
import java.util.List;

// A class representing a player in a card game
public class Player {
    private String name; // The name of the player
    private List<Card> hand; // A list to store the player's cards in hand
    private Card selectedCard; // The card selected by the player

    // Constructor for creating a new player with the specified name
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>(); // Initialize the list of cards in hand as an empty ArrayList
    }

    // Returns the name of the player
    public String getName() {
        return name;
    }

    // Returns the list of cards in the player's hand
    public List<Card> getHand() {
        return hand;
    }

    // Returns the selected card of the player
    public Card getSelectedCard() {
        return selectedCard;
    }

    // Sets the selected card for the player
    public void setSelectedCard(Card card) {
        selectedCard = card;
    }

    // Adds a card to the player's hand
    public void addCardToHand(Card card) {
        hand.add(card);
    }

    // Removes a card from the player's hand
    public void removeCardFromHand(Card card) {
        hand.remove(card);
    }
}

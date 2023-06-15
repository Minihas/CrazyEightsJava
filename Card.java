import javax.swing.ImageIcon;

// A class representing a playing card
public class Card {
    private String rank; // The rank of the card (e.g., "Ace", "2", "King")
    private String suit; // The suit of the card (e.g., "Spades", "Hearts", "Diamonds", "Clubs")
    private ImageIcon imageIcon; // An image icon representing the card

    // Constructor for creating a new card with the specified rank, suit, and image
    // path
    public Card(String rank, String suit, String imagePath) {
        this.rank = rank;
        this.suit = suit;
        this.imageIcon = new ImageIcon(imagePath);
    }

    // Returns the rank of the card
    public String getRank() {
        return rank;
    }

    // Returns the suit of the card
    public String getSuit() {
        return suit;
    }

    // Returns the image icon representing the card
    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    // Overrides the toString() method to provide a string representation of the
    // card
    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
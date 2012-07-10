import java.util.*;

/**
 * Players hand of cards in a game of Blackjack. Initial size is two cards, and when the
 * player hits, a card will be added to the hand. The hand keeps track of the value the
 * hand is worth, as well as determining whether the hand contains an ace, and how many
 * it contains. Allows synchronized access to the underlying list via synchronization
 * blocks used on the underlying lists lock
 */

public class Hand{
	private final List<Card> hand; //underlying list which represents the hand
	
	/**
	 * Constructor for a hand of cards
	 * @param List<Card>
	 */
	public Hand(List<Card> hand){
		this.hand = hand;
	}
	
	//Creates a new empty hand
	public static Hand newHand(){
		return new Hand(new ArrayList<Card>());
	}
	
	/**
	 * Adds a card to the hand
	 * @param {@code Card}
	 */
	public void addCard(Card c){
		synchronized(hand){
			hand.add(c);
		}
	}
	
	/**
	 * Returns the value of a hand by simply adding each cards value
	 * @return {@code int}
	 */
	public int getHandValue(){
		int val = 0;
		synchronized(hand){
			for(Card c : hand)
				val += c.cardValue();
		}
		return val;
	}
	
	/**
	 * Creates a String representation of a hand of cards, the format is
	 * Cards:
	 * Then the toString of each card in the hand on one line
	 * @return {@code String}
	 */
	@Override public String toString(){
		List<Card> snapshot = hand;
		StringBuilder sb = new StringBuilder();
		for(Card c : snapshot)
			sb.append(c);
		return sb.toString();
	}
	
	/**
	 * Clears out the hand, used when a player busts(goes over 21)
	 */
	public void bust(){
		synchronized(hand){
			hand.clear();
		}
	}
	
	/**
	 * Determines whether there is an Ace in the hand
	 * @return {@code boolean}
	 */
	public boolean hasAce(){
		synchronized(hand){
			for(Card c : hand)
				if(c.isAce())
					return true;
			return false;
		}
	}
	
	/**
	 * Determines how many aces can be found in the hand
	 * @return {@code int}
	 */
	public int countAces(){
		synchronized(hand){
			int count = 0;
			for(Card c : hand)
				if(c.isAce())
					count++;
			return count;
		}
	}
	
	//Shows the first card in the hand
	public Card viewFirst(){
		synchronized(hand){
			return hand.get(0);
		}
	}
	
	public static void main(String[] args){
		List<Card> cards = new ArrayList<Card>();
		for(int i = 0; i < 8; ++i)
			cards.add(Card.valueOf(i));
		Hand h = new Hand(cards);
		System.out.println("Has Aces: " + h.hasAce());
		System.out.println("Number of Aces: " + h.countAces());
		System.out.println(h);
		System.out.println("Hand Value: " + h.getHandValue());
		for(int i = 0; i < 3; ++i)
			h.addCard(Card.valueOf(i * 4));
		System.out.println("Has Aces: " + h.hasAce());
		System.out.println("Number of Aces: " + h.countAces());
		System.out.println(h);
		System.out.println("Hand Value: " + h.getHandValue());
	}
}
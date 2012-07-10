import java.util.*;

/**
 * @author Kenny Hegeland
 * A representation of a deck of cards, a deck of cards has 52
 * individual cards. Each card should be unique from the previous
 * card. There will be Ace - King of each suit. There are 13 ranks
 * per card, and 4 suits
 */
 
 public final class Deck{
	private List<Card> deck;
	
	/**
	 * Private constructor to create a deck of cards
	 */
	private Deck(){
		deck = new ArrayList<Card>();
		for(int i = 0; i < 52; ++i)
			deck.add(Card.valueOf(i));
	}
	
	/**
	 * Static factory method to create a deck of cards
	 * @return {@code Deck}
	 */
	public static Deck createDeck(){
		Deck d = new Deck();
		for(int i = 0; i < 3; ++i)
			Collections.shuffle(d.deck);
		return d;
	}
	
	/**
	 * Removes the first card from a deck of cards
	 * @return {@code Card}
	 */
	public Card removeNext(){
		synchronized(deck){
			Card c = deck.remove(0);
			return c;
		}
	}
	
	/**
	 * Determines whether the deck is empty or not
	 */
	public boolean empty(){
		synchronized(deck){
			return deck.isEmpty();
		}
	}
	
	/**
	 * Returns a representation of a deck of cards. A deck of cards is 
	 * "Cards:"
	 * Then each Card in the deck on an indicidual line
	 * @return {@code String}
	 */
	@Override public String toString(){
		StringBuilder sb = new StringBuilder();
		synchronized(deck){
			sb.append("Cards:\n");
			for(Card c : deck)
				sb.append(c);
			return sb.toString();
		}
	}
	
	//Size of the deck of cards
	public int deckSize(){
		synchronized(deck){
			return deck.size();
		}
	}	
	
	//Shuffles a deck of cards 
	public void shuffleDeck(){
		Collections.shuffle(deck);
	}
	
	public static void main(String[] args){
		Deck d = Deck.createDeck();
		System.out.println(d);
		System.out.println(d.deckSize());
		
		List<Card> hand = new ArrayList<Card>();
		for(int i = 0; i < 10; ++i)
			hand.add(d.removeNext());
		for(Card c : hand)
			System.out.println(c);
		
	}
}
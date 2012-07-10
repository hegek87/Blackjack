import java.util.*;

/**
 *  Card is an immutable class, it represents one unique instance of a
 *  card in a deck of cards. It has methods to get the value of the
 *  card.
 *  @Immutable
 */
public final class Card{
	
	private enum Rank{
		//ACE has a special value, the default is one, but if the value being added to ace is < 11, ACE is an 11
		ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7),
		EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10);
		
		private final int value; //represents the value of some card
		/**
		 * Sets the value of a rank
		 * @param {@code int}
		 */
		Rank(int value){
			this.value = value;
		}
	}
	private enum Suit{
		CLUBS, HEARTS, DIAMONDS, SPADES;
	}
	
	private final Rank cardRank; //The rank of the card
	private final Suit cardSuit; //The suit of the card
	
	/**
	 * valueOf -- static factory, creates a card by using the private
	 * constructor to initialize a card based on an {@code int}
	 * @return {@code Card}
	 * @param {@code int}
	 */
	public static Card valueOf(int num){
		int suitNum = num % 4;
		int rankNum = num % 13;
		Rank[] ranks = Rank.values();
		Suit[] suits = Suit.values();
		Card c = new Card(ranks[rankNum], suits[suitNum]);
		return c;
	}
	
	/**
	 * Constructor for creating a Card object. Uses a Rank and Suit to
	 * initialize the number. Private constructor, it is only used in 
	 * the static factory method.
	 */
	private Card(Rank cardRank, Suit cardSuit){
		this.cardRank = cardRank;
		this.cardSuit = cardSuit;
	}
	
	/**
	 * Overridden equals -- uses the design established in Effective
	 * Jave, first tests whether the argument is identical to this, 
	 * if it is, it can obviously return true
	 * {@code String s = "h"; String b = s; s.equals(b) == true}
	 * next the code tasts if the object is actually a Card, it it
	 * isn't, it's easy to realize that the two items being compared
	 * cannot be equal. A String will never be equal to an Integer.
	 * Since we previously tested the type of o, it's safe to case o
	 * to type Card, and give a reference to it. Finally, compare the
	 * enum constants of the card, only is both the rank and suit of
	 * this is equal to the rank and suit of o(or c), will true be 
	 * returned
	 * @return {@code boolean} -- true if this.equals(o), else false
	 * @param {@code Object} -- item to be compared to this
	 */
	@Override public boolean equals(Object o){
		if(this == o)
			return true;
		if(!(o instanceof Card))
			return false;
		Card c = (Card) o;
		return cardRank.equals(c.cardRank) && cardSuit.equals(c.cardSuit);
	}
	
	/**
	 * uses the values of the Enums hashCode and adds them together with 31,
   	 * as seen in Effective Java
	 * @return int
	 */
	@Override public int hashCode(){
		return 31 + cardSuit.hashCode() + cardRank.hashCode();
	}
	
	/**
	 * Safely publish the rank of the card. Since it's an Enum
	 * the value is final, as is the assigned rank
	 * @return {@code Rank}
	 */
	public Rank getRank(){
		return cardRank;
	}
	
	/**
	 * Safely publish the suit of the card. Since it's an Enum
	 * the value is final, as is the assigned suit
	 * @return {@code Suit}
	 */
	public Suit getSuit(){
		return cardSuit;
	}
	
	/**
	 * determines whether or not some card is an ace or not
	 * @return {@code boolean}
	 */
	public boolean isAce(){
		return cardRank.equals(Rank.ACE);
	}
	
	/**
	 * determines what the calue of a card is
	 * @return {@code int}
	 */
	public int cardValue(){
		return cardRank.value;
	}
	
	/**
	 * String representation of a single card
	 * RANK of SUIT
	  * @return {@code String}
	 */
	@Override public String toString(){
		return cardRank + " of " + cardSuit + "\n";
	}
	
	public static void main(String[] args){
		Set<Card> deck = new HashSet<Card>();
		for(int i = 0; i < 52; ++i)
			deck.add(Card.valueOf(i));
		for(Card a : deck)
			System.out.println(a);
		for(Card a : deck)
			System.out.println(a.hashCode());
		System.out.println(deck.size());
		
		for(Card c : deck)
			c.hashCode();
		for(Card c : deck)
			System.out.println("Card value: " + c.cardValue() + "\nAce? " + c.isAce());
	}
}